package main;

import java.util.HashMap;
import java.util.Stack;

public class Context {
    private HashMap<String, Double> defines = new HashMap<>();
    private Stack<Double> stack = new Stack<>();

    public HashMap<String, Double> getDefines() {
        return defines;
    }

    public Stack<Double> getStack() {
        return stack;
    }
}
