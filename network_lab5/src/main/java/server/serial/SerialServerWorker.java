package server.serial;

import client.events.messages.Command;
import client.events.messages.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.StartServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

public class SerialServerWorker extends server.ServerSocketWorker implements Runnable {
    private final Socket socketToClient;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private final server.Story story;
    private final Thread curThread;
    private String nickname;
    private final boolean isLogging;

    private static final Logger logger = LoggerFactory.getLogger(SerialServerWorker.class);

    public SerialServerWorker(Socket socket, server.Story story, boolean isLogging) throws IOException {
        this.socketToClient = socket;
        this.story = story;
        this.isLogging = isLogging;
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        curThread = new Thread(this);
        curThread.start();
        showStory();
    }

    private void showStory() {
        LinkedList<String> history = story.getStory();
        if (history == null) return;
        for (int i = 0; i < history.size(); i++) {
            this.send(history.get(i));
        }
    }

    @Override
    public synchronized String getNickname() {
        return nickname;
    }


    @Override
    public void run() {
        Object message;

        try {
            message = in.readObject();
            if (message instanceof Command) {
                nickname = ((Command) message).getCommand().replaceFirst("/nickname: ", "");
                showStory();
            } else {
                out.writeObject("Invalid command");
                out.flush();
                this.downService();
            }
            for (server.ServerSocketWorker socketWorker : StartServer.clientsList) {
                socketWorker.send(nickname + " connected");
            }

            while (true) {
                message = in.readObject();
                if (message == null) continue;
                else if (message instanceof Command) {
                    if (isLogging) {
                        logger.info(((Command) message).getCommand());
                    }
                    if (((Command) message).getCommand().contains("/exit")) {
                        synchronized (StartServer.clientsList) {
                            for (server.ServerSocketWorker socketWorker : StartServer.clientsList) {
                                socketWorker.send(nickname + " disconnected");
                            }
                        }
                        this.downService();
                        break;
                    }  else if (((Command) message).getCommand().contains("/list")) {
                        ArrayList<String> list = new ArrayList<>(StartServer.clientsList.size() + 1);
                        list.add("List of participants:\n");
                        for (server.ServerSocketWorker socketWorker : StartServer.clientsList) {
                            list.add(socketWorker.getNickname() + "\n");
                        }
                        if (isLogging) {
                            logger.info(list.toString());
                        }
                        this.send((new TextMessage(list)).toString());
                        continue;
                    }
                    story.addStory(((Command) message).getCommand());
                } else if (message instanceof TextMessage) {
                    if (isLogging) {
                        logger.info(message.toString());
                    }

                    for (server.ServerSocketWorker socketWorker : StartServer.clientsList) {
                        socketWorker.send(message.toString());
                    }
                    story.addStory(message.toString());
                }

            }
        } catch (IOException e) {
            this.downService();
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public synchronized void send(String message) {
        try {
            out.writeObject(new TextMessage(message));
            out.flush();
        } catch (IOException e) {
            System.out.println("Can not send message");
        }
    }

    private void downService() {
        try {
            socketToClient.close();
            StartServer.clientsList.remove(this);
            curThread.interrupt();
            System.out.println("Socket closed");
        } catch (IOException ignored) {
        }
    }
}
