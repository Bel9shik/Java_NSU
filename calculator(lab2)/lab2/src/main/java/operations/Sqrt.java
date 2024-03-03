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
        Double value;
        try {
            value = stack.pop();
        } catch (EmptyStackException ex) {
            logger.error(ex.getMessage(), ex);
            System.out.println("Error with quantity elements in stack");
            return -1;
        }
        stack.push(Math.sqrt(value));
        return 0;
    }
}
