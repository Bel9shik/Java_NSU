package main;

import operations.Product;
import org.apache.log4j.Logger;

import java.util.ArrayList;

public class Calculate {

    public static final Logger logger = Logger.getLogger(Calculate.class);
    boolean calculating (ArrayList<String> commands, Context context, OperationFactory operationFactory) {
        for (String command : commands) { //убрать в отдельный класс?
            Product operator;
            try {
                operator = operationFactory.getOperation(command.split(" ")[0]);
            } catch (IllegalAccessException e) {
                logger.error(e.getMessage(), e);
                return false;
            }
            if (operator.doOperations(context, command) == -1 && operator.doOperations(context) == -1) {
                return false;
            }
        }
        return true;
    }
}
