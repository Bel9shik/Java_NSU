package client.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CreateChatFrame extends JFrame {


    public CreateChatFrame() {
        setTitle("Chat client");
//        setPreferredSize(new Dimension(800, 600));
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
           @Override
           public void windowClosing(WindowEvent e) {
               System.out.println("типа корректно остановилось"); //TODO: доделать нормальную обработку выхода
           }
        });

    }

}
