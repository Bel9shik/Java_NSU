package server;

import client.event.messages.Command;
import client.event.messages.Message;
import client.event.messages.TextMessage;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ServerSocketWorker implements Runnable {
    private final Socket socketToClient;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private final Story story;
    private final Thread curThread;
    private String nickname;

    public ServerSocketWorker(Socket socket, Story story) throws IOException {
        this.socketToClient = socket;
        this.story = story;
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
//                System.out.println(message + " " + message.getClass().getName());
                if (message == null) continue;
                else if (message instanceof Command) {
                    System.out.println(((Command) message).getCommand());
                    if (((Command) message).getCommand().equals("/exit")) {
                        synchronized (Server.clientsList) {
                            for (ServerSocketWorker socketWorker : Server.clientsList) {
                                socketWorker.send(new TextMessage(nickname + " disconnected"));
                            }
                        }
                        this.downService();
                        break;
                    }  else if (((Command) message).getCommand().equals("/list")) {
                        ArrayList<String> list = new ArrayList<>(Server.clientsList.size() + 1);
                        list.add("List of participants:\n");
                        for (ServerSocketWorker socketWorker : Server.clientsList) {
                            list.add(socketWorker.getNickname() + "\n");
                        }
                        System.out.println(list);
                        this.send(new TextMessage(list));
                        continue;
                    }
                    story.addStory((Command) message);
                } else if (message instanceof TextMessage) {
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
