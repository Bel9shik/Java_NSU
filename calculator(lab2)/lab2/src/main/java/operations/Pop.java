package operations;

import org.apache.log4j.Logger;

import java.util.EmptyStackException;
import java.util.Map;
import java.util.Stack;

@operation(
        name = "POP"
)
public class Pop implements Product {

    public static final Logger logger = Logger.getLogger(Pop.class);

    @Override
    public int doOperations(Map<String, Double> defines, Stack<Double> stack, String action) {

        if (stack.isEmpty()) {
            logger.info("Not enough elements in stack");
            return -1;
        } else stack.pop();

        return 0;
    }
}
