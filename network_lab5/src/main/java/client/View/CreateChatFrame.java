package client.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CreateChatFrame extends JFrame {


    public CreateChatFrame() {
        setTitle("Very simple chat");
        setPreferredSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
           @Override
           public void windowClosing(WindowEvent e) {
               System.out.println("типа корректно остановилось"); //TODO: доделать нормальную обработку выхода
           }
        });

        pack();
        setVisible(true);
    }

    private class ChatPanel extends JPanel {

    }

    private class MsgPanel extends JPanel {

        public MsgPanel() {
            setLayout(new BorderLayout());
            JTextField textField = new JTextField();

            JButton enterButton = new JButton("Enter");

            enterButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {

                }
            });

            getContentPane().add(textField, BorderLayout.CENTER);
            getContentPane().add(enterButton, BorderLayout.EAST);

        }

    }
}
