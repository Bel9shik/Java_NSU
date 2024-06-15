package client;

import server.StartServer;

public class StartClient {
    public static void main(String[] args) {
        new ClientController("localhost", StartServer.DEFAULT_PORT, 0);
        new ClientController("localhost", StartServer.DEFAULT_PORT, 0);
        new ClientController("localhost", StartServer.DEFAULT_PORT, 1);
    }
}
