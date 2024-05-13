package serialization.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class Server {

    public static int DEFAULT_PORT = 5000;

    static final LinkedList<ServerSocketWorker> clientsList = new LinkedList<>();


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
