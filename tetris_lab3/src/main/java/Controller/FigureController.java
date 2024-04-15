package Controller;

import Model.Block;
import Model.ModelController;
import View.GamePanel;

import java.util.ArrayList;
import java.util.List;

import static View.GamePanel.*;

public final class FigureController {
    private final FigureFactory figureFactory;
    private final ModelController model;
    int autoDropCounter = 0; //controller
    boolean leftCollision, rightCollision, bottomCollision;
    boolean deactivating;
    int deactivateCounter = 0;
    public boolean currentFigureIsActive = true;

    public Sound music = new Sound(); // controller
    public static Sound soundEffect = new Sound();

    private int extremeCoordinateLeftX;
    private int extremeCoordinateRightX;
    List<Block> tempBlocks = new ArrayList<>(); //for check rotate

    public FigureController(ModelController modelController) {
        figureFactory = new FigureFactory();
        this.model = modelController;
        model.currentFigure = figureFactory.getNextFigure();
        setXY(FIGURE_START_X, FIGURE_START_Y, model.currentFigure);
        model.nextFigure = figureFactory.getNextFigure();
        setXY(NEXT_FIGURE_X, NEXT_FIGURE_Y, model.nextFigure);
    }

    public void rotateFigure() {
        tempBlocks = model.currentFigure;//write new coords in tempBlocks
            //rotate
            int centerX = model.currentFigure.get(0).coords.getX();
            int centerY = model.currentFigure.get(0).coords.getY();
            for (int i = 0; i < tempBlocks.size(); i++) {
                int deltaX = model.currentFigure.get(i).coords.getX() - centerX;
                int deltaY = model.currentFigure.get(i).coords.getY() - centerY;
                tempBlocks.get(i).coords.setX(centerX - deltaY);
                tempBlocks.get(i).coords.setY(centerY + deltaX);
        }
    }

    public void update() { //update figures
        //if current figure is active
        if (currentFigureIsActive) {
            updateFigure();
        } else  {
            model.staticBlocks.addAll(model.currentFigure);

            if (model.currentFigure.get(0).coords.getX() == FIGURE_START_X && model.currentFigure.get(0).coords.getY() == FIGURE_START_Y) {
                model.gameOver = true;
            }

            activatingNextFigure();
            currentFigureIsActive = true;

            //when a mino becomes inactive, check lines
            checkDelete();
        }
    }

    private void checkDelete() { //check to delete the line, if it filled
        int x = LEFT_X;
        int y = TOP_Y;
        int blockCount = 0;
        int linesCount = 0;

        while (x < RIGHT_X && y < BOTTOM_Y) {
            //count blocks in line
            for (int i = 0; i < model.staticBlocks.size(); i++) {
                if (model.staticBlocks.get(i).coords.getX() == x && model.staticBlocks.get(i).coords.getY() == y) {
                    blockCount++;
                }
            }
            x += Block.WIDTH;

            if (x == RIGHT_X) {

                // if line filled blocks
                if (blockCount == GamePanel.PLAY_WIDTH / Block.WIDTH) {
                    linesCount++;
                    for (int i = model.staticBlocks.size() - 1; i > -1; i--) {
                        if (model.staticBlocks.get(i).coords.getY() == y) {
                            model.staticBlocks.remove(i);
                        }
                    }

                    //move down other blocks
                    for (int i = 0; i < model.staticBlocks.size(); i++) {
                        if (model.staticBlocks.get(i).coords.getY() < y) {
                            model.staticBlocks.get(i).coords.setY(model.staticBlocks.get(i).coords.getY() + Block.HEIGHT);
                        }
                    }
                }
                x = LEFT_X;
                y += Block.HEIGHT;
                blockCount = 0;
            }

            if (linesCount > 0) {
                model.lines += linesCount;
                model.score += 100 * (long) Math.pow(3, linesCount);
                linesCount = 0;
            }
        }
    }

    private void updateFigure() {
        if (deactivating) {
            deactivatingDoing();
        }

        if (KeyHandler.upPressed) {

            updateXY();

            KeyHandler.upPressed = false;
        }

        checkMovementCollision();

        if (KeyHandler.downPressed) {
            if (!bottomCollision) {
                for (Block curBLock : model.currentFigure) {
                    curBLock.coords.setY(curBLock.coords.getY() + Block.HEIGHT);
                }
            }
            autoDropCounter = 0;
            KeyHandler.downPressed = false;
        }
        if (KeyHandler.leftPressed) {
            if (!leftCollision) {
                for (Block curBLock : model.currentFigure) {
                    curBLock.coords.setX(curBLock.coords.getX() - Block.WIDTH);
                }
            }

            KeyHandler.leftPressed = false;
        }
        if (KeyHandler.rightPressed) {

            if (!rightCollision) {
                for (Block curBLock : model.currentFigure) {
                    curBLock.coords.setX(curBLock.coords.getX() + Block.WIDTH);
                }
            }

            KeyHandler.rightPressed = false;
        }

        if (bottomCollision) {
            deactivating = true;
        } else {
            autoDropCounter++; //increases in every frame
            if (autoDropCounter == model.dropInterval) {
                for (Block curBLock : model.currentFigure) {
                    curBLock.coords.setY(curBLock.coords.getY() + Block.HEIGHT);
                }
                autoDropCounter = 0;
            }
        }
    }

    private void deactivatingDoing() {
        deactivateCounter++;

        //wait 20 frames
        if (deactivateCounter == 20) {
            deactivateCounter = 0;
            checkMovementCollision();

            if (bottomCollision) {
                currentFigureIsActive = false;
            }
        }
    }

    public final void checkMovementCollision() {
        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;

        checkContactStaticBlockCollision();

        //check left wall
        for (Block block : model.currentFigure) {
            if (block.coords.getX() == GamePanel.LEFT_X) {
                leftCollision = true;
                break;
            }
        }

        //check right wall
        for (Block block : model.currentFigure) {
            if (block.coords.getX() + Block.WIDTH == GamePanel.RIGHT_X) {
                rightCollision = true;
                break;
            }
        }

        //check bottom collision
        for (Block block : model.currentFigure) {
            if (block.coords.getY() + Block.HEIGHT == GamePanel.BOTTOM_Y) {
                bottomCollision = true;
                break;
            }
        }
    }

    public final void checkRotateCollision() {
        leftCollision = false;
        bottomCollision = false;
        rightCollision = false;

        checkContactStaticBlockCollision();

        rotateFigure();

        for (Block tempBlock : tempBlocks) {
            //check left wall
            if (tempBlock.coords.getX() < GamePanel.LEFT_X) {
                extremeCoordinateLeftX = GamePanel.LEFT_X;
                leftCollision = true;
                if (extremeCoordinateLeftX > tempBlock.coords.getX()) {
                    extremeCoordinateLeftX = tempBlock.coords.getX();
                }
            }

            //check right wall
            if (tempBlock.coords.getX() + Block.WIDTH > GamePanel.RIGHT_X) {
                extremeCoordinateRightX = GamePanel.RIGHT_X;
                rightCollision = true;
                if (extremeCoordinateRightX < tempBlock.coords.getX()) {
                    extremeCoordinateRightX = tempBlock.coords.getX();
                }
            }

            //check bottom collision
            if (tempBlock.coords.getY() + Block.HEIGHT > GamePanel.BOTTOM_Y) {
                bottomCollision = true;
            }
        }
    }

    public final void checkContactStaticBlockCollision() {
        for (int i = 0; i < model.staticBlocks.size(); i++) {
            int targetX = model.staticBlocks.get(i).coords.getX();
            int targetY = model.staticBlocks.get(i).coords.getY();

            //bottom collision
            for (Block block : model.currentFigure) {
                if (block.coords.getY() + Block.HEIGHT == targetY && block.coords.getX() == targetX) {
                    bottomCollision = true;
                    break;
                }
            }

            //right collision
            for (Block block : model.currentFigure) {
                if (block.coords.getY() == targetY && block.coords.getX() + Block.WIDTH == targetX) {
                    rightCollision = true;
                    break;
                }
            }

            //left collision
            for (Block block : model.currentFigure) {
                if (block.coords.getY() == targetY && block.coords.getX() - Block.WIDTH == targetX) {
                    leftCollision = true;
                    break;
                }
            }
        }
    }

    public void updateXY() { //check collision

        checkRotateCollision();

        if (bottomCollision) return;

        if (leftCollision) {

            while ((GamePanel.LEFT_X - extremeCoordinateLeftX) != 0) {
                extremeCoordinateLeftX += Block.WIDTH;
                for (int i = 0; i < tempBlocks.size(); i++) {
                    tempBlocks.get(i).coords.setX(tempBlocks.get(i).coords.getX() + Block.WIDTH);
                }
            }
        }

        if (rightCollision) {

            while ((GamePanel.RIGHT_X - (extremeCoordinateRightX + Block.WIDTH)) != 0) {
                extremeCoordinateRightX -= Block.WIDTH;
                for (int i = 0; i < tempBlocks.size(); i++) {
                    tempBlocks.get(i).coords.setX(tempBlocks.get(i).coords.getX() - Block.WIDTH);
                }
            }
        }

        model.currentFigure = tempBlocks;
    }

    private void setXY(int x, int y, List<Block> blocks) {
        for (Block block : blocks) {
            block.coords.setX(x + block.coords.getX() * Block.WIDTH);
            block.coords.setY(y + block.coords.getY() * Block.HEIGHT);
        }
    }

    private void activatingNextFigure() {
        model.currentFigure = model.nextFigure;
        model.nextFigure = new ArrayList<>();
        for (Block block : model.currentFigure) {
            block.coords.setX(FIGURE_START_X + (NEXT_FIGURE_X - block.coords.getX()));
            block.coords.setY(FIGURE_START_Y + (NEXT_FIGURE_Y - block.coords.getY()));
        }
        model.nextFigure = figureFactory.getNextFigure();
        setXY(NEXT_FIGURE_X, NEXT_FIGURE_Y, model.nextFigure);
    }

}
