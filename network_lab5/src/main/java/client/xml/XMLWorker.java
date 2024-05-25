package client.xml;

import client.Serializator;
import client.SocketWorker;
import client.events.clientEvents.ConnectionLost;
import client.events.clientEvents.UpdateChat;
import client.events.messages.Message;
import client.events.messages.Command;
import client.events.messages.TextMessage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class XMLWorker extends SocketWorker implements Serializator {
    private Socket socketToServer;
    private InputStream in;
    private OutputStream out;
    private BufferedReader inputUser;
    private String nickname;
    private DocumentBuilder documentBuilder;
    private long UNIQUE_SESSION_ID = -1;

    public XMLWorker(String host, int port) {
        try {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            inputUser = new BufferedReader(new InputStreamReader(System.in));
            pressNickname();
            connect(host, port);
            if (UNIQUE_SESSION_ID == -1) {
                inputUser.close();
                downService();
            }
            new Thread(new ReadMsg()).start();
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
            XMLWorker.this.downService();
        }
    }

    @Override
    public void sendMessage(Message message) {
        try {
            Document doc;
            if (message instanceof Command) {
                String command = ((Command) message).getCommand();
                if (command.equals("/exit")) {
                    doc = createCommandDocument("logout", "");
                    send(doc);
                    downService();
                    notifyObservers(new ConnectionLost());
                } else if (command.equals("/list")) {
                    doc = createCommandDocument("list", "");
                    send(doc);
                }
            } else if (message instanceof TextMessage) {
                doc = createCommandDocument("message", message.toString());
                send(doc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Message receiveMessage() throws IOException, SAXException {
        Document document = readMessage();
        if (document != null) {
            return new TextMessage(document.getElementsByTagName("message").item(0).getTextContent());
        }
        return null;
    }

    private void requestHistory() throws IOException {
        Document doc = createCommandDocument("history", "");
        send(doc);
    }

    // В методе connect после успешного подключения добавить вызов requestHistory
    @Override
    public void connect(String host, int port) throws IOException, SAXException {
        socketToServer = new Socket(host, port);
        this.out = socketToServer.getOutputStream();
        this.in = socketToServer.getInputStream();
        out.write(0);

        send(createLoginDocument());
        Document doc = readMessage();
        if (doc != null && doc.getDocumentElement().getNodeName().equals("success")) {
            UNIQUE_SESSION_ID = Long.parseLong(doc.getElementsByTagName("session").item(0).getTextContent());
            requestHistory();
        } else {
            UNIQUE_SESSION_ID = -1;
        }
    }

    @Override
    public void disconnect() throws IOException {
        if (socketToServer != null && !socketToServer.isClosed()) {
            socketToServer.close();
        }
        if (out != null) out.close();
        if (in != null) in.close();
    }

    private Document createCommandDocument(String type, String message) {
        Document doc = documentBuilder.newDocument();
        Element command = doc.createElement("command");
        command.setAttribute("name", type);

        if (type.equals("login")) {
            Element name = doc.createElement("name");
            name.setTextContent(nickname);
            Element typeElement = doc.createElement("type");
            typeElement.setTextContent("CHAT_CLIENT_NAME");
            command.appendChild(name);
            command.appendChild(typeElement);
        } else if (type.equals("list") || type.equals("logout")) {
            Element session = doc.createElement("session");
            session.setTextContent(String.valueOf(UNIQUE_SESSION_ID));
            command.appendChild(session);
        } else if (type.equals("message")) {
            Element messageElement = doc.createElement("message");
            String dtime = new SimpleDateFormat("HH:mm:ss").format(new Date());
            messageElement.setTextContent("[" + dtime + "] " + nickname + ": " + message);
            Element session = doc.createElement("session");
            session.setTextContent(String.valueOf(UNIQUE_SESSION_ID));
            command.appendChild(messageElement);
            command.appendChild(session);
        }

        doc.appendChild(command);
        return doc;
    }

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

    private void send(Document document) throws IOException {
        Optional<String> messageString = convertDocumentToString(document);
        if (messageString.isPresent()) {
            byte[] messageBytes = messageString.get().getBytes(StandardCharsets.UTF_8);
            out.write(ByteBuffer.allocate(4).putInt(messageBytes.length).array());
            out.write(messageBytes);
        }
    }

    private Document createLoginDocument() {
        Document doc = documentBuilder.newDocument();
        Element command = doc.createElement("command");
        command.setAttribute("name", "userLogin");
        Element name = doc.createElement("name");
        name.setTextContent(nickname);
        Element type = doc.createElement("type");
        type.setTextContent("CHAT_CLIENT_NAME");
        command.appendChild(name);
        command.appendChild(type);
        doc.appendChild(command);
        return doc;
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
                }
            }
        }
    }

    private void pressNickname() throws IOException {
        System.out.print("Enter your nickname: ");
        nickname = inputUser.readLine();
    }

    private class ReadMsg implements Runnable {
        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Document responseDoc = readMessage();
                    toDoFromDocument(responseDoc);
                }
            } catch (IOException | SAXException e) {
                XMLWorker.this.downService();
            }
        }
    }

    @Override
    public void downService() {
        try {
            disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
