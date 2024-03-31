package Model.Figures;

import Model.PairCoords;

import java.awt.*;

public class Block extends Rectangle {
    public PairCoords coords;
    public static final int SIZE = 30; //block size: 30x30 pixels
    public Color color;
    private final int margin = 2;
    public Block(Color c) {
        color = c;
    }

    public void draw (Graphics2D g2) {
        g2.setColor(color);
        g2.fillRect(coords.getX() + margin, coords.getY() + margin, Block.SIZE - (margin * 2), Block.SIZE - (margin * 2));
    }
}
