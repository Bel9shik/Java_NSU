package client;

import server.Server;

import java.io.*;
import java.net.Socket;

public class Client {
    Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Client(String host, int port) throws IOException {
        Socket socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//        new Thread(new ReadMsg()).start();
//        new Thread(new SendMsg()).start();
    }


    private class ReadMsg implements Runnable {

        @Override
        public void run() {
            String response;
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (true) {
                    response = in.readLine();
                    if (response.equals("bye bye")) {
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e);
            }
        }
    }


    private class SendMsg implements Runnable {

        @Override
        public void run() {
            String inputLine;
            try {
                in = new BufferedReader(new InputStreamReader(System.in));
                while (true) {
                    inputLine = in.readLine();
                    if (inputLine.equals("bye bye")) { //TODO: change in future
                        break;
                    }
                    for (Socket client : Server.clientsList) {
                        if (client.equals(socket)) {
                            System.out.println("sockets equals");
                            continue;
                        }
                        out.println(inputLine);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e);
            }
        }
    }

}
