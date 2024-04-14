package operations;

import Exceptions.CheckNotEnoughElementsException;
import Exceptions.InvalidCheckException;
import Exceptions.StackException;
import Exceptions.StackNotEnoughElements;
import main.Context;
import org.apache.log4j.Logger;

import java.util.*;

@operation(
        name = "POP"
)
public class Pop implements Product {

    public static final Logger logger = Logger.getLogger(Pop.class);

    @Override
    public int doOperations(ArrayList<Object> args) throws StackException {
        Context context = (Context) args.get(0);
        if (context.getStack().isEmpty()) {
            throw new StackNotEnoughElements("POP");
        } else context.getStack().pop();

        return 0;
    }

    @Override
    public boolean checkArguments(List<Object> args) throws InvalidCheckException {
        if (!((String)args.get(0)).isEmpty()) throw new CheckNotEnoughElementsException("there should be no arguments in the \"POP\" command");
        return true;
    }
}
