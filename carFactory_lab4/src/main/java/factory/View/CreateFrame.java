package factory.View;

import javax.swing.*;
import java.awt.*;

public class CreateFrame extends JFrame {

    public final static int WINDOW_WIDTH = 750;
    public final static int WINDOW_HEIGHT = 300;

    public CreateFrame(String title, FactoryPanel factoryPanel) {
        super(title);
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        getContentPane().add(factoryPanel.getMainPanel(), BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        pack();
        setVisible(true);
    }
}
