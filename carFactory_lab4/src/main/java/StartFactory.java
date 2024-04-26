import factory.Storage.StorageController;
import factory.View.ViewController;

public class StartFactory {
    public static void main(String[] args) {
        //in future program start in controller
        ConfigFileParser configFileParser = new ConfigFileParser("config.properties");
        StorageController storageController = new StorageController(configFileParser.getDealers(), configFileParser.getAccessoriesCapacity(), configFileParser.getBodiesCapacity(), configFileParser.getEnginesCapacity(), configFileParser.getCarsCapacity());
        ViewController viewController = new ViewController(storageController);
        storageController.startFactory(configFileParser.getAccessorySuppliers(), configFileParser.getBodySuppliers(), configFileParser.getEngineSuppliers(), configFileParser.getDealers(), configFileParser.getWorkers());
        while (storageController.isActive()) {
            viewController.update();
        }
    }
}
