package serialization.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class Server {

    public static int DEFAULT_PORT = 5000;

    static final LinkedList<ServerSocketWorker> clientsList = new LinkedList<>();


    public static void main(String[] args) {
        startServer();
    }

    private static void startServer() {
        System.out.println("Starting server...");
        Story story = new Story();
        ParseConfigFile parseConfigFile = new ParseConfigFile("server.properties");

        try (ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                clientsList.add(new ServerSocketWorker(socket, story, parseConfigFile.getLogStatus()));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
        } finally {
            System.out.println("Server stopped.");
        }





//        try (ServerSocketChannel ssc = ServerSocketChannel.open(); Selector selector = Selector.open()) {
//
//            ssc.socket().bind(new InetSocketAddress(DEFAULT_PORT));
//            ssc.configureBlocking(false);
//            SelectionKey selectionKey = ssc.register(selector, SelectionKey.OP_ACCEPT);
//
//            while (true) {
//                //Обрабатываем доступные к ожиданию подключения с использованием каллбэка
//                selector.select(key -> {
//                    if (key.isAcceptable()) {
//                        try {
//                            //Принятие подключения серверным сокетом
//                            ServerSocketChannel server = (ServerSocketChannel) key.channel();
//                            SocketChannel client = server.accept();
//                            client.configureBlocking(false);
//                            client.register(selector, SelectionKey.OP_READ); //Регистрируем принятое подключение в селекторе с интересующим типом операции - чтение
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    if (key.isReadable()) {
//                        try {
//                            //Тут происходит обработка принятых подключений
//                            SocketChannel client = (SocketChannel) key.channel();
//                            ByteBuffer request = ByteBuffer.allocate(512);
//                            int r = client.read(request);
//                            if (r == -1) {
//                                client.close();
//                            } else {
//                                //В этом блоке происходит обработка запроса
//                                System.out.println(new String(request.array()));
//                                String response = "Hello from server";
//                                //Несмотря на то, что интересующая операция, переданная в селектор - чтение, мы все равно можем писать в сокет
//                                client.write(ByteBuffer.wrap(response.getBytes()));
//                            }
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                });
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            System.out.println("Server stopped.");
//        }


    }

}
