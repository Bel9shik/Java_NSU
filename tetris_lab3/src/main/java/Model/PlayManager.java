package Model;

import Model.Figures.*;
import View.GamePanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static View.GamePanel.*;

public class PlayManager {
    public GeneralFigure currentFigure;
    public GeneralFigure nextFigure;
    public boolean gameOver = false;
    public long score = 0;
    public long lines = 0;

    public static Sound music = new Sound();
    public static Sound soundEffect = new Sound();
    public static int dropInterval = 60; //mino drops in every 60 frames

    private final FigureFactory factoryFigure = new FigureFactory();
    private final List<String> figuresNames = factoryFigure.getBackOfFigure();

    public static ArrayList<Block> staticBlocks = new ArrayList<>();

    public PlayManager() {
        currentFigure = factoryFigure.getFigure(figuresNames.get(0));
        currentFigure.setXY(GamePanel.FIGURE_START_X, GamePanel.FIGURE_START_Y);
        nextFigure = factoryFigure.getFigure(figuresNames.get(1));
        nextFigure.setXY(GamePanel.NEXT_FIGURE_X, GamePanel.NEXT_FIGURE_Y);

        music.play(0, true);
        music.loop();
    }

    public void update() {
        //if current figure is active
        if (currentFigure.isActive) {
            currentFigure.update();
        } else  {
            staticBlocks.addAll(currentFigure.mino);

            if (currentFigure.mino.get(0).coords.getX() == FIGURE_START_X && currentFigure.mino.get(0).coords.getY() == FIGURE_START_Y) {
                gameOver = true;
            }

            currentFigure = nextFigure;
            currentFigure.setXY(FIGURE_START_X, FIGURE_START_Y);
            nextFigure = factoryFigure.getFigure(figuresNames.get((int)(Math.random() * 100) % figuresNames.size()));
            nextFigure.setXY(NEXT_FIGURE_X, NEXT_FIGURE_Y);
            
            //when a mino becomes inactive, check lines
            checkDelete();
        }
    }

    private void checkDelete() {
        int x = left_x;
        int y = top_y;
        int blockCount = 0;
        int linesCount = 0;

        while (x < right_x && y < bottom_y) {
            //count blocks in line
            for (int i = 0; i < staticBlocks.size(); i++) {
                if (staticBlocks.get(i).coords.getX() == x && staticBlocks.get(i).coords.getY() == y) {
                    blockCount++;
                }
            }
            x += Block.SIZE;

            if (x == right_x) {

                // if line filled blocks
                if (blockCount == GamePanel.PLAY_WIDTH / Block.SIZE) {
                    linesCount++;
                    for (int i = staticBlocks.size() - 1; i > -1; i--) {
                        if (staticBlocks.get(i).coords.getY() == y) {
                            staticBlocks.remove(i);
                        }
                    }

                    //move down other blocks
                    for (int i = 0; i < staticBlocks.size(); i++) {
                        if (staticBlocks.get(i).coords.getY() < y) {
                            staticBlocks.get(i).coords.setY(staticBlocks.get(i).coords.getY() + Block.SIZE);
                        }
                    }
                }
                x = left_x;
                y += Block.SIZE;
                blockCount = 0;
            }

            if (linesCount > 0) {
                lines += linesCount;
                score += 100 * (long) Math.pow(3, linesCount);
                linesCount = 0;
            }
        }
    }
 }
