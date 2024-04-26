package factory.Storage;

import factory.Storage.AccessoriesWarehouse.AccessoriesStorage;
import factory.Storage.AccessoriesWarehouse.Accessory;
import factory.Storage.AccessoriesWarehouse.AccessoryController;
import factory.Storage.BodyWarehouse.Body;
import factory.Storage.BodyWarehouse.BodyController;
import factory.Storage.BodyWarehouse.BodyStorage;
import factory.Storage.CarWarehouse.CarStorage;
import factory.Storage.EngineWarehouse.Engine;
import factory.Storage.EngineWarehouse.EngineController;
import factory.Storage.EngineWarehouse.EngineStorage;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class StorageController {
    public final static int ACCESSORY_FREQUENCY = 100;
    public final static int BODY_FREQUENCY = 100;
    public final static int ENGINES_FREQUENCY = 100;
    private final int dealersQuantity;

    private boolean isActive = true;

    private final ArrayList<Thread> threads;
    private final AtomicInteger counterAccessory;
    private final AtomicInteger counterBody;
    private final AtomicInteger counterEngine;

    AccessoriesStorage accessoriesStorage;
    AccessoryController accessoryController;

    BodyStorage bodyStorage;
    BodyController bodyController;

    EngineStorage engineStorage;
    EngineController engineController;

    CarStorage carStorage;

    public StorageController(final int dealersQuantity, final int accessoryCapacity, final int bodyCapacity, final int engineCapacity, final int carCapacity) {
        this.dealersQuantity = dealersQuantity;
        accessoriesStorage = new AccessoriesStorage(accessoryCapacity);
        bodyStorage = new BodyStorage(bodyCapacity);
        engineStorage = new EngineStorage(engineCapacity);
        carStorage = new CarStorage(carCapacity);
        threads = new ArrayList<>(10);
        counterAccessory = new AtomicInteger(0);
        counterBody = new AtomicInteger(0);
        counterEngine = new AtomicInteger(0);
    }

    public int getDealersQuantity() {
        return dealersQuantity;
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

    public void startFactory(final int numberOfAccessory, final int numberOfBody, final int numberOfEngine, final int dealersQuantity, final int workersQuantity) {
        accessoryController = new AccessoryController(accessoriesStorage, numberOfAccessory);
        accessoryController.start();

        bodyController = new BodyController(bodyStorage, numberOfBody);
        bodyController.start();

        engineController = new EngineController(engineStorage, numberOfEngine);
        engineController.start();

        for (int i = 0; i < workersQuantity; i++) {
            Thread tmp = new Thread(new Worker(accessoriesStorage, bodyStorage, engineStorage, carStorage));
            tmp.setName("Worker " + i);
            tmp.start();
        }

    }

    public void stopFactory() {
        isActive = false;
        accessoryController.stop();
        bodyController.stop();
        engineController.stop();
        threads.forEach(Thread::interrupt);
    }

    public boolean isActive() {
        return isActive;
    }
}
