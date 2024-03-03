package operations;

import org.apache.log4j.Logger;

import java.util.EmptyStackException;
import java.util.Map;
import java.util.Stack;

@operation(
        name = "DIVISION"
)
public class Division implements Product {
    public static final Logger logger = Logger.getLogger(Division.class);

    @Override
    public int doOperations(Map<String, Double> defines, Stack<Double> stack, String action) {
        double num1;
        double num2;
        try {
            num1 = stack.pop();
            num2 = stack.pop();
        } catch (EmptyStackException ex) {
            logger.error(ex.getMessage(), ex);
            System.out.println("Error with quantity elements in stack");
            return -1;
        }
        stack.push(num1 / num2);
        return 0;
    }
}
