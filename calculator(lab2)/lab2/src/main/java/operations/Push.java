package operations;

import Exceptions.InvalidArgumentsException;
import Exceptions.StackException;
import main.Context;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@operation(
        name = "PUSH"
)
public class Push implements Product  {
    public static final Logger logger = Logger.getLogger(Push.class);

    @Override
    public int doOperations(ArrayList<Object> args) throws StackException {
        Context context = (Context) args.get(0);
        String argument = (String) args.get(1);

        for (Map.Entry<String, Double> entry : context.getDefines().entrySet()) {
            if (entry.getKey().equals(argument)) {
                context.getStack().push(entry.getValue());
                return 0;
            }
        }
        Double value;
        try {
            value = Double.parseDouble(argument);
        } catch (NumberFormatException e) {
            throw new StackException("error parse in double in \"PUSH\" command");
        }
        context.getStack().push(value);

        return 0;
    }

    @Override
    public boolean checkArguments(List<Object> args) throws InvalidArgumentsException {
        if (((String)args.get(0)).isEmpty()) throw new InvalidArgumentsException("arguments in command \"PUSH\" can not be empty");
        return true;
    }
}


