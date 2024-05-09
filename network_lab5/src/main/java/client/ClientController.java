package client;

import client.view.ControllerView;
import messages.Message;
import messages.TextMessage;

import java.io.IOException;

public class ClientController {
    private final ControllerView controllerView;
    private final SocketWorker socketWorker;

    public ClientController(String host, int port) {
        socketWorker = new SocketWorker(host, port);
        controllerView = new ControllerView();
        socketWorker.setClientController(this);
        controllerView.setClientController(this);
    }

    public void sendMessage(Message message) throws IOException {
        socketWorker.sendMessage(message);
    }

    public void downService() {
        socketWorker.downService();
    }

    public void connectionLost() {
        controllerView.connectionLost();
    }

    public void updateChat(TextMessage message) {
        controllerView.updateChat(message + "\n");
    }

}
