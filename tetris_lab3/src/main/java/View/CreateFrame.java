package View;

import javax.swing.*;
import java.io.IOException;

public class CreateFrame extends JFrame {
    GamePanel gamePanel;
    private JFrame mainFrame;

    public CreateFrame(String windowTitle, String path) throws IOException {
        mainFrame = new JFrame(windowTitle);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);

        gamePanel = new GamePanel();
        mainFrame.add(gamePanel);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
        gamePanel.launchGame();
    }
}
