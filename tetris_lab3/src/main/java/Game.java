import Controller.MainLogic;
import View.FX.CreateFrameFXAndStart;
import javafx.application.Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Game {

    public static void main(String[] args) {
        String typeGUI;
        System.out.println("Enter \"Swing\" or \"FX\"");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            typeGUI = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        if (typeGUI.equals("Swing")) {
            MainLogic game = new MainLogic("tetris");
            game.launchGame();
        } else if (typeGUI.equals("FX")) {
            Application.launch(CreateFrameFXAndStart.class);
        }
    }
}
