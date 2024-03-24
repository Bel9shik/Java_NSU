package Model;

import java.awt.*;

public class tmpFigure {
    PairCoords coords;
    Color color;

    public tmpFigure(PairCoords curCoords, Color curColor) {
        coords = curCoords;
        color = curColor;
    }
    public PairCoords getCoords() {
        return coords;
    }

    public void setCoords(PairCoords coords) {
        this.coords = coords;
    }
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
