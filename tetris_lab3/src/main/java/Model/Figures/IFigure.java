package Model.Figures;

import java.awt.*;

@FigureAnnotation(name = "I")
public final class IFigure extends GeneralFigure {

    public IFigure() {
        create(Color.CYAN, 4);
    }

    @Override
    public void setXY(int x, int y) {
        // o o o o

        // mino[1] mino[0] mino[2] mino[3]

        mino.get(0).coords.setX(x);
        mino.get(0).coords.setY(y);
        mino.get(1).coords.setX(mino.get(0).coords.getX() - Block.SIZE);
        mino.get(1).coords.setY(mino.get(0).coords.getY());
        mino.get(2).coords.setX(mino.get(0).coords.getX() + Block.SIZE);
        mino.get(2).coords.setY(mino.get(0).coords.getY());
        mino.get(3).coords.setX(mino.get(0).coords.getX() + 2 * Block.SIZE);
        mino.get(3).coords.setY(mino.get(0).coords.getY());
    }

}
