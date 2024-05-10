package serialization.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ParseConfigFile {
    Properties properties;
    public ParseConfigFile(String configFile) {
        properties = new Properties();
        try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
            properties.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    public boolean getLogStatus() {
        return Boolean.parseBoolean(properties.getProperty("LogStatus"));
    }
}
