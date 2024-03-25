package operations;

import main.Context;
import org.apache.log4j.Logger;

import java.util.*;

@operation(
        name = "DIVISION"
)
public class Division implements Product {
    public static final Logger logger = Logger.getLogger(Division.class);

    @Override
    public int doOperations(ArrayList<Object> args) {
        Context context = (Context) args.get(0);
        if (context.getStack().size() < 2) return -1;
        double num1 = context.getStack().pop();
        double num2 = context.getStack().pop();

        context.getStack().push(num1 / num2);
        return 0;
    }

    @Override
    public boolean checkArguments(List<Object> args) {
        return ((String)args.get(0)).isEmpty();
    }
}
