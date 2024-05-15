package client;

import client.events.messages.Message;

public abstract class SocketWorker extends Observable {
    public abstract void sendMessage(Message message);

    public abstract void downService();
}
