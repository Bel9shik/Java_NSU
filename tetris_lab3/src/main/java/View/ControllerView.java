package View;

import Model.Field;
import Model.Figures.IFigure;
import Model.PairCoords;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ControllerView extends JFrame {

    GamePanel gamePanel;
    private JFrame mainFrame;

    public ControllerView(String windowTitle, String path) throws IOException {
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
