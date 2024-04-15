package Exceptions;

public class CheckNotEnoughElementsException extends InvalidCheckException {
    public CheckNotEnoughElementsException(String operation) {
        super(operation);
    }
}
