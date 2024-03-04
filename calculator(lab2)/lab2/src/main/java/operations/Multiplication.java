package operations;

import org.apache.log4j.Logger;

import java.util.EmptyStackException;
import java.util.Map;
import java.util.Stack;

@operation(
        name = "MULTIPLICATION"
)
public class Multiplication implements Product {

    public static final Logger logger = Logger.getLogger(Multiplication.class);

    @Override
    public int doOperations(Map<String, Double> defines, Stack<Double> stack, String action) {
        double num1;
        double num2;

        if (stack.isEmpty()) {
            logger.info("Not enough elements in stack");
            return -1;
        } else num1 = stack.pop();

        if (stack.isEmpty()) {
            logger.info("Not enough elements in stack");
            stack.push(num1);
            return -1;
        } else num2 = stack.pop();

        stack.push(num1 * num2);
        return 0;
    }
}
