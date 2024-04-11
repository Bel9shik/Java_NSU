package Model.Figures;

import java.awt.*;

@FigureAnnotation(name="T")
public final class TFigure extends GeneralFigure {
    public TFigure() {
        create(Color.magenta, 4);
    }
    @Override
    public void setXY(int x, int y) {
        //   o
        // o o o

        //   3
        // 1 0 2

        mino.get(0).coords.setX(x);
        mino.get(0).coords.setY(y);
        mino.get(1).coords.setX(mino.get(0).coords.getX() - Block.SIZE);
        mino.get(1).coords.setY(mino.get(0).coords.getY());
        mino.get(2).coords.setX(mino.get(0).coords.getX() + Block.SIZE);
        mino.get(2).coords.setY(mino.get(0).coords.getY());
        mino.get(3).coords.setX(mino.get(0).coords.getX());
        mino.get(3).coords.setY(mino.get(0).coords.getY() - Block.SIZE);
    }

}
