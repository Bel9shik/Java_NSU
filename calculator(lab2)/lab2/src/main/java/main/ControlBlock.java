package main;

import operations.Product;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class ControlBlock {

    public static final Logger logger = Logger.getLogger(ControlBlock.class);
    Parser parser;
    OperationFactory opFactory = new OperationFactory();

    public void startGame(String[] args)  {
        BufferedReader reader;
        //initialize reader and parser
        if (args.length == 0) {
            reader = new BufferedReader(new InputStreamReader(System.in));
            parser = new CmdParser();
            System.out.println("Work with console");
        } else {
            try {
                reader = new BufferedReader(new FileReader(args[0]));
            } catch (FileNotFoundException e) {
                logger.error(e.getMessage(), e);
                System.out.println("File not found");
                return;
            }
            parser = new FileParser();
            System.out.println("Work with file");
        }


        HashMap<String, Double> defines = new HashMap<>();

        ArrayList<String> commands = new ArrayList<>();

        Stack<Double> stack = new Stack<>();

        try {
            parser.loadCommands(commands, reader);
        } catch (IOException exception) {
            System.out.println("Reader stream is closed");
            logger.error(exception.getMessage(), exception);
            return;
        }

        for (String command : commands) {
            Product operator;
            try {
                operator = opFactory.getOperation(command.split(" ")[0]);
            } catch (IllegalAccessException | InstantiationException e) {
                System.out.println(e);
                logger.error(e.getMessage(), e);
                return;
            }
            if (operator.doOperations(defines, stack, command) == -1) {
                break;
            }
        }

    }
}


