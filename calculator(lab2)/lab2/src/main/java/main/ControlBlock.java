package main;

import operations.Product;
import org.apache.log4j.Logger;

import javax.management.OperationsException;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;

public class ControlBlock {

    public static final Logger logger = Logger.getLogger(ControlBlock.class);
    Parser parser;

    Calculate calculate = new Calculate();

    Context context = new Context();
    public void start(String[] args) {

        ArrayList<String> commands = new ArrayList<>();

        //create factory
        //initialize reader and parser
        if (args.length == 0) {
            logger.info("Work with console");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); //итеративное считывание
//            parser = new CmdParser();
//            parser.loadCommands(commands, reader);
            String line = "";
            try {
                while (!(line = reader.readLine()).isEmpty()) {
                    commands.add(line);
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
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

        OperationFactory operationFactory = new OperationFactory();

        calculate.calculating(commands, context, operationFactory);

    }
}



