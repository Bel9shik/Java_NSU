import Controller.MainLogic;

import java.io.IOException;

public class Game {

    public static void main(String[] args) {
        MainLogic game = new MainLogic("tetris");
        game.launchGame();
    }
}
