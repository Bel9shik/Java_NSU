package Exceptions;

public class StackParsingException extends StackException {
    public StackParsingException(String message) {
        super("error parsing " + message);
    }
}
