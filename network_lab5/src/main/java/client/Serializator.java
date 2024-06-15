package client;

import client.events.messages.Message;
import org.xml.sax.SAXException;

import java.io.IOException;

public interface Serializator {
    void sendMessage(Message message) throws IOException;
    Message receiveMessage() throws IOException, ClassNotFoundException, SAXException;
    void connect(String host, int port) throws IOException, SAXException;
    void disconnect() throws IOException;
}
