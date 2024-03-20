package operations;

import main.Context;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Stack;

@operation(
        name = "DEFINE"
)
public class Define implements Product {

    public static final Logger logger = Logger.getLogger(Define.class);

    @Override
    public int doOperations(Context context, String action) {
        String argument = action.split(" ")[1];
        Double value;
        try {
            value = Double.parseDouble(action.split(" ")[2]);
        } catch (NumberFormatException ex) {
            logger.error(ex.getMessage(), ex);
            System.out.println("Invalid arguments in define command");
            return -1;
        }
        context.getDefines().put(argument, value);

        return 0;
    }

    @Override
    public int doOperations(Context context) {
        return -1;
    }
}
