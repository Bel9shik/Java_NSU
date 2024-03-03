import operations.*;
import main.*;
import org.junit.jupiter.api.Test;
import org.apache.log4j.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

public class TestCalculator {

    OperationFactory opFactory = new OperationFactory();

    HashMap<String, Double> defines = new HashMap<>();

    Stack<Double> stack = new Stack<>();

    Product operator;

    String action;

    @Test
    void defineTest() {
        action = "DEFINE A 10";

        try {
            operator = opFactory.getOperation("DEFINE");
        } catch (IllegalAccessException | InstantiationException e) {
            fail();
        }

        operator.doOperations(defines, stack, action);

        if (defines.get("A") == 10d) {
            assertTrue(true);
        }
    }

    @Test
    void pushTest() {
        action = "PUSH A";
        defines.put("A", 10d);

        try {
            operator = opFactory.getOperation("PUSH");
        } catch (IllegalAccessException | InstantiationException e) {
            fail();
        }

        operator.doOperations(defines, stack, action);

        if (stack.size() == 1 && stack.pop() == 10.0) {
            assertTrue(true);
        }
    }

    @Test
    void popTest() {
        action = "POP";
        stack.push(10d);

        try {
            operator = opFactory.getOperation("POP");
        } catch (IllegalAccessException | InstantiationException e) {
            fail();
        }

        operator.doOperations(defines, stack, action);

        if (stack.isEmpty()) {
            assertTrue(true);
        }
    }

    @Test
    void divisionTest() {
        action = "DIVISION";
        stack.push(6d);
        stack.push(2d);

        try {
            operator = opFactory.getOperation("DIVISION");
        } catch (IllegalAccessException | InstantiationException e) {
            fail();
        }

        operator.doOperations(defines, stack, action);

        if (stack.size() == 1 && stack.pop() == 3d) {
            assertTrue(true);
        }
    }

    @Test
    void plusTest() {
        action = "PLUS";
        stack.push(6d);
        stack.push(2d);

        try {
            operator = opFactory.getOperation("PLUS");
        } catch (IllegalAccessException | InstantiationException e) {
            fail();
        }

        operator.doOperations(defines, stack, action);

        if (stack.size() == 1 && stack.pop() == 8d) {
            assertTrue(true);
        }
    }

    @Test
    void minusTest() {
        action = "MINUS";
        stack.push(6d);
        stack.push(2d);

        try {
            operator = opFactory.getOperation("MINUS");
        } catch (IllegalAccessException | InstantiationException e) {
            fail();
        }

        operator.doOperations(defines, stack, action);

        if (stack.size() == 1 && stack.pop() == 4d) {
            assertTrue(true);
        }
    }

    @Test
    void multiplicationTest() {
        action = "MULTIPLICATION";
        stack.push(6d);
        stack.push(2d);

        try {
            operator = opFactory.getOperation("MULTIPLICATION");
        } catch (IllegalAccessException | InstantiationException e) {
            fail();
        }

        operator.doOperations(defines, stack, action);

        if (stack.size() == 1 && stack.pop() == 12d) {
            assertTrue(true);
        }
    }

    @Test
    void SqrtTest() {
        action = "SQRT";
        stack.push(4d);

        try {
            operator = opFactory.getOperation("SQRT");
        } catch (IllegalAccessException | InstantiationException e) {
            fail();
        }

        operator.doOperations(defines, stack, action);

        if (stack.size() == 1 && stack.pop() == 2d) {
            assertTrue(true);
        }
    }

    @Test
    void fileParserTest() {

        BufferedReader reader;
        Parser parser = new FileParser();
//        File file = new File("testForCalculate");

        try {
            reader = new BufferedReader(new FileReader("testForCalculate"));
        } catch (FileNotFoundException e) {
//            System.out.println(file.getAbsolutePath());
//            System.out.println("test");
            fail();
            return;
        }

        ArrayList<String> commands = new ArrayList<>();

        try {
            parser.loadCommands(commands, reader);
        } catch (IOException e) {
            fail();
        }

        assertEquals("DEFINE A 4", commands.get(0));
        assertEquals("DEFINE B 5", commands.get(1));
        assertEquals("SQRT", commands.get(5));
        assertEquals("PRINT", commands.get(commands.size() - 1));

    }

}
