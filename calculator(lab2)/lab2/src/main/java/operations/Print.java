package operations;


import Exceptions.CheckNotEnoughElementsException;
import Exceptions.InvalidCheckException;
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
    public int doOperations(ArrayList<Object> args) {
        Context context = (Context) args.get(0);
        double num;

        if (context.getStack().isEmpty()) {
            System.out.println("stack is empty");
            return 0;
        }

        num = context.getStack().pop();

        System.out.println(num);

        context.getStack().push(num);
        return 0;
    }

    @Override
    public boolean checkArguments(List<Object> args) throws InvalidCheckException {
        if (!((String)args.get(0)).isEmpty()) throw new CheckNotEnoughElementsException("there should be no arguments in the \"PRINT\" command");
        return true;
    }
}
