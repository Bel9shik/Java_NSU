package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerSocketWorker extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String nickname;
    private Story story;

    public ServerSocketWorker(Socket socket, Story story) throws IOException {
        this.socket = socket;
        this.story = story;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        this.start();
        story.showStory(out);
    }

    @Override
    public void run() {
        String message;

        try {
            message = in.readLine();
            nickname = message.replaceFirst("My nickname: ", "");
            System.out.println("nickname news connection: " + nickname);
            while (true) {
                synchronized (this) {
                    wait(100);
                }
                message = in.readLine();
                if (message == null) continue;
                if (message.equals("bye")) {
                    this.downService();
                    break;
                }
                System.out.println(message);
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
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                for (ServerSocketWorker vr : Server.clientsList) {
                    if (vr.equals(this)) {
                        vr.interrupt();
                        Server.clientsList.remove(this);
                    }
                }
            }
        } catch (IOException ignored) {
        }
    }
}
