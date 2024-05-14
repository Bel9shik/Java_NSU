package Model;

import Controller.Sound;

import java.util.ArrayList;
import java.util.List;

public final class ModelController {
    public List<Block> currentFigure;
    public List<Block> nextFigure;
    public boolean gameOver = false;
    public long score = 0;
    public long lines = 0;
    public ArrayList<Block> staticBlocks = new ArrayList<>();
    public int dropInterval = 60; //mino drops in every 60 frames
}
