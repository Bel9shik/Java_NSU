package Model.Figures;

import java.awt.*;
import java.util.ArrayList;

public enum Colors {
    //если допустим, другой чувак захочет создат свою фигуру со своим цветом, то наверное лучше enum поменять на класс ?
    CYAN (new Color(0, 255, 255)),
    BLUE (new Color(0, 0, 255)),
    ORANGE (new Color(255, 128, 0)),
    YELLOW (new Color(255, 255, 0)),
    GREEN (new Color(0, 255, 0)),
    PURPLE (new Color(155, 12, 155)),
    RED (new Color(255, 0, 0)),

    ;
    private Color myColor;
    Colors (Color myColor) {
        this.myColor = myColor;
    }

    public Color getColor() {
        return myColor;
    }
}
