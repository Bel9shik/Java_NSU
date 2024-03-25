package operations;

import main.Context;
import org.apache.log4j.Logger;

import java.util.*;

@operation(
        name = "SQRT"
)
public class Sqrt implements Product {
    public static final Logger logger = Logger.getLogger(Sqrt.class);

    @Override
    public int doOperations(ArrayList<Object> args) {
        Context context = (Context) args.get(0);
        if (context.getStack().isEmpty()) return -1;
        double num = context.getStack().pop();

        context.getStack().push(Math.sqrt(num));
        return 0;
    }

    @Override
    public boolean checkArguments(List<Object> args) {
       return ((String)args.get(0)).isEmpty();
    }
}
