package operations;

import java.util.Map;
import java.util.Stack;

public interface Product {
    int doOperations(Map<String, Double> defines, Stack<Double> stack, String action);
}
