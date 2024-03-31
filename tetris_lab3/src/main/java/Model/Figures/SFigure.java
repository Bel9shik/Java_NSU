package Model.Figures;

import java.awt.*;

@FigureAnnotation(name="S")
public final class SFigure extends GeneralFigure {
    public SFigure() {
        create(Color.green, 4);
    }
    @Override
    public void setXY(int x, int y) {
        //   o o
        // o o

        //   2 3
        // 1 0

        mino.get(0).coords.setX(x);
        mino.get(0).coords.setY(y);
        mino.get(1).coords.setX(mino.get(0).coords.getX() - Block.SIZE);
        mino.get(1).coords.setY(mino.get(0).coords.getY());
        mino.get(2).coords.setX(mino.get(0).coords.getX());
        mino.get(2).coords.setY(mino.get(0).coords.getY() - Block.SIZE);
        mino.get(3).coords.setX(mino.get(0).coords.getX() + Block.SIZE);
        mino.get(3).coords.setY(mino.get(0).coords.getY() - Block.SIZE);
    }
}
