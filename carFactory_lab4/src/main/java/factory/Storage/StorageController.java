package factory.Storage;

import factory.Storage.AccessoriesWarehouse.AccessoriesStorage;
import factory.Storage.BodyWarehouse.BodyStorage;
import factory.Storage.CarWarehouse.CarStorage;
import factory.Storage.EngineWarehouse.EngineStorage;

public class StorageController {
    AccessoriesStorage accessoriesStorage;
    BodyStorage bodyStorage;
    EngineStorage engineStorage;
    CarStorage carStorage;

    public StorageController(final int accessoryCapacity, final int bodyCapacity, final int engineCapacity, final int carCapacity) {
        accessoriesStorage = new AccessoriesStorage(accessoryCapacity);
        bodyStorage = new BodyStorage(bodyCapacity);
        engineStorage = new EngineStorage(engineCapacity);
        carStorage = new CarStorage(carCapacity);
    }

    public int getAccessoriesCount() {
        return accessoriesStorage.getNumberOfAccessories();
    }

    public int getBodyCount() {
        return bodyStorage.getNumOfBodies();
    }

    public int getEngineCount() {
        return engineStorage.getNumOfEngines();
    }

    public int getTotalAccessoriesProduced() {
        return accessoriesStorage.getTotalProduced();
    }

    public int getTotalBodyProduced() {
        return bodyStorage.getTotalProduced();
    }

    public int getTotalEngineProduced() {
        return engineStorage.getTotalProduced();
    }

    public int getTotalCarProduced() {
        return carStorage.getTotalProduced();
    }

    public int getCarsCount() {
        return carStorage.getNumberOfCars();
    }

}
