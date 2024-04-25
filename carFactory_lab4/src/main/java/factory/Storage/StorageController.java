package factory.Storage;

import factory.Storage.AccessoriesWarehouse.AccessoriesStorage;
import factory.Storage.AccessoriesWarehouse.AccessoryController;
import factory.Storage.BodyWarehouse.BodyStorage;
import factory.Storage.CarWarehouse.CarStorage;
import factory.Storage.EngineWarehouse.EngineStorage;

import java.util.concurrent.atomic.AtomicLong;

public class StorageController {
    AccessoriesStorage accessoriesStorage;
    BodyStorage bodyStorage;
    EngineStorage engineStorage;
    CarStorage carStorage;

    private boolean isActive = true;

    public StorageController(final int accessoryCapacity, final int bodyCapacity, final int engineCapacity, final int carCapacity) {
        accessoriesStorage = new AccessoriesStorage(accessoryCapacity);
        bodyStorage = new BodyStorage(bodyCapacity);
        engineStorage = new EngineStorage(engineCapacity);
        carStorage = new CarStorage(carCapacity);
    }

    public AccessoriesStorage getAccessoryStorage() {
        return accessoriesStorage;
    }

    public BodyStorage getBodyStorage() {
        return bodyStorage;
    }

    public EngineStorage getEngineStorage() {
        return engineStorage;
    }

    public CarStorage getCarStorage() {
        return carStorage;
    }

    public void startFactory(final int numberOfAccessory, final int numberOfBody, final int numberOfEngine) {
        AccessoryController accessoryController = new AccessoryController(accessoriesStorage, numberOfAccessory);
        accessoryController.start();
    }
}
