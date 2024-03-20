package operations;

import main.Context;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Stack;

@operation(
        name = "PUSH"
)
public class Push implements Product {
    public static final Logger logger = Logger.getLogger(Push.class);

    @Override
    public int doOperations(Context context, String action) {
        String argument = action.split(" ")[1];

        for (Map.Entry<String, Double> entry : context.getDefines().entrySet()) {
            if (entry.getKey().equals(argument)) {
                context.getStack().push(entry.getValue());
                return 0;
            }
        }

        argument = action.split(" ")[1];
        Double value;
        try {
            value = Double.parseDouble(argument);
        } catch (NumberFormatException ex) {
            logger.error(ex.getMessage(), ex);
            return -1;
        }
        context.getStack().push(value);

        return 0;
    }

    @Override
    public int doOperations(Context context) {
        return -1;
    }
}


