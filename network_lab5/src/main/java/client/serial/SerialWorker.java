package client.serial;

import client.Serializator;
import client.SocketWorker;
import client.events.clientEvents.ConnectionLost;
import client.events.clientEvents.UpdateChat;
import client.events.messages.Message;
import client.events.messages.Command;
import client.events.messages.TextMessage;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SerialWorker extends SocketWorker implements Serializator {
    private Socket socketToServer;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private BufferedReader inputUser;
    private String nickname;

    public SerialWorker(String host, int port) {
        try {
            inputUser = new BufferedReader(new InputStreamReader(System.in));
            pressNickname();
            connect(host, port);
            new Thread(new ReadMsg()).start();
        } catch (IOException e) {
            e.printStackTrace();
            SerialWorker.this.downService();
        }
    }

    @Override
    public void sendMessage(Message message) {
        try {
            Date time = new Date();
            SimpleDateFormat dt1 = new SimpleDateFormat("HH:mm:ss");
            String dtime = dt1.format(time);
            if (message instanceof Command) {
                if (((Command) message).getCommand().equals("/exit")) {
                    out.writeObject(new Command("(" + dtime + ") " + nickname + ": /exit"));
                    out.flush();
                    downService();
                    notifyObservers(new ConnectionLost());
                } else if (((Command) message).getCommand().equals("/list")) {
                    out.writeObject(new Command("(" + dtime + ") " + nickname + ": /list"));
                    out.flush();
                }
            } else if (message instanceof TextMessage) {
                out.writeObject(new TextMessage("(" + dtime + ") " + nickname + ": " + message));
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Message receiveMessage() throws IOException, ClassNotFoundException {
        return (Message) in.readObject();
    }

    @Override
    public void connect(String host, int port) throws IOException {
        socketToServer = new Socket(host, port);
        OutputStream tmpOutput = socketToServer.getOutputStream();
        tmpOutput.write(1);
        tmpOutput.flush();
        this.out = new ObjectOutputStream(tmpOutput);
        this.in = new ObjectInputStream(socketToServer.getInputStream());
        out.writeObject(new Command("/nickname: " + nickname));
        out.flush();
    }

    @Override
    public void disconnect() throws IOException {
        if (!socketToServer.isClosed()) {
            socketToServer.close();
        }
        out.close();
        in.close();
    }

    private void pressNickname() throws IOException {
        System.out.print("Enter your nickname: ");
        nickname = inputUser.readLine();
    }

    private class ReadMsg implements Runnable {
        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    synchronized (this) {
                        wait(100);
                    }
                    Message message = receiveMessage();
                    if (message instanceof TextMessage) {
                        notifyObservers(new UpdateChat(message.toString()));
                    }
                }
            } catch (IOException | ClassNotFoundException | InterruptedException e) {
                SerialWorker.this.downService();
            }
        }
    }

    @Override
    public void downService() {
        try {
            disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
