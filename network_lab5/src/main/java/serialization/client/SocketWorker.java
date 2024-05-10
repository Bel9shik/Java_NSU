package serialization.client;

import serialization.client.event.clientEvents.ConnectionLost;
import serialization.client.event.clientEvents.UpdateChat;
import serialization.client.event.messages.Command;
import serialization.client.event.messages.Message;
import serialization.client.event.messages.TextMessage;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SocketWorker extends Observable {
    private Socket socketToServer;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private BufferedReader inputUser; // поток чтения с консоли
    private String nickname; // имя клиента

    public SocketWorker(String host, int port) {
        try {
            inputUser = new BufferedReader(new InputStreamReader(System.in));
            pressNickname();
            tryToConnect(host, port);
            new Thread(new ReadMsg()).start();
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
            SocketWorker.this.downService();
        }
    }

    public void sendMessage(Message message) throws IOException {
        Date time = new Date(); // текущая дата
        SimpleDateFormat dt1 = new SimpleDateFormat("HH:mm:ss"); // берем только время до секунд
        String dtime = dt1.format(time); // время
        if (message instanceof Command) {
            if (((Command) message).getCommand().equals("/exit")) {
                out.writeObject(new Command("(" + dtime + ") " + nickname + ": /exit"));
                out.flush();
                downService(); // харакири
                notifyObservers(new ConnectionLost());
            } else if (((Command) message).getCommand().equals("/list")) {
                out.writeObject(new Command("(" + dtime + ") " + nickname + ": /list"));
                out.flush();
            }
        } else if (message instanceof TextMessage) {
            out.writeObject(new TextMessage("(" + dtime + ") " + nickname + ": " + message)); // отправляем на сервер
            out.flush();
        }
    }

    private void tryToConnect(String host, int port) throws IOException {
        socketToServer = new Socket(host, port);
        this.out = new ObjectOutputStream(socketToServer.getOutputStream());
        this.in = new ObjectInputStream(socketToServer.getInputStream());
        out.writeObject(new Command("/nickname: " + nickname));
        out.flush();
    }

    private void pressNickname() throws IOException {
        System.out.print("Press your nick: ");
        nickname = inputUser.readLine();
    }

    // нить чтения сообщений с сервера
    private class ReadMsg implements Runnable {
        @Override
        public void run() {

            Object obj;
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    synchronized (this) {
                        wait(100);
                    }
                    if ((obj = in.readObject()) == null) continue;
                    if (obj instanceof TextMessage) {
                        notifyObservers(new UpdateChat(obj.toString()));
                    }
                }
            } catch (IOException | InterruptedException e) {
                SocketWorker.this.downService();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.out.println(e);
            }
        }
    }

    public void downService() {
        try {
            if (!socketToServer.isClosed()) {
                socketToServer.close();
            }
            out.close();
            in.close();
            System.out.println("user logged out");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }
}
