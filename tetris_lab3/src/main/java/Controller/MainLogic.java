package Controller;

import Model.PlayManager;
import View.CreateFrame;
import View.GamePanel;

public class MainLogic implements Runnable {

    private final int FPS = 60;

    Thread gameThread;
    PlayManager playManager;
    GamePanel gamePanel;
    CreateFrame mainFrame;


    public MainLogic(String windowTitle) {
        playManager = new PlayManager();
        gamePanel = new GamePanel(playManager);
        mainFrame = new CreateFrame(windowTitle, gamePanel);
    }


    public void launchGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        System.out.println("run");
        double drawInterval = 1000000000.0 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                gamePanel.repaint();
                delta--;
            }
        }
    }

    private void update() {
        if (!playManager.gameOver) {
            playManager.update();
        }
    }
}
