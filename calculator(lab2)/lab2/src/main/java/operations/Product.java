package operations;

import java.util.*;

public interface Product {
    int doOperations(ArrayList<Object> args); //in args[0] = context

    boolean checkArguments(List<Object> args);
}
