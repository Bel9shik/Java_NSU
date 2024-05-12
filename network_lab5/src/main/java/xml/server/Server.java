package xml.server;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {

    public static int DEFAULT_PORT = 5000;

    static final LinkedList<ServerSocketWorker> clientsList = new LinkedList<>();

    public static AtomicInteger uniqueSessionID = new AtomicInteger(0);


    public static void main(String[] args) {
        startServer();
    }

    private static void startServer() {
        System.out.println("Starting xml.server...");
        Story story = new Story();
        ParseConfigFile parseConfigFile = new ParseConfigFile("server.properties");

        try (ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                clientsList.add(new ServerSocketWorker(socket, story, parseConfigFile.getLogStatus()));
            }
        } catch (IOException | ParserConfigurationException e) {
            e.printStackTrace();
            System.out.println(e);
        } finally {
            System.out.println("Server stopped.");
        }
    }

}
