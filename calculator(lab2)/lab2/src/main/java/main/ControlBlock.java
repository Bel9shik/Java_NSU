package main;

import Exceptions.InvalidCheckException;
import Exceptions.StackException;
import operations.Product;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class ControlBlock {

    public static final Logger logger = Logger.getLogger(ControlBlock.class);
    Context context = new Context();

    public void start(String[] args) {

        OperationFactory operationFactory = new OperationFactory();

        BufferedReader reader;

        boolean flagAboutConsoleInput = false;
        //create factory
        //initialize reader and parser
        if (args.length == 0) {
            reader = new BufferedReader(new InputStreamReader(System.in)); //итеративное считывание
            flagAboutConsoleInput = true;
        } else {
            try {
                reader = new BufferedReader(new FileReader(args[0]));
            } catch (FileNotFoundException e) {
                logger.error(e.getMessage(), e);
                return;
            }
        }

        String delimiter = " ";

        String line;

        try {

            //calculating
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                ArrayList<Object> parameters = new ArrayList<>();
                parameters.add(context);
                Product operation;
                try {
                    if ((operation = operationFactory.getOperation(line.split(delimiter)[0])) == null) {
                        logger.warn("Command not found");
                        continue;
                    }
                } catch (IllegalArgumentException e) {
                    logger.warn("Command not found");
                    continue;
                }
                String opArgs = "";

                try {
                    if(!line.contains(delimiter)) {
                        operation.checkArguments(Arrays.asList(opArgs.split(delimiter)));
                    }

                    else {
                        opArgs = line.substring(line.indexOf(delimiter) + 1);
                        operation.checkArguments(Arrays.asList(opArgs.split(delimiter)));
                    }

                    parameters.addAll(Arrays.asList(opArgs.split(delimiter)));
                    operation.doOperations(parameters);

                } catch (InvalidCheckException | StackException e) {
                    logger.warn(e.getMessage(), e);
                }

            }
        } catch (IOException | IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        }

        if (!flagAboutConsoleInput) {
            try {
                reader.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }

    }
}
