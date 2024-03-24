import Model.ModelController;

import java.io.IOException;

public class Game {

    public static void main(String[] args) throws IOException {
        System.out.println("TETRIS IS COMING");

        ModelController controller = new ModelController();
        double alpha = Math.atan(2.0/3);
        int mainX = 5;
        int mainY = 20;
        int x = 7;
        int y = 20;
        int x_1 = mainX + (int) Math.round((mainX - x) * Math.cos(90 - alpha) - (mainY - y) * Math.sin(90 - alpha));
        int y_1 = mainY + (int) Math.round((mainX - x) * Math.sin(90 - alpha) + (mainY - y) * Math.cos(90 - alpha));
        System.out.println(x_1);
        System.out.println(y_1);
        x = 5;
        y = 18;
        int x_2 = mainX + (int) Math.round((mainX - x) * Math.cos(90 - alpha) - (mainY - y) * Math.sin(90 - alpha));
        int y_2 = mainY + (int) Math.round((mainX - x) * Math.sin(90 - alpha) + (mainY - y) * Math.cos(90 - alpha));
        System.out.println(x_2);
        System.out.println(y_2);
    }
}
