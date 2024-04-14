package Exceptions;

public class StackNotEnoughElements extends StackException {
    public StackNotEnoughElements(String operation) {
        super("Not enough elements on stack in \"" + operation + "\"");
    }
}
