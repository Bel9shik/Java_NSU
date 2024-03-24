package Model.Figures;

import Model.PairCoords;

import java.awt.*;
import java.util.ArrayList;

public class GeneralFigure {
    ArrayList<PairCoords> coordsFigure = new ArrayList<>();
    PairCoords referencePoint;
    double corner;

    Color color;
    final void rotate () {
        for (PairCoords curCoords : coordsFigure) {
            curCoords = new PairCoords(referencePoint.getX() + (int) Math.round((referencePoint.getX() - curCoords.getX()) * Math.cos(90 - corner) - (referencePoint.getY() - curCoords.getY()) * Math.sin(90 - corner)),
                    referencePoint.getY() + (int) Math.round((referencePoint.getX() - curCoords.getX()) * Math.sin(90 - corner) + (referencePoint.getY() - curCoords.getY()) * Math.cos(90 - corner)));

        }
    }
}
