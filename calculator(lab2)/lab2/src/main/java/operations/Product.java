package operations;

import Exceptions.InvalidArgumentsException;
import Exceptions.StackException;

import java.util.*;

public interface Product {
    int doOperations(ArrayList<Object> args) throws StackException; //in args[0] = context

    boolean checkArguments(List<Object> args) throws InvalidArgumentsException;
}
