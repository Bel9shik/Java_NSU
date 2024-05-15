package server;

public abstract class ServerSocketWorker {
    public abstract void send (String message);

    public abstract String getNickname();
}
