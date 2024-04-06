package Model.Figures;

import Model.PairCoords;

import java.awt.*;

public class Block extends Rectangle {
    public PairCoords coords;
    public static final int SIZE = 30; //block size: 30x30 pixels
    public Color color;
    public final int margin = 2;
    public Block(Color c) {
        color = c;
    }
}
