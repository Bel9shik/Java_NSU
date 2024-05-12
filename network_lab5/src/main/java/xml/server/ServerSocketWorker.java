package xml.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerSocketWorker implements Runnable {
    private final Socket socketToClient;
    private final InputStream in;
    private final OutputStream out;
    private final Story story;
    private final Thread curThread;
    private String nickname;
    private final boolean isLogging;
    private final DocumentBuilder documentBuilder;

    private static final Logger logger = LoggerFactory.getLogger(ServerSocketWorker.class);

    public ServerSocketWorker(Socket socket, Story story, boolean isLogging) throws IOException, ParserConfigurationException {
        this.socketToClient = socket;
        this.story = story;
        this.isLogging = isLogging;
        documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        out = socket.getOutputStream();
        in = socket.getInputStream();
        curThread = new Thread(this);
        curThread.start();
    }

    public synchronized String getNickname() {
        return nickname;
    }

    private boolean clientConnected() {
        try {
            Document reqDocument = readMessage();
            nickname = reqDocument.getElementsByTagName("name").item(0).getTextContent();
        } catch (IOException | SAXException e) {
            return false;
        }

        return true;
    }

    // Метод для конвертации XML-документа в строку
    private Optional<String> convertDocumentToString(Document document) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(writer));
            return Optional.of(writer.getBuffer().toString());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Document createErrorDocument(String reason) {
        Document errorDocument = documentBuilder.newDocument();

        Element errorElement = errorDocument.createElement("error");
        Element messageElement = errorDocument.createElement("message");
        messageElement.setTextContent(reason);
        errorElement.appendChild(messageElement);

        errorDocument.appendChild(errorElement);

        return errorDocument;
    }

    private Document createSuccessDocument(String type) {

        Document successDocument = documentBuilder.newDocument();
        Element successElement = successDocument.createElement("success");
        if (type.equals("login")) {
            Element sessionElement = successDocument.createElement("session");
            sessionElement.setTextContent(String.valueOf(Server.uniqueSessionID.incrementAndGet()));
            successElement.appendChild(sessionElement);

        } else if (type.equals("list")) {
            AtomicInteger i = new AtomicInteger(1);
            Element listusersElement = successDocument.createElement("listusers");
            StringBuilder forLog = new StringBuilder("List of users: ");
            Server.clientsList.forEach(x -> {
                Element userElement = successDocument.createElement("user");
                Element nameElement = successDocument.createElement("name");
                nameElement.setTextContent(x.getNickname());
                forLog.append(x.getNickname()).append(",");
                Element typeElement = successDocument.createElement("type");
                typeElement.setTextContent(String.format("CHAT_CLIENT_%d", i.getAndIncrement()));
                userElement.appendChild(nameElement);
                userElement.appendChild(typeElement);
                listusersElement.appendChild(userElement);
            });
            if (isLogging) {
                logger.info(forLog.toString());
            }
            successElement.appendChild(listusersElement);
        }

        successDocument.appendChild(successElement);
        return successDocument;
    }

    private Document createEventDocument(String type, String message, String nickname) {
        Document eventDocument = documentBuilder.newDocument();
        Element eventElement = eventDocument.createElement("event");
        if (type.equals("message")) {
            eventElement.setAttribute("name", "message");
            Element messageElement = eventDocument.createElement("message");
            messageElement.setTextContent(message);
            Element nameElement = eventDocument.createElement("name");
            nameElement.setTextContent(nickname);

            eventElement.appendChild(messageElement);
            eventElement.appendChild(nameElement);

        } else if (type.equals("userLogin")) {
            eventElement.setAttribute("name", "userLogin");
            Element nicknameElement = eventDocument.createElement("name");
            nicknameElement.setTextContent(nickname);
            eventElement.appendChild(nicknameElement);
        } else if (type.equals("userLogout")) {
            eventElement.setAttribute("name", "userLogout");
            Element nicknameElement = eventDocument.createElement("name");
            nicknameElement.setTextContent(nickname);
            eventElement.appendChild(nicknameElement);
        }
        eventDocument.appendChild(eventElement);
        return eventDocument;
    }

    private Document readMessage() throws IOException, SAXException {
        byte[] lengthBytes = new byte[4];
        in.read(lengthBytes);
        int messageLength = ByteBuffer.wrap(lengthBytes).getInt();
        byte[] messageBytes = new byte[messageLength];
        in.read(messageBytes);
        String message = new String(messageBytes, StandardCharsets.UTF_8);

        return documentBuilder.parse(new ByteArrayInputStream(message.getBytes()));
    }

    private Document createResponseDocument(Document input) {
        Document response = documentBuilder.newDocument();
        NodeList nodes;
        if ((nodes = input.getElementsByTagName("command")).getLength() > 0) {
            if (nodes.getLength() == 1) {
                String command = nodes.item(0).getAttributes().getNamedItem("name").getNodeValue();
                switch (command) {
                    case "login":
                        response = createEventDocument("userLogin", nodes.item(0).getFirstChild().getTextContent(), nickname);
                        break;
                    case "list":
                        response = createSuccessDocument("list");
                        break;
                    case "logout":
                        response = createEventDocument("userLogout", nodes.item(0).getFirstChild().getTextContent(), nickname);
                        break;
                    case "message":
                        response = createEventDocument("message", nodes.item(0).getFirstChild().getTextContent(), nickname);
                        break;
                }
            }
        }
        return response;
    }


    @Override
    public void run() {

        try {
            if (clientConnected()) {
                Document response = createSuccessDocument("login");
                this.send(response);
                if (isLogging) {
                    logger.info("{} logged in", nickname);
                }

                if (story.getStory() != null) {
                    for (Document document : story.getStory()) {
                        this.send(document);
                    }
                }
                for (ServerSocketWorker serverSocketWorker : Server.clientsList) {
                    serverSocketWorker.send(createEventDocument("userLogin", "", nickname));
                }
            } else {
                Document response = createErrorDocument("Incorrect data");
                this.send(response);
                this.downService();
            }

            while (true) {

                synchronized (this) {
                    wait(100);
                }

                try {
                    Document reqDocument = readMessage();

                    Document response = createResponseDocument(reqDocument);

                    Node tmpNode = response.getElementsByTagName("success").item(0);

                    if (isLogging) {
                        logger.info(response.getFirstChild().getTextContent());
                    }

                    if(tmpNode != null && tmpNode.getAttributes().getLength() == 0) {
                        this.send(response);
                        continue;
                    }

                    tmpNode = reqDocument.getElementsByTagName("command").item(0);
                    if (tmpNode != null && tmpNode.getAttributes().getLength() != 0 && tmpNode.getAttributes().getNamedItem("name").getNodeValue().equals("logout")) {
                        synchronized (Server.clientsList) {
                            Server.clientsList.remove(this);
                            for (ServerSocketWorker serverSocketWorker : Server.clientsList) {
                                serverSocketWorker.send(response);
                            }
                        }
                        this.downService();
                        return;
                    }

                    story.addStory(response);
                    synchronized (Server.clientsList) {
                        for (ServerSocketWorker serverSocketWorker : Server.clientsList) {
                            serverSocketWorker.send(response);
                        }
                    }

                } catch (SAXException ignored) {}

            }


        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println(e);
            this.downService();
        }
    }

    private synchronized void send(Document message) throws IOException {
        Optional<String> sendMessage = convertDocumentToString(message);
        if (sendMessage.isPresent()) {
            byte[] messageBytes = sendMessage.get().getBytes(StandardCharsets.UTF_8);
            out.write(ByteBuffer.allocate(4).putInt(messageBytes.length).array());
            out.write(messageBytes);
        }
    }

    private void downService() {
        try {
            socketToClient.close();
            in.close();
            out.close();
            Server.clientsList.remove(this);
            curThread.interrupt();
            if (isLogging) {
                logger.info("{} disconnected", nickname);
            }
        } catch (IOException ignored) {
        }
    }
}
