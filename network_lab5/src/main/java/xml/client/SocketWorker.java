package xml.client;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import xml.client.event.clientEvents.ConnectionLost;
import xml.client.event.clientEvents.UpdateChat;
import xml.client.event.messages.Command;
import xml.client.event.messages.Message;
import xml.client.event.messages.TextMessage;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class SocketWorker extends Observable {
    private Socket socketToServer;
    private InputStream in;
    private OutputStream out;

    private BufferedReader inputUser; // поток чтения с консоли
    private String nickname; // имя клиента
    private DocumentBuilder documentBuilder;
    private long UNIQUE_SESSION_ID;

    public SocketWorker(String host, int port) {
        try {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            inputUser = new BufferedReader(new InputStreamReader(System.in));
            pressNickname();
            tryToConnect(host, port);
            if (UNIQUE_SESSION_ID == -1) {
                inputUser.close();
                downService();
            }
            new Thread(new ReadMsg()).start();
        } catch (IOException | ParserConfigurationException | SAXException e) {
            System.out.println(e);
            e.printStackTrace();
            SocketWorker.this.downService();
        }
    }

    private Document createCommandDocument(String type, String message) {
        Document doc = documentBuilder.newDocument();
        Element command = doc.createElement("command");

        if (type.equals("login")) {
            command.setAttribute("name", "login");
            Element name = doc.createElement("name");
            name.setTextContent(nickname);
            Element typeElement = doc.createElement("type");
            typeElement.setTextContent("CHAT_CLIENT_NAME");
            command.appendChild(name);
            command.appendChild(typeElement);
        } else if (type.equals("list")) {
            command.setAttribute("name", "list");
            Element session = doc.createElement("session");
            session.setTextContent(String.valueOf(UNIQUE_SESSION_ID));
            command.appendChild(session);
        } else if (type.equals("message")) {
            command.setAttribute("name", "message");
            Element messageElement = doc.createElement("message");
            Date time = new Date(); // текущая дата
            SimpleDateFormat dt1 = new SimpleDateFormat("HH:mm:ss"); // берем только время до секунд
            String dtime = dt1.format(time); // время
            messageElement.setTextContent("[" + dtime + "] " + nickname + ": " + message);
            Element session = doc.createElement("session");
            session.setTextContent(String.valueOf(UNIQUE_SESSION_ID));
            command.appendChild(messageElement);
            command.appendChild(session);
        } else if (type.equals("logout")) {
            command.setAttribute("name", "logout");
            Element session = doc.createElement("session");
            session.setTextContent(String.valueOf(UNIQUE_SESSION_ID));
            command.appendChild(session);
        }

        doc.appendChild(command);
        return doc;
    }

    public void sendMessage(Message message) throws IOException { // связка между клиентом и сервером

        Document doc;
        if (message instanceof Command) {
            if (((Command) message).getCommand().equals("/exit")) {
                doc = createCommandDocument("logout", "");
                send(doc);
                downService(); // харакири
                notifyObservers(new ConnectionLost());
            } else if (((Command) message).getCommand().equals("/list")) {
                doc = createCommandDocument("list", "");
                send(doc);
            }
        } else if (message instanceof TextMessage) {
            doc = createCommandDocument("message", message.toString());
            send(doc); // отправляем на сервер
        }
    }

    private void toDoFromDocument(Document document) {
        NodeList nodes;
        if ((nodes = document.getElementsByTagName("event")).getLength() > 0) {
            if (nodes.getLength() == 1) {
                String command = nodes.item(0).getAttributes().getNamedItem("name").getNodeValue();
                switch (command) {
                    case "userLogin":
                        notifyObservers(new UpdateChat(document.getElementsByTagName("name").item(0).getTextContent() + " connected"));
                        break;
                    case "userLogout":
                        notifyObservers(new UpdateChat(nodes.item(0).getFirstChild().getTextContent() + " disconnected"));
                        break;
                    case "message":
                        notifyObservers(new UpdateChat(document.getElementsByTagName("message").item(0).getTextContent()));
                        break;
                }
            }
        } else if ((nodes = document.getElementsByTagName("success")).getLength() > 0) {
            if (nodes.item(0).getAttributes().getLength() == 0) {
                NodeList users = document.getElementsByTagName("user");
                if (users.getLength() != 0) {
                    StringBuilder list = new StringBuilder();
                    list.append("List of participants:\n");
                    for (int i = 0; i < users.getLength(); i++) {
                        String username = users.item(i).getTextContent().replace(String.format("CHAT_CLIENT_%d", i + 1), "");
                        list.append(username).append("\n");
                    }
                    notifyObservers(new UpdateChat(list.toString()));
                } //else типа другая команда
            }
        }
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

    public void send(Document message) throws IOException {
        Optional<String> sendMessage = convertDocumentToString(message);
        if (sendMessage.isPresent()) {
            byte[] messageBytes = sendMessage.get().getBytes(StandardCharsets.UTF_8);
            out.write(ByteBuffer.allocate(4).putInt(messageBytes.length).array());
            out.write(messageBytes);
        }
    }

    private Document createLoginDocument() {
        Document nicknameDocument = documentBuilder.newDocument();
        Element nicknameElement = nicknameDocument.createElement("command");
        nicknameElement.setAttribute("name", "userLogin");
        Element name = nicknameDocument.createElement("name");
        name.setTextContent(nickname);
        Element chatClientName = nicknameDocument.createElement("type");
        chatClientName.setTextContent("CHAT_CLIENT_NAME");

        nicknameElement.appendChild(name);
        nicknameElement.appendChild(chatClientName);

        nicknameDocument.appendChild(nicknameElement);

        return nicknameDocument;
    }

    private void tryToConnect(String host, int port) throws IOException, SAXException {
        socketToServer = new Socket(host, port);
        this.out = socketToServer.getOutputStream();
        this.in = socketToServer.getInputStream();

        send(createLoginDocument());
        Document doc = readMessage();
        if (doc.getDocumentElement().getNodeName().equals("success")) {
            UNIQUE_SESSION_ID = Long.parseLong(doc.getElementsByTagName("success").item(0).getTextContent());
        } else {
            UNIQUE_SESSION_ID = -1;
        }

    }

    private void pressNickname() throws IOException {
        System.out.print("Press your nick: ");
        nickname = inputUser.readLine();
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

    // нить чтения сообщений с сервера
    private class ReadMsg implements Runnable {
        @Override
        public void run() {

            Document responseDoc;
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    synchronized (this) {
                        wait(100);
                    }
                    responseDoc = readMessage();
                    toDoFromDocument(responseDoc);
                }
            } catch (IOException | InterruptedException e) {
                SocketWorker.this.downService();
            } catch (SAXException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void downService() {
        try {
            if (!socketToServer.isClosed()) {
                socketToServer.close();
            }
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }
}
