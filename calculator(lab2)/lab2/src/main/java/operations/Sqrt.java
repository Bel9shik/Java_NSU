package operations;

import Exceptions.CheckNotEnoughElementsException;
import Exceptions.InvalidCheckException;
import Exceptions.StackException;
import Exceptions.StackNotEnoughElements;
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

        if (context.getStack().isEmpty()) throw new StackNotEnoughElements("SQRT");

        double num = context.getStack().pop();

        context.getStack().push(Math.sqrt(num));
        return 0;
    }

    @Override
    public boolean checkArguments(List<Object> args) throws InvalidCheckException {
        if (!((String)args.get(0)).isEmpty()) throw new CheckNotEnoughElementsException("there should be no arguments in the \"SQRT\" command");
        return true;
    }
}
