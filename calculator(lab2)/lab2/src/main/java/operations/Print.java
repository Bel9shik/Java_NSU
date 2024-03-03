package operations;


import org.apache.log4j.Logger;

import java.util.EmptyStackException;
import java.util.Map;
import java.util.Stack;

@operation(
        name = "PRINT"
)
public class Print implements Product {

    public static final Logger logger = Logger.getLogger(Print.class);

    @Override
    public int doOperations(Map<String, Double> defines, Stack<Double> stack, String action) {
        Double num;
        try {
            num = stack.peek();
        } catch (EmptyStackException ex) {
            logger.error(ex.getMessage(), ex);
            System.out.println("Error with quantity elements in stack");
            return -1;
        }
        System.out.println(num);
        return 0;
    }
}
