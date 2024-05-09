package client.view;

import client.ClientController;
import client.Observable;
import client.event.clientEvents.DownService;
import client.event.messages.Command;
import client.event.messages.TextMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class ControllerView extends Observable {
    private JFrame frame;
    private JTextArea textArea;
    private JTextField messageField;
    private JButton sendButton;
//    private ClientController clientController;

    public ControllerView() {
        frame = new CreateChatFrame();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                    notifyObservers(new DownService());
            }
        });

        frame.getContentPane().setLayout(new BorderLayout());

        frame.add(new ChatPanel(), BorderLayout.CENTER);
        frame.add(new MsgPanel(), BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);
    }

//    public void setClientController(ClientController clientController) {
//        this.clientController = clientController;
//    }

    private class ChatPanel extends JPanel {
        public ChatPanel() {
            setLayout(new BorderLayout());
            textArea = new JTextArea();
            textArea.setEditable(false);

            JScrollPane scrollPane = new JScrollPane(textArea);
            add(scrollPane, BorderLayout.CENTER);
        }
    }

    private void sendMsg(String msg) {
        if (msg.equals("/exit") || msg.equals("/list")) {
            notifyObservers(new Command(msg));
        } else {
            notifyObservers(new TextMessage(msg));
        }
        messageField.setText("");
    }

    private class MsgPanel extends JPanel {

        public MsgPanel() {
            setLayout(new BorderLayout());
            messageField = new JTextField(30);

            sendButton = new JButton("Send");

            sendButton.addActionListener(e -> sendMsg(messageField.getText()));

            messageField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    int code = e.getKeyCode();

                    if (code == KeyEvent.VK_ENTER) {
                        sendMsg(messageField.getText());
                    }
                }

            });

            add(messageField, BorderLayout.CENTER);
            add(sendButton, BorderLayout.EAST);

        }

    }

    public void connectionLost() {
        JOptionPane.showMessageDialog(frame, "Connection lost.");
        frame.setVisible(false);
    }

    public void updateChat(String message) {
        textArea.append(message);
    }
}
