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
            tryToConnect(host, port);
            inputUser = new BufferedReader(new InputStreamReader(System.in));
            pressNickname();
            new Thread(new ReadMsg()).start();
        } catch (IOException e) {
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
    }

    private void pressNickname() {
        System.out.print("Press your nick: ");
        try {
            nickname = inputUser.readLine();
            out.println("My nickname: " + nickname + "\n");
        } catch (IOException ignored) {
        }

    }

    // нить чтения сообщений с сервера
    private class ReadMsg implements Runnable {
        @Override
        public void run() {
            synchronized (this) {

                String str;
                try {
                    while (true) {
                        wait(100);
                        if ((str = in.readLine()) == null) continue;
                        if (str.equals("stop")) {
                            SocketWorker.this.downService(); // харакири
                            break; // выходим из цикла если пришло "stop"
                        }
                        System.out.println(str); // пишем сообщение с сервера на консоль
                        if (clientController != null) {
                            clientController.updateChat(str);
                        }
                    }
                } catch (IOException | InterruptedException e) {
                    SocketWorker.this.downService();
                }
            }
        }
    }

    private void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
            }
            out.close();
            in.close();
        } catch (IOException e) {
        }
    }
}
