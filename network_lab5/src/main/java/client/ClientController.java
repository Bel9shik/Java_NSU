package client;

import client.View.ControllerView;

public class ClientController {
    private ControllerView controllerView;
    private SocketWorker socketWorker;

    public ClientController(String host, int port) {
        socketWorker = new SocketWorker(host, port);
        controllerView = new ControllerView();
        socketWorker.setClientController(this);
        controllerView.setClientController(this);
    }

    public void sendMessage(String message) {
        if (!message.isEmpty()) {
            socketWorker.sendMessage(message);
        }
    }

    public void connectionLost () {
        controllerView.connectionLost();
    }

    public void updateChat (String message) {
        if (!message.isEmpty()) {
            controllerView.updateChat(message + "\n");
        }
    }

}
