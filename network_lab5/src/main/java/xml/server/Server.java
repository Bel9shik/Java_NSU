package xml.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {

    public static int DEFAULT_PORT = 5000;

    static final LinkedList<ServerSocketWorker> clientsList = new LinkedList<>();


    public static void main(String[] args) {
        startServer();
    }

    private static void startServer() {
        System.out.println("Starting serialization.server...");
        Story story = new Story();
        ParseConfigFile parseConfigFile = new ParseConfigFile("server.properties");
        try (ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                clientsList.add(new ServerSocketWorker(socket, story, parseConfigFile.getLogStatus()));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
        } finally {
            System.out.println("Server stopped.");
        }
    }

}
