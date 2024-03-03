package main;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class CmdParser implements Parser {

    public static final Logger logger = Logger.getLogger(CmdParser.class);
    @Override
    public void loadCommands(ArrayList<String> list, BufferedReader reader) throws IOException {
        String line = "";
        try {
            while (!(line = reader.readLine()).isEmpty()) {
                list.add(line);
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            System.out.println();
        }
        reader.close();
    }
}
