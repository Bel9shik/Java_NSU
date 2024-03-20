package operations;

import main.Context;
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
    public int doOperations(Context context, String action) {

        if (context.getStack().isEmpty()) {
            logger.info("Not enough elements in stack");
            return -1;
        } else context.getStack().pop();

        return 0;
    }

    @Override
    public int doOperations(Context context) {
        return -1;
    }
}
