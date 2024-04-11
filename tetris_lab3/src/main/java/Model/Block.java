package Model;

import java.awt.*;

public final class Block extends Rectangle {
    public PairCoords coords;
    public static final int WIDTH = 30; //block size: 30x30 pixels view
    public static final int HEIGHT = 30; //block size: 30x30 pixels view
    public Color color;
    public final int margin = 2;
    public Block(Color c, PairCoords coords) {
        color = c;
        this.coords = coords;
    }

    @Override
    public String toString() { //have been used in debugging
        return "coords: (" + coords.getX() + ";" + coords.getY() + "), color: " + color;
    }
}
