package operations;

import main.Context;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@operation(
        name = "DEFINE"
)
public class Define implements Product {

    public static final Logger logger = Logger.getLogger(Define.class);

    @Override
    public int doOperations(ArrayList<Object> args) {
        Context context = (Context) args.get(0);
        String argument = (String) args.get(1);
        Double value = Double.parseDouble((String) args.get(2));
        context.getDefines().put(argument, value);

        return 0;
    }

    @Override
    public boolean checkArguments(List<Object> args) {
        if (args.size() != 2) return false;
        String letter = (String) args.get(0);
        String digit = (String) args.get(1);
        if (!letter.matches("[a-zA-Z]+")) return false;
        try {
            Double.parseDouble(digit);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
