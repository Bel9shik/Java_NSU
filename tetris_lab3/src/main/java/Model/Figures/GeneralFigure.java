package Model.Figures;

import Controller.KeyHandler;
import Model.PairCoords;
import Model.PlayManager;
import View.GamePanel;

import java.awt.*;
import java.util.ArrayList;

abstract public class GeneralFigure {
    public ArrayList<Block> mino = new ArrayList<>();
    public ArrayList<Block> tempBlocks = new ArrayList<>(); // for check collision controller
    int autoDropCounter = 0;
    boolean leftCollision, rightCollision, bottomCollision;
    public boolean isActive = true;
    boolean deactivating;
    int deactivateCounter = 0;

    private int extremeCoordinateLeftX;
    private int extremeCoordinateRightX;


    public final void create(Color c, int totalBlocks) {
        for (int i = 0; i < totalBlocks; ++i) {
            mino.add(new Block(c));
            mino.get(i).coords = new PairCoords(0, 0);
            tempBlocks.add(new Block(c));
            tempBlocks.get(i).coords = new PairCoords(0, 0);
        }
    }

    abstract public void setXY(int x, int y);

    public void rotate() { //white new coords in tempBlocks controller
        //rotate
        tempBlocks = mino;
        int centerX = mino.get(0).coords.getX();
        int centerY = mino.get(0).coords.getY();
        for (int i = 0; i < mino.size(); i++) {
            int deltaX = mino.get(i).coords.getX() - centerX;
            int deltaY = mino.get(i).coords.getY() - centerY;
            tempBlocks.get(i).coords.setX(centerX - deltaY);
            tempBlocks.get(i).coords.setY(centerY + deltaX);
        }
    }

    public void updateXY() { //check collision

        checkRotateCollision();

        if (bottomCollision) return;

        if (leftCollision) {

            while ((GamePanel.LEFT_X - extremeCoordinateLeftX) != 0) {
                extremeCoordinateLeftX += Block.SIZE;
                for (int i = 0; i < tempBlocks.size(); i++) {
                    tempBlocks.get(i).coords.setX(tempBlocks.get(i).coords.getX() + Block.SIZE);
                }
            }
        }

        if (rightCollision) {

            while ((GamePanel.RIGHT_X - (extremeCoordinateRightX + Block.SIZE)) != 0) {
                extremeCoordinateRightX -= Block.SIZE;
                for (int i = 0; i < tempBlocks.size(); i++) {
                    tempBlocks.get(i).coords.setX(tempBlocks.get(i).coords.getX() - Block.SIZE);
                }
            }
        }

        mino = tempBlocks;
    }

    public void update() {

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
                for (Block curBLock : mino) {
                    curBLock.coords.setY(curBLock.coords.getY() + Block.SIZE);
                }
            }
            autoDropCounter = 0;
            KeyHandler.downPressed = false;
        }
        if (KeyHandler.leftPressed) {
            if (!leftCollision) {
                for (Block curBLock : mino) {
                    curBLock.coords.setX(curBLock.coords.getX() - Block.SIZE);
                }
            }

            KeyHandler.leftPressed = false;
        }
        if (KeyHandler.rightPressed) {

            if (!rightCollision) {
                for (Block curBLock : mino) {
                    curBLock.coords.setX(curBLock.coords.getX() + Block.SIZE);
                }
            }

            KeyHandler.rightPressed = false;
        }

        if (bottomCollision) {
            deactivating = true;
        } else {
            autoDropCounter++; //increases in every frame
            if (autoDropCounter == PlayManager.dropInterval) {
                for (Block curBLock : mino) {
                    curBLock.coords.setY(curBLock.coords.getY() + Block.SIZE);
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
                isActive = false;
            }
        }
    }

    public final void checkMovementCollision() {
        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;

        checkContactStaticBlockCollision();

        //check left wall
        for (Block block : mino) {
            if (block.coords.getX() == GamePanel.LEFT_X) {
                leftCollision = true;
                break;
            }
        }

        //check right wall
        for (Block block : mino) {
            if (block.coords.getX() + Block.SIZE == GamePanel.RIGHT_X) {
                rightCollision = true;
                break;
            }
        }

        //check bottom collision
        for (Block block : mino) {
            if (block.coords.getY() + Block.SIZE == GamePanel.BOTTOM_Y) {
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

        rotate();

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
            if (tempBlock.coords.getX() + Block.SIZE > GamePanel.RIGHT_X) {
                extremeCoordinateRightX = GamePanel.RIGHT_X;
                rightCollision = true;
                if (extremeCoordinateRightX < tempBlock.coords.getX()) {
                    extremeCoordinateRightX = tempBlock.coords.getX();
                }
            }

            //check bottom collision
            if (tempBlock.coords.getY() + Block.SIZE > GamePanel.BOTTOM_Y) {
                bottomCollision = true;
            }
        }
    }

    public final void checkContactStaticBlockCollision() {
        for (int i = 0; i < PlayManager.staticBlocks.size(); i++) {
            int targetX = PlayManager.staticBlocks.get(i).coords.getX();
            int targetY = PlayManager.staticBlocks.get(i).coords.getY();

            //bottom collision
            for (Block block : mino) {
                if (block.coords.getY() + Block.SIZE == targetY && block.coords.getX() == targetX) {
                    bottomCollision = true;
                    break;
                }
            }

            //right collision
            for (Block block : mino) {
                if (block.coords.getY() == targetY && block.coords.getX() + Block.SIZE == targetX) {
                    rightCollision = true;
                    break;
                }
            }

            //left collision
            for (Block block : mino) {
                if (block.coords.getY() == targetY && block.coords.getX() - Block.SIZE == targetX) {
                    leftCollision = true;
                    break;
                }
            }
        }
    }


//    final void rotate() {
//        for (PairCoords curCoords : coordsFigure) {
//            curCoords = new PairCoords(referencePoint.getX() + (int) Math.round((referencePoint.getX() - curCoords.getX()) * Math.cos(90 - corner) - (referencePoint.getY() - curCoords.getY()) * Math.sin(90 - corner)),
//                    referencePoint.getY() + (int) Math.round((referencePoint.getX() - curCoords.getX()) * Math.sin(90 - corner) + (referencePoint.getY() - curCoords.getY()) * Math.cos(90 - corner)));
//
//        }
//    }
}
