package Controller;

import View.ControllerView;

import java.io.IOException;

public class UserController {

    public ControllerView controllerView;

    public void startGame () throws IOException {
        controllerView = new ControllerView("tetris", "./src/main/java/RESOURCES/titleImage.png", 800, 1000);
        controllerView.createApplicationWindow();
    }
}
