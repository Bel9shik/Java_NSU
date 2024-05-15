package server;

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
                if (Byte.parseByte(String.valueOf(bytes[0])) == 0) { //xml
                    System.out.println("xml SocketWorker");
                    clientsList.add(new server.xml.ServerSocketWorker(socket, story, parseConfigFile.getLogStatus()));
                } else if (Byte.parseByte(String.valueOf(bytes[0])) == 1) { //serial
                    System.out.println("serial SocketWorker");
                    clientsList.add(new server.serial.ServerSocketWorker(socket, story, parseConfigFile.getLogStatus()));
                } else {
                    System.out.println("Unknown command");
                }
            }
        } catch (IOException | ParserConfigurationException e) {
            e.printStackTrace();
            System.out.println(e);
        } finally {
            System.out.println("Server stopped.");
        }
    }
}
