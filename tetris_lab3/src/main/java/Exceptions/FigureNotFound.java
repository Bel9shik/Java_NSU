package Exceptions;

public class FigureNotFound extends TetrisException{

    public FigureNotFound() {
        super("Figure not found");
    }
}
