package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SocketWorker {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private ClientController clientController;

    private BufferedReader inputUser; // поток чтения с консоли
    private String nickname; // имя клиента

    public SocketWorker(String host, int port) {
        try {
            inputUser = new BufferedReader(new InputStreamReader(System.in));
            pressNickname();
            tryToConnect(host, port);
            new Thread(new ReadMsg()).start();
        } catch (IOException e) {
            SocketWorker.this.downService();
            e.printStackTrace();
            System.out.println(e);
        }
    }

    public void setClientController(ClientController controller) {
        this.clientController = controller;
    }

    public void sendMessage(String message) {
        Date time = new Date(); // текущая дата
        SimpleDateFormat dt1 = new SimpleDateFormat("HH:mm:ss"); // берем только время до секунд
        String dtime = dt1.format(time); // время
        if (message.equals("stop")) {
            out.println("stop");
            SocketWorker.this.downService(); // харакири

        } else {
            out.println("(" + dtime + ") " + nickname + ": " + message); // отправляем на сервер
        }
    }

    private void tryToConnect(String host, int port) throws IOException {
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        out.println("My nickname: " + nickname + "\n");
    }

    private void pressNickname() {
        System.out.print("Press your nick: ");
        try {
            nickname = inputUser.readLine();
        } catch (IOException ignored) {
        }

    }

    // нить чтения сообщений с сервера
    private class ReadMsg implements Runnable {
        @Override
        public void run() {

            String str;
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    synchronized (this) {
                        wait(100);
                    }
                    if ((str = in.readLine()) == null) continue;
                    if (clientController != null) {
                        clientController.updateChat(str);
                    }
                }
            } catch (IOException | InterruptedException e) {
                SocketWorker.this.downService();
            }
        }
    }

    public void downService() {
        try {
            out.println("Server, i go out");
            if (!socket.isClosed()) {
                socket.close();
            }
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }
}
