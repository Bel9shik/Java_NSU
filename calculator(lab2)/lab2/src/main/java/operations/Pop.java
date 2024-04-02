package operations;

import Exceptions.InvalidArgumentsException;
import Exceptions.StackException;
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
            throw new StackException("stack is empty in \"POP\" command");
        } else context.getStack().pop();

        return 0;
    }

    @Override
    public boolean checkArguments(List<Object> args) throws InvalidArgumentsException {
        if (!((String)args.get(0)).isEmpty()) throw new InvalidArgumentsException("the command \"POP\" should not have arguments");
        return true;
    }
}
