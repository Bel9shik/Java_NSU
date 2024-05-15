package client;

import client.events.Event;
import client.events.clientEvents.ConnectionLost;
import client.events.clientEvents.DownService;
import client.events.clientEvents.UpdateChat;
import client.events.messages.Message;
import client.events.messages.TextMessage;
import client.view.ControllerView;

import java.io.IOException;

public class ClientController implements Observer {
    private final ControllerView controllerView;
    private client.SocketWorker socketWorker;

    public ClientController(String host, int port, int type) {
        if (type == 0) {
            socketWorker = new client.xml.SocketWorker(host, port);
        } else if (type == 1) {
            socketWorker = new client.serial.SocketWorker(host, port);
        }
        controllerView = new ControllerView();
        socketWorker.addObserver(this);
        controllerView.addObserver(this);
    }

    private void sendMessage(Message message) throws IOException {
        socketWorker.sendMessage(message);
    }

    private void downService() {
        socketWorker.downService();
    }

    private void connectionLost() {
        controllerView.connectionLost();
    }

    private void updateChat(TextMessage message) {
        controllerView.updateChat(message + "\n");
    }

    @Override
    public void update(Event event) {
        if (event instanceof Message) {
            try {
                sendMessage((Message) event);
            } catch (IOException e) {
                updateChat(new TextMessage("Couldn't send the message."));
            }
        } else if (event instanceof DownService) {
            downService();
        } else if (event instanceof ConnectionLost) {
            connectionLost();
        } else if (event instanceof UpdateChat) {
            updateChat(new TextMessage(((UpdateChat) event).getMessage()));
        }
    }
}
