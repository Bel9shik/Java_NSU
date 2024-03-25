package main;

import java.io.BufferedReader;
import java.util.ArrayList;

public interface Parser {
    void loadCommands(ArrayList<String> list, BufferedReader reader);
}
