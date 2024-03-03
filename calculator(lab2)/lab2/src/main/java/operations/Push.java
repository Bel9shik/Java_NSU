package operations;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Stack;

@operation(
        name = "PUSH"
)
public class Push implements Product {
    public static final Logger logger = Logger.getLogger(Push.class);

    @Override
    public int doOperations(Map<String, Double> defines, Stack<Double> stack, String action) {
        String argument = action.split(" ")[1];

        for (Map.Entry<String, Double> entry : defines.entrySet()) {
            if (entry.getKey().equals(argument)) {
                stack.push(entry.getValue());
                return 0;
            }
        }

        argument = action.split(" ")[1];
        Double value;
        try {
            value = Double.parseDouble(argument);
        } catch (NumberFormatException ex) {
            logger.error(ex.getMessage(), ex);
            System.out.println("Invalid arguments in push command");
            return -1;
        }
        stack.push(value);

        return 0;
    }

}


