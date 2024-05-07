package Controller;

import Model.ModelController;
import View.FX.GamePanelFX;
import View.swing.CreateFrameSwing;
import View.swing.GamePanelSwing;

public final class MainLogic implements Runnable {

    public static final int FPS = 60;

    Thread gameThread;
    ModelController model;
    GamePanelSwing gamePanelSwing;
    CreateFrameSwing frameSwing;
    FigureController figureController;

    public MainLogic(String windowTitle) {
        model = new ModelController();
        figureController = new FigureController(model);
        gamePanelSwing = new GamePanelSwing(model);
        frameSwing = new CreateFrameSwing(windowTitle, gamePanelSwing);
        frameSwing.showFrame();
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
                gamePanelSwing.repaint();
                delta--;
            }
        }
    }

    private void update() {
        if (!model.gameOver) {
            figureController.update();
        }
    }
}
