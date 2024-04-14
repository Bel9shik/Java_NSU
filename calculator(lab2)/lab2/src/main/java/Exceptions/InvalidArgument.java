package Exceptions;

public class InvalidArgument extends InvalidCheckException {
    public InvalidArgument(String name) {
        super("argument " + name + " is invalid");
    }
}
