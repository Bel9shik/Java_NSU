package operations;


import main.Context;
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
    public int doOperations(Context context, String action) {
        double num;

        if (context.getStack().isEmpty()) {
            logger.info("Not enough elements in stack");
            return -1;
        } else num = context.getStack().pop();

        System.out.println(num);
        return 0;
    }

    @Override
    public int doOperations(Context context) {
        return -1;
    }
}
