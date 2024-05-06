package client;

import client.View.ControllerView;
import client.View.CreateChatFrame;

public class StartClient {

    public static void main(String[] args) {
        System.out.println("Hello World!");

        new ClientController("localhost", 5000);
        new ClientController("localhost", 5000);
        new ClientController("localhost", 5000);

    }
}
