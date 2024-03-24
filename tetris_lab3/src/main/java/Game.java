import Controller.GeneralController;

import java.io.IOException;

public class Game {

    public static void main(String[] args) throws IOException {
        System.out.println("TETRIS IS COMING");
        GeneralController controller = new GeneralController();
        controller.startGame();
        double alpha = Math.atan(2.0/3);
        int mainX = 5;
        int mainY = 8;
        int x = 6;
        int y = 7;
        int x_1 = mainX + (int) Math.round((mainX - x) * Math.cos(90 - alpha) - (mainY - y) * Math.sin(90 - alpha));
        int y_1 = mainY + (int) Math.round((mainX - x) * Math.sin(90 - alpha) + (mainY - y) * Math.cos(90 - alpha));
        System.out.println(x_1);
        System.out.println(y_1);
    }
}
