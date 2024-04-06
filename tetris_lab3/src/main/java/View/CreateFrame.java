package View;

import javax.swing.*;

public class CreateFrame extends JFrame {
    GamePanel gamePanel;
    private JFrame mainFrame;

    public CreateFrame(String windowTitle, GamePanel gamePanel) {
        mainFrame = new JFrame(windowTitle);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);

        this.gamePanel = gamePanel;
        mainFrame.add(gamePanel);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }
}