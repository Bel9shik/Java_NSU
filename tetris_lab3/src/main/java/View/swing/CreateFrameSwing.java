package View.swing;

import javax.swing.*;

public class CreateFrameSwing extends JFrame {
    GamePanelSwing gamePanelSwing;
    private JFrame mainFrame;

    public CreateFrameSwing(String windowTitle, GamePanelSwing gamePanelSwing) {
        mainFrame = new JFrame(windowTitle);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);

        this.gamePanelSwing = gamePanelSwing;
        mainFrame.add(gamePanelSwing);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
    }

    public void showFrame() {
        mainFrame.setVisible(true);
    }
}