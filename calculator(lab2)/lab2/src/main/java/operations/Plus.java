package operations;

import Exceptions.InvalidArgumentsException;
import Exceptions.StackException;
import main.Context;

import java.util.ArrayList;
import java.util.List;

@operation(
        name = "PLUS"
)
public class Plus implements Product {

    @Override
    public int doOperations(ArrayList<Object> args) throws StackException {
        Context context = (Context) args.get(0);
        if (context.getStack().size() < 2) throw new StackException("in \"PLUS\" command not enough elements on stack");

        double num1 = context.getStack().pop();
        double num2 = context.getStack().pop();

        context.getStack().push(num1 + num2);
        return 0;
    }

    @Override
    public boolean checkArguments(List<Object> args) throws InvalidArgumentsException {
        if (!((String)args.get(0)).isEmpty()) throw new InvalidArgumentsException("arguments in command \"PLUS\" can be empty");
        return true;
    }
}
