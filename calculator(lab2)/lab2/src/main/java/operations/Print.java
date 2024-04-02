package operations;


import Exceptions.InvalidArgumentsException;
import Exceptions.StackException;
import main.Context;
import org.apache.log4j.Logger;

import java.util.*;

@operation(
        name = "PRINT"
)
public class Print implements Product {

    public static final Logger logger = Logger.getLogger(Print.class);

    @Override
    public int doOperations(ArrayList<Object> args) throws StackException {
        Context context = (Context) args.get(0);
        double num;

        if (context.getStack().isEmpty()) throw new StackException("in \"PRINT\" command not enough elements on stack");

        num = context.getStack().pop();

        System.out.println(num);

        context.getStack().push(num);
        return 0;
    }

    @Override
    public boolean checkArguments(List<Object> args) throws InvalidArgumentsException {
        if (!((String)args.get(0)).isEmpty()) throw new InvalidArgumentsException("arguments in command \"PRINT\" can be empty");
        return true;
    }
}
