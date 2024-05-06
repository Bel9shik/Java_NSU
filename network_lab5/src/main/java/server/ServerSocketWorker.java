package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerSocketWorker implements Runnable {
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private final Story story;
    private final Thread curThread;
    private String nickname;

    public ServerSocketWorker(Socket socket, Story story) throws IOException {
        this.socket = socket;
        this.story = story;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        curThread = new Thread(this);
        curThread.start();
        story.showStory(out);
    }

    public synchronized String getNickname() {
        return nickname;
    }


    @Override
    public void run() {
        String message;

        try {
            message = in.readLine();
            nickname = message.replaceFirst("My nickname: ", "");
            for (ServerSocketWorker socketWorker : Server.clientsList) {
                socketWorker.send(nickname + " connected");
            }
            while (true) {
                synchronized (this) {
                    wait(100);
                }
                message = in.readLine();
                if (message == null) continue;
                else if (message.equals("Server, i go out")) {
                    synchronized (Server.clientsList) {
                        for (ServerSocketWorker socketWorker : Server.clientsList) {
                            socketWorker.send(nickname + " disconnected");
                        }
                    }
                    this.downService();
                    break;
                } else if (message.contains("/list")) {
                    StringBuilder List = new StringBuilder("List of participants:\n");
                    for (ServerSocketWorker socketWorker : Server.clientsList) {
                        List.append(socketWorker.getNickname()).append("\n");
                    }
                    this.send(List.toString());
                    continue;
                }
                story.addStory(message);
                for (ServerSocketWorker socketWorker : Server.clientsList) {
                    socketWorker.send(message);
                }

            }
        } catch (IOException | InterruptedException e) {
            this.downService();
        }
    }

    private synchronized void send(String message) {
        out.println(message);
    }

    private void downService() {
        try {
            socket.close();
            in.close();
            out.close();
            Server.clientsList.remove(this);
            curThread.interrupt();
            System.out.println("Socket closed");
        } catch (IOException ignored) {
        }
    }
}
