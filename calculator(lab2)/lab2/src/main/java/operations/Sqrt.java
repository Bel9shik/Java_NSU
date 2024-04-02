package operations;

import Exceptions.InvalidArgumentsException;
import Exceptions.StackException;
import main.Context;
import org.apache.log4j.Logger;

import java.util.*;

@operation(
        name = "SQRT"
)
public class Sqrt implements Product {
    public static final Logger logger = Logger.getLogger(Sqrt.class);

    @Override
    public int doOperations(ArrayList<Object> args) throws StackException {
        Context context = (Context) args.get(0);

        if (context.getStack().isEmpty()) throw new StackException("in \"SQRT\" command not enough elements on stack");

        double num = context.getStack().pop();

        context.getStack().push(Math.sqrt(num));
        return 0;
    }

    @Override
    public boolean checkArguments(List<Object> args) throws InvalidArgumentsException {
        if (!((String)args.get(0)).isEmpty()) throw new InvalidArgumentsException("arguments in command \"SQRT\" can be empty");
        return true;
    }
}
