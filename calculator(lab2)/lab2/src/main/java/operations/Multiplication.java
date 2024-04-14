package operations;

import Exceptions.CheckNotEnoughElementsException;
import Exceptions.InvalidCheckException;
import Exceptions.StackException;
import Exceptions.StackNotEnoughElements;
import main.Context;
import org.apache.log4j.Logger;

import java.util.*;

@operation(
        name = "MULT"
)
public class Multiplication implements Product {

    public static final Logger logger = Logger.getLogger(Multiplication.class);

    @Override
    public int doOperations(ArrayList<Object> args) throws StackException {
        Context context = (Context) args.get(0);
        if (context.getStack().size() < 2) throw new StackNotEnoughElements("MULT");
        double num1 = context.getStack().pop();
        double num2 = context.getStack().pop();

        context.getStack().push(num1 * num2);
        return 0;
    }

    @Override
    public boolean checkArguments(List<Object> args) throws InvalidCheckException {
        if (!((String)args.get(0)).isEmpty()) throw new CheckNotEnoughElementsException("there should be no arguments in the \"MULT\" command");
        return true;
    }
}
