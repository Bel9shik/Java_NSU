package server;

import client.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {

    public static int DEFAULT_PORT = 5000;


    public static void main(String[] args) {
        System.out.println("Starting server...");
//        startServer();
    }

//    private static void startServer() {
//        try (ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT)) {
//            while (true) {
//                Socket socket = serverSocket.accept();
//                System.out.println("New connection from " + socket.getRemoteSocketAddress());
//                clientsList.add(new SocketWorker(socket));
//            }
//        } catch (IOException e) {
//            System.out.println(e);
//            e.printStackTrace();
//        } finally {
//            System.out.println("Server stopped.");
//        }
//    }

}
