package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.serial.SerialServerWorker;
import server.xml.XMLServerWorker;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class StartServer {

    public static int DEFAULT_PORT = 5000;

    public static final LinkedList<ServerSocketWorker> clientsList = new LinkedList<>();

    public static AtomicInteger uniqueSessionID = new AtomicInteger(0);

    private static final Logger logger = LoggerFactory.getLogger(StartServer.class);

    public static void main(String[] args) {
        startServer();
    }

    private static void startServer() {
        System.out.println("Starting server...");
        Story story = new Story();
        ParseConfigFile parseConfigFile = new ParseConfigFile("server.properties");

        try (ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                InputStream inputStream = socket.getInputStream();
                byte[] bytes = new byte[1];
                inputStream.read(bytes);
                if (bytes[0] == 0) { // XML
                    clientsList.add(new XMLServerWorker(socket, story, parseConfigFile.getLogStatus()));
                } else if (bytes[0] == 1) { // Serialized
                    clientsList.add(new SerialServerWorker(socket, story, parseConfigFile.getLogStatus()));
                } else {
                    System.out.println("Unknown command");
                    socket.close();
                }
            }
        } catch (IOException | ParserConfigurationException e) {
            logger.error(e.getMessage(), e);
        } finally {
            System.out.println("Server stopped.");
        }
    }
}
