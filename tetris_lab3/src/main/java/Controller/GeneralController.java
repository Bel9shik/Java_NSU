package Controller;

import View.ControllerView;

import java.io.IOException;

public class GeneralController {

    public ControllerView controllerView;

    public void startGame () throws IOException {
        controllerView = new ControllerView("tetris", "/home/bel9sh/IdeaProjects/Java_NSU/lab3/src/main/java/RESOURCES/titleImage.png", 800, 1000);
        controllerView.createApplicationWindow();
    }
}
