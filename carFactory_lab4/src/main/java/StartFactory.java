import factory.Storage.StorageController;
import factory.View.ViewController;

public class StartFactory {
    public static void main(String[] args) {
        //in future program start in controller
        ConfigFileParser configFileParser = new ConfigFileParser("config.properties");
        StorageController storageController = new StorageController(configFileParser.getAccessoriesCapacity(), configFileParser.getBodiesCapacity(), configFileParser.getEnginesCapacity(), configFileParser.getCarsCapacity());
        System.out.println(configFileParser.getBodiesCapacity());
        ViewController viewController = new ViewController(storageController);
    }
}
