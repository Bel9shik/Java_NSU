package main;

import operations.Product;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;

public class ControlBlock {

    public static final Logger logger = Logger.getLogger(ControlBlock.class);
    Parser parser;
    public void startGame(String[] args) {

        ArrayList<String> commands = new ArrayList<>();

        //initialize reader and parser
        if (args.length == 0) {
            logger.info("Work with console");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            parser = new CmdParser();
            parser.loadCommands(commands, reader);
            try {
                reader.close();
            } catch (IOException ex) {
                logger.error(ex.getMessage(), ex);
                return;
            }
        } else {
            try {
                logger.info("Work with file " + args[0]);
                parser = new FileParser();
                BufferedReader reader = new BufferedReader(new FileReader(args[0]));
                parser.loadCommands(commands, reader);
                try {
                    reader.close();
                } catch (IOException ex) {
                    logger.error(ex.getMessage(), ex);
                    return;
                }
            } catch (FileNotFoundException exception) {
                logger.error(exception.getMessage(), exception);
                return;
            }
        }


        for (String command : commands) {
            Product operator;
            try {
                operator = Context.opFactory.getOperation(command.split(" ")[0]);
            } catch (IllegalAccessException | InstantiationException e) {
                logger.error(e.getMessage(), e);
                return;
            }
            if (operator.doOperations(Context.defines, Context.stack, command) == -1) {
                break;
            }
        }
    }
}



