package client;

import client.event.clientEvents.ConnectionLost;
import client.event.clientEvents.DownService;
import client.event.clientEvents.UpdateChat;
import client.view.ControllerView;
import client.event.Event;
import client.event.messages.Message;
import client.event.messages.TextMessage;

import java.io.IOException;

public class ClientController implements Observer {
    private final ControllerView controllerView;
    private final SocketWorker socketWorker;

    public ClientController(String host, int port) {
        socketWorker = new SocketWorker(host, port);
        controllerView = new ControllerView();
        socketWorker.addObserver(this);
        controllerView.addObserver(this);
//        socketWorker.setClientController(this);
//        controllerView.setClientController(this);
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
