import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public final class ConfigFileParser {

    ArrayList<String> arguments;

    public ConfigFileParser(String configFile) {
        arguments = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                arguments.add(line);
            }
        } catch (IOException ignored) {
        }
    }

    public int getBodiesCapacity() {
        return arguments.stream()
                .filter(x -> x.contains("MaxCapacityBodies"))
                .map(x -> Integer.parseInt(x.replaceFirst("MaxCapacityBodies=", "")))
                .toList()
                .get(0);
    }

    public int getEnginesCapacity() {
        return arguments.stream()
                .filter(x -> x.contains("MaxCapacityEngines"))
                .map(x -> Integer.parseInt(x.replaceFirst("MaxCapacityEngines=", "")))
                .toList()
                .get(0);
    }

    public int getAccessoriesCapacity() {
        return arguments.stream()
                .filter(x -> x.contains("MaxCapacityAccessories"))
                .map(x -> Integer.parseInt(x.replaceFirst("MaxCapacityAccessories=", "")))
                .toList()
                .get(0);
    }

    public int getCarsCapacity() {
        return arguments.stream()
                .filter(x -> x.contains("MaxCapacityCars"))
                .map(x -> Integer.parseInt(x.replaceFirst("MaxCapacityCars=", "")))
                .toList()
                .get(0);
    }

    public int getAccessorySuppliers() {
        return arguments.stream()
                .filter(x -> x.contains("AccessorySuppliers"))
                .map(x -> Integer.parseInt(x.replaceFirst("AccessorySuppliers=", "")))
                .toList()
                .get(0);
    }

    public int getBodySuppliers() {
        return arguments.stream()
                .filter(x -> x.contains("BodySuppliers"))
                .map(x -> Integer.parseInt(x.replaceFirst("BodySuppliers=", "")))
                .toList()
                .get(0);
    }

    public int getEngineSuppliers() {
        return arguments.stream()
                .filter(x -> x.contains("EngineSuppliers"))
                .map(x -> Integer.parseInt(x.replaceFirst("EngineSuppliers=", "")))
                .toList()
                .get(0);
    }


    public int getWorkers() {
        return arguments.stream()
                .filter(x -> x.contains("Workers"))
                .map(x -> Integer.parseInt(x.replaceFirst("Workers=", "")))
                .toList()
                .get(0);
    }

    public int getDealers() {
        return arguments.stream()
                .filter(x -> x.contains("Dealers"))
                .map(x -> Integer.parseInt(x.replaceFirst("Dealers=", "")))
                .toList()
                .get(0);
    }

    public boolean getLogStatus() {
        return arguments.stream()
                .filter(x -> x.contains("LogStatus"))
                .map(x -> Boolean.parseBoolean(x.replaceFirst("LogStatus=", "")))
                .toList()
                .get(0);
    }
}
