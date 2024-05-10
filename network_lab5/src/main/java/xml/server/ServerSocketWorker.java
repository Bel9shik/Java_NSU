package xml.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xml.client.event.messages.Command;
import xml.client.event.messages.Message;
import xml.client.event.messages.TextMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ServerSocketWorker implements Runnable {
    private final Socket socketToClient;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private final Story story;
    private final Thread curThread;
    private String nickname;
    private final boolean isLogging;

    private static final Logger logger = LoggerFactory.getLogger(ServerSocketWorker.class);

    public ServerSocketWorker(Socket socket, Story story, boolean isLogging) throws IOException {
        this.socketToClient = socket;
        this.story = story;
        this.isLogging = isLogging;
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        curThread = new Thread(this);
        curThread.start();
        story.showStory(out);
    }

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
            } else {
                out.writeObject("Invalid command");
                out.flush();
                this.downService();
            }
            for (ServerSocketWorker socketWorker : Server.clientsList) {
                socketWorker.send(new TextMessage(nickname + " connected"));
            }
            while (true) {
                synchronized (this) {
                    wait(100);
                }
                message = in.readObject();
                if (message == null) continue;
                else if (message instanceof Command) {
                    if (isLogging) {
                        logger.info(((Command) message).getCommand());
                    }
                    if (((Command) message).getCommand().contains("/exit")) {
                        synchronized (Server.clientsList) {
                            for (ServerSocketWorker socketWorker : Server.clientsList) {
                                socketWorker.send(new TextMessage(nickname + " disconnected"));
                            }
                        }
                        this.downService();
                        break;
                    }  else if (((Command) message).getCommand().contains("/list")) {
                        ArrayList<String> list = new ArrayList<>(Server.clientsList.size() + 1);
                        list.add("List of participants:\n");
                        for (ServerSocketWorker socketWorker : Server.clientsList) {
                            list.add(socketWorker.getNickname() + "\n");
                        }
                        if (isLogging) {
                            logger.info(list.toString());
                        }
                        this.send(new TextMessage(list));
                        continue;
                    }
                    story.addStory((Command) message);
                } else if (message instanceof TextMessage) {
                    if (isLogging) {
                        logger.info(message.toString());
                    }

                    for (ServerSocketWorker socketWorker : Server.clientsList) {
                        socketWorker.send((TextMessage) message);
                    }
                    story.addStory((TextMessage) message);
                }

            }
        } catch (IOException | InterruptedException e) {
            this.downService();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    private synchronized void send(Message message) throws IOException {
        out.writeObject(message);
        out.flush();
    }

    private void downService() {
        try {
            socketToClient.close();
            in.close();
            out.close();
            Server.clientsList.remove(this);
            curThread.interrupt();
            System.out.println("Socket closed");
        } catch (IOException ignored) {
        }
    }
}
