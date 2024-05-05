package client;

import client.View.CreateChatFrame;

public class StartClient {

    public static void main(String[] args) {
        System.out.println("Hello World!");

        CreateChatFrame frame = new CreateChatFrame();





//        try (Socket clientSocket = new Socket("localhost", 5000)) {
//
//            try (BufferedReader socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//                 BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
//                 PrintWriter consoleWriter = new PrintWriter(clientSocket.getOutputStream(), true)) {
//                while (true) {
//                    System.out.print("enter message: ");
//                    String message = consoleReader.readLine();
//                    consoleWriter.println(message); // + \n ?
//                    String response = socketReader.readLine();
//                    System.out.println("Server responded: " + response);
//                }
//            }
//
////            new Client("localhost", Server.DEFAULT_PORT);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println(e);
//        }
//    }
}
