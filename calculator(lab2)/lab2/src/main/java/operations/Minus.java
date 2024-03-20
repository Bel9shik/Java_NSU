package operations;

import main.Context;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.EmptyStackException;
import java.util.Map;
import java.util.Stack;

@operation(
        name = "MINUS"
)
public class Minus implements Product {

    public static final Logger logger = Logger.getLogger(Minus.class);

    @Override
    public int doOperations(Context context, String action) {
        double num1;
        double num2;

        if (context.getStack().isEmpty()) {
            logger.info("Not enough elements in stack");
            return -1;
        } else num1 = context.getStack().pop();

        if (context.getStack().isEmpty()) {
            logger.info("Not enough elements in stack");
            context.getStack().push(num1);
            return -1;
        } else num2 = context.getStack().pop();

        context.getStack().push(num1 - num2);
        return 0;
    }

    @Override
    public int doOperations(Context context) {
        return -1;
    }
}
