package main;

import java.util.HashMap;
import java.util.Stack;

public class Context {
    public static HashMap<String, Double> defines = new HashMap<>();

    public static Stack<Double> stack = new Stack<>();

    public static OperationFactory opFactory = new OperationFactory();

}
