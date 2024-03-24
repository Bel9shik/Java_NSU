package Controller.Figures;

import java.awt.*;

public enum Colors {
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
