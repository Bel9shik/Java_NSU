package Model.Figures;

import Controller.KeyHandler;
import Model.PairCoords;
import Model.PlayManager;

import java.awt.*;
import java.util.ArrayList;

abstract public class GeneralFigure {
    public ArrayList<Block> mino = new ArrayList<>();
    public ArrayList<Block> tempBlocks = new ArrayList<>(); // for check collision
    int autoDropCounter = 0;
    boolean leftCollision, rightCollision, bottomCollision;
    public boolean isActive = true;
    boolean deactivating;
    int deactivateCounter = 0;


    public final void create(Color c, int totalBlocks) {
        for (int i = 0; i < totalBlocks; ++i) {
            mino.add(new Block(c));
            mino.get(i).coords = new PairCoords(0, 0);
            tempBlocks.add(new Block(c));
            tempBlocks.get(i).coords = new PairCoords(0, 0);
        }
    }

    abstract public void setXY(int x, int y);

    public void rotate() { //white new coords in tempBlocks
        //rotate
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

        if (leftCollision || rightCollision || bottomCollision) return;

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

    public final void draw(Graphics2D g2) {
        g2.setColor(mino.get(0).color);
        for (Block block : mino) {
            block.draw(g2);
        }
    }

    public final void checkMovementCollision() {
        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;

        checkContactStaticBlockCollision();

        //check left wall
        for (int i = 0; i < mino.size(); i++) {
            if (mino.get(i).coords.getX() == PlayManager.left_x) {
                leftCollision = true;
            }
        }

        //check right wall
        for (int i = 0; i < mino.size(); i++) {
            if (mino.get(i).coords.getX() + Block.SIZE == PlayManager.right_x) {
                rightCollision = true;
            }
        }

        //check bottom collision
        for (int i = 0; i < mino.size(); i++) {
            if (mino.get(i).coords.getY() + Block.SIZE == PlayManager.bottom_y) {
                bottomCollision = true;
            }
        }
    }

    public final void checkRotateCollision() {
        leftCollision = false;
        bottomCollision = false;
        rightCollision = false;

        checkContactStaticBlockCollision();

        rotate();

        //check left wall
        for (int i = 0; i < tempBlocks.size(); i++) {
            if (tempBlocks.get(i).coords.getX() <= PlayManager.left_x) {
                leftCollision = true;
            }
        }

        //check right wall
        for (int i = 0; i < tempBlocks.size(); i++) {
            if (tempBlocks.get(i).coords.getX() + Block.SIZE >= PlayManager.right_x) {
                rightCollision = true;
            }
        }

        //check bottom collision
        for (int i = 0; i < tempBlocks.size(); i++) {
            if (tempBlocks.get(i).coords.getY() + Block.SIZE >= PlayManager.bottom_y) {
                bottomCollision = true;
            }
        }

    }

    public final void checkContactStaticBlockCollision() {
        for (int i = 0; i < PlayManager.staticBlocks.size(); i++) {
            int targetX = PlayManager.staticBlocks.get(i).coords.getX();
            int targetY = PlayManager.staticBlocks.get(i).coords.getY();

            //bottom collision
            for (int j = 0; j < mino.size(); j++) {
                if (mino.get(j).coords.getY() + Block.SIZE == targetY && mino.get(j).coords.getX() == targetX) {
                    bottomCollision = true;
                    break;
                }
            }

            //right collision
            for (int j = 0; j < mino.size(); j++) {
                if (mino.get(j).coords.getY() == targetY && mino.get(j).coords.getX() + Block.SIZE == targetX) {
                    rightCollision = true;
                    break;
                }
            }

            //left collision
            for (int j = 0; j < mino.size(); j++) {
                if (mino.get(j).coords.getY() == targetY && mino.get(j).coords.getX() - Block.SIZE == targetX) {
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
