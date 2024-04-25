package factory.View;

import javax.swing.*;
import java.awt.*;

public class CreateFrame extends JFrame {

    public final static int WINDOW_WIDTH = 800;
    public final static int WINDOW_HEIGHT = 300;

    private FactoryPanel factoryPanel;

    public CreateFrame(String title, FactoryPanel factoryPanel) {
        super(title);
        this.factoryPanel = factoryPanel;
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        getContentPane().add(factoryPanel.getMainPanel(), BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        pack();
        setVisible(true);
    }
}
