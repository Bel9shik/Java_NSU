package Model.Figures;

import Model.PairCoords;

import java.awt.*;

import static java.lang.Math.*;

@FigureAnnotation(name="I")
public final class IFigure extends GeneralFigure {
    public IFigure() {
        coordsFigure.add(new PairCoords(4, 20));
        coordsFigure.add(new PairCoords(5, 20));
        coordsFigure.add(new PairCoords(6, 20));
        coordsFigure.add(new PairCoords(7, 20));
        referencePoint= new PairCoords(5, 20);
        corner = atan(1.0 / 4);
        color = Color.CYAN;
    }
}
