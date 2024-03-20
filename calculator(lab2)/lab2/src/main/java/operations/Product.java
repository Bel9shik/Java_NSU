package operations;

import main.Context;

import java.util.Map;
import java.util.Stack;

public interface Product {
    int doOperations(Context context, String action); //context
    int doOperations(Context context); //context
}
