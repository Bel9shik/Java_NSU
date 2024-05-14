package View.FX;

import Controller.FigureController;
import Controller.KeyHandler;
import Controller.MainLogic;
import Model.ModelController;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class CreateFrameFXAndStart extends Application {
    ModelController model;
    GamePanelFX gamePanelFX;
    FigureController figureController;

    @Override
    public void start(Stage stage) {
        stage.setTitle("tetris");
        stage.setResizable(false);
        model = new ModelController();
        figureController = new FigureController(model);
        gamePanelFX = new GamePanelFX(model);
        Scene scene = new Scene(gamePanelFX, GamePanelFX.WINDOW_WIDTH, GamePanelFX.WINDOW_HEIGHT);
        scene.setOnKeyPressed(e -> {
            String code = e.getCode().toString();

            switch (code) {
                case "W", "UP" -> KeyHandler.upPressed = true;
                case "A", "LEFT" -> KeyHandler.leftPressed = true;
                case "S", "DOWN" -> KeyHandler.downPressed = true;
                case "D", "RIGHT" -> KeyHandler.rightPressed = true;
            }
        });
        scene.setFill(Color.BLACK);
        stage.setScene(scene);

        AnimationTimer animationTimer = new AnimationTimer() {
            private long lastTime = System.nanoTime();

            @Override
            public void handle(long now) {
                double drawInterval = 1000000000.0 / MainLogic.FPS;
                if (now - lastTime >= drawInterval) {
                    lastTime = now;
                    update();
                }
            }
        };

        animationTimer.start();
        stage.show();
    }

    private void update() {
        if (!model.gameOver) {
            figureController.update();
            gamePanelFX.requestLayout();
        }
    }
}