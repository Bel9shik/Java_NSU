import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class ConfigFileParser {

    Properties properties;

    public ConfigFileParser(String configFile) {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(configFile)) {
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getBodiesCapacity() {
        return Integer.parseInt(properties.getProperty("MaxCapacityBodies"));
    }

    public int getEnginesCapacity() {
        return Integer.parseInt(properties.getProperty("MaxCapacityEngines"));
    }

    public int getAccessoriesCapacity() {
        return Integer.parseInt(properties.getProperty("MaxCapacityAccessories"));
    }

    public int getCarsCapacity() {
        return Integer.parseInt(properties.getProperty("MaxCapacityCars"));
    }

    public int getAccessorySuppliers() {
        return Integer.parseInt(properties.getProperty("AccessorySuppliers"));
    }

    public int getBodySuppliers() {
        return Integer.parseInt(properties.getProperty("BodySuppliers"));
    }

    public int getEngineSuppliers() {
        return Integer.parseInt(properties.getProperty("EngineSuppliers"));
    }


    public int getWorkers() {
        return Integer.parseInt(properties.getProperty("Workers"));
    }

    public int getDealers() {
        return Integer.parseInt(properties.getProperty("Dealers"));
    }

    public boolean getLogStatus() {
        return Boolean.parseBoolean(properties.getProperty("LogStatus"));
    }
}
