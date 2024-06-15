package server.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import server.ServerSocketWorker;
import server.StartServer;
import server.Story;

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
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class XMLServerWorker extends server.ServerSocketWorker implements Runnable {
    private final Socket socketToClient;
    private final InputStream in;
    private final OutputStream out;
    private final Story story;
    private final Thread curThread;
    private String nickname;
    private final boolean isLogging;
    private final DocumentBuilder documentBuilder;

    private static final Logger logger = LoggerFactory.getLogger(XMLServerWorker.class);

    public XMLServerWorker(Socket socket, Story story, boolean isLogging) throws IOException, ParserConfigurationException {
        this.socketToClient = socket;
        this.story = story;
        this.isLogging = isLogging;
        documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        out = socket.getOutputStream();
        in = socket.getInputStream();
        curThread = new Thread(this);
        curThread.start();

    }

    public String getNickname() {
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
            sessionElement.setTextContent(String.valueOf(StartServer.uniqueSessionID.incrementAndGet()));
            successElement.appendChild(sessionElement);

        } else if (type.equals("list")) {
            AtomicInteger i = new AtomicInteger(1);
            Element listusersElement = successDocument.createElement("listusers");
            StringBuilder forLog = new StringBuilder("List of users: ");
            StartServer.clientsList.forEach(x -> {
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
        switch (type) {
            case "message" -> {
                eventElement.setAttribute("name", "message");
                Element messageElement = eventDocument.createElement("message");
                messageElement.setTextContent(message);
                Element nameElement = eventDocument.createElement("name");
                nameElement.setTextContent(nickname);

                eventElement.appendChild(messageElement);
                eventElement.appendChild(nameElement);

                eventDocument.appendChild(eventElement);
                return eventDocument;
            }
            case "userLogin" -> {
                eventElement.setAttribute("name", "userLogin");
                Element nicknameElement = eventDocument.createElement("name");
                nicknameElement.setTextContent(nickname);
                eventElement.appendChild(nicknameElement);
                eventDocument.appendChild(eventElement);
                return eventDocument;
            }
            case "userLogout" -> {
                eventElement.setAttribute("name", "userLogout");
                Element nicknameElement = eventDocument.createElement("name");
                nicknameElement.setTextContent(nickname);
                eventElement.appendChild(nicknameElement);
                eventDocument.appendChild(eventElement);
                return eventDocument;
            }
            default -> {
                return null;
            }
        }
    }

    private Document readMessage() throws IOException, SAXException {
        byte[] lengthBytes = new byte[4];
        int bytesRead = in.read(lengthBytes);
        if (bytesRead == -1) {
            throw new IOException("Stream closed unexpectedly");
        }
        int messageLength = ByteBuffer.wrap(lengthBytes).getInt();
        byte[] messageBytes = new byte[messageLength];
        bytesRead = in.read(messageBytes);
        if (bytesRead == -1) {
            throw new IOException("Stream closed unexpectedly");
        }
        String message = new String(messageBytes, StandardCharsets.UTF_8);

        if (message.isEmpty()) {
            throw new SAXException("Received an empty XML message");
        }

        return documentBuilder.parse(new ByteArrayInputStream(message.getBytes()));
    }


    private Document createResponseDocument(Document input) {
        Document response = documentBuilder.newDocument();
        NodeList nodes;
        if ((nodes = input.getElementsByTagName("command")).getLength() > 0) {
            if (nodes.getLength() == 1) {
                Node commandNode = nodes.item(0);
                if (commandNode != null && commandNode.getAttributes() != null) {
                    String command = commandNode.getAttributes().getNamedItem("name").getNodeValue();
                    response = switch (command) {
                        case "login" ->
                                createEventDocument("userLogin", commandNode.getFirstChild().getTextContent(), nickname);
                        case "list" -> createSuccessDocument("list");
                        case "logout" ->
                                createEventDocument("userLogout", commandNode.getFirstChild().getTextContent(), nickname);
                        case "message" ->
                                createEventDocument("message", commandNode.getFirstChild().getTextContent(), nickname);
                        default -> response;
                    };
                }
            }
        }
        return response;
    }


    private Document createResponseDocument(String typeMessage, String message) {
        documentBuilder.newDocument();
        Document response = switch (typeMessage) {
            case "login" -> createEventDocument("userLogin", message, message.split(" ")[0]);
            case "list" -> createSuccessDocument("list");
            case "logout" -> createEventDocument("userLogout", message, message.split(" ")[0]);
            case "message" -> createEventDocument("message", message, nickname);
            default -> null;
        };
        return response;
    }

    private void showStory() {
        LinkedList<String> history = story.getStory();
        if (history == null) return;
        for (int i = 0; i < history.size(); i++) {
            this.send(history.get(i));
        }
    }


    @Override
    public void run() {
        if (clientConnected()) {
            Document response = createSuccessDocument("login");
            this.sendDocument(response);
            if (isLogging) {
                logger.info("{} logged in", nickname);
            }

            showStory();

            synchronized (StartServer.clientsList) {
                for (ServerSocketWorker serverSocketWorker : StartServer.clientsList) {
                    serverSocketWorker.send(nickname + " connected");
                }
            }
        } else {
            Document response = createErrorDocument("Incorrect data");
            this.sendDocument(response);
            this.downService();
        }

        while (true) {
            try {
                Document reqDocument = readMessage();
                Document response = createResponseDocument(reqDocument);

                if (response == null) {
                    logger.error("Response document is null");
                    continue;
                }

                Node tmpNode = response.getElementsByTagName("success").item(0);

                if (tmpNode != null && tmpNode.getAttributes().getLength() == 0) {
                    this.sendDocument(response);
                    continue;
                }

                tmpNode = reqDocument.getElementsByTagName("command").item(0);
                if (tmpNode != null && tmpNode.getAttributes() != null
                        && "logout".equals(tmpNode.getAttributes().getNamedItem("name").getNodeValue())) {
                    synchronized (StartServer.clientsList) {
                        StartServer.clientsList.remove(this);
                        for (ServerSocketWorker serverSocketWorker : StartServer.clientsList) {
                            serverSocketWorker.send(response.getElementsByTagName("name").item(0).getTextContent() + " disconnected");
                        }
                    }
                    this.downService();
                    return;
                }

                Node messageNode = response.getElementsByTagName("message").item(0);
                if (messageNode != null) {
                    story.addStory(messageNode.getTextContent());
                    synchronized (StartServer.clientsList) {
                        for (ServerSocketWorker serverSocketWorker : StartServer.clientsList) {
                            serverSocketWorker.send(messageNode.getTextContent());
                        }
                    }
                } else {
                    logger.error("Message node is null in response document");
                }

            } catch (SAXException | IOException e) {
                logger.error("Error reading message: ", e);
            }
        }
    }



    private synchronized void sendDocument(Document message) {
        if (message == null) return;
        Optional<String> sendMessage = convertDocumentToString(message);
        try {
            if (sendMessage.isPresent()) {
                byte[] messageBytes = sendMessage.get().getBytes(StandardCharsets.UTF_8);
                out.write(ByteBuffer.allocate(4).putInt(messageBytes.length).array());
                out.write(messageBytes);
            }
        } catch (IOException e) {

        }
    }

    @Override
    public void send(String message) {
        if (message.contains("disconnected")) {
            sendDocument(createResponseDocument("logout", message));
        } else if (message.contains("connected")) {
            sendDocument(createResponseDocument("login", message));
        } else if (message.contains("list")) {
            sendDocument(createResponseDocument("list", message));
        } else {
            sendDocument(createResponseDocument("message", message));
        }
    }

    private void downService() {
        try {
            socketToClient.close();
            in.close();
            out.close();
            StartServer.clientsList.remove(this);
            curThread.interrupt();
            if (isLogging) {
                logger.info("{} disconnected", nickname);
            }
        } catch (IOException ignored) {
        }
    }
}
