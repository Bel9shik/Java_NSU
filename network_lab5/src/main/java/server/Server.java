package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {

    private static Story story;

    public static int DEFAULT_PORT = 5000;

    static LinkedList<ServerSocketWorker> clientsList = new LinkedList<>();


    public static void main(String[] args) {
        startServer();
    }

    private static void startServer() {
        System.out.println("Starting server...");
        story = new Story();
        try (ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                clientsList.add(new ServerSocketWorker(socket, story));
            }
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        } finally {
            System.out.println("Server stopped.");
        }
    }

}
