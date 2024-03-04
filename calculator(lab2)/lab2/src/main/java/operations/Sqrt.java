package operations;

import org.apache.log4j.Logger;

import java.util.EmptyStackException;
import java.util.Map;
import java.util.Stack;

@operation(
        name = "SQRT"
)
public class Sqrt implements Product {
    public static final Logger logger = Logger.getLogger(Sqrt.class);

    @Override
    public int doOperations(Map<String, Double> defines, Stack<Double> stack, String action) {
        double num;

        if (stack.isEmpty()) {
            logger.info("Not enough elements in stack");
            return -1;
        } else num = stack.pop();

        stack.push(Math.sqrt(num));
        return 0;
    }
}
