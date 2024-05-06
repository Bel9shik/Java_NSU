package factory.Storage;

import ThreadPool.WorkersThreadPool;
import factory.Storage.AccessoriesWarehouse.AccessoriesStorage;
import factory.Storage.AccessoriesWarehouse.AccessoryController;
import factory.Storage.BodyWarehouse.BodyController;
import factory.Storage.BodyWarehouse.BodyStorage;
import factory.Storage.CarWarehouse.CarController;
import factory.Storage.CarWarehouse.CarStorage;
import factory.Storage.EngineWarehouse.EngineController;
import factory.Storage.EngineWarehouse.EngineStorage;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class StorageController {
    public final static int ACCESSORY_MAX_DELAY = 100;
    public final static int BODY_MAX_DELAY = 100;
    public final static int ENGINES_MAX_DELAY = 100;
    public final static int DEALERS_MAX_DELAY = 100;
    private final int dealersQuantity;
    private final AtomicInteger carsCounter;
    private final boolean isLogging;

    private boolean isActive = true;

    private final ArrayList<Thread> threads;

    AccessoriesStorage accessoriesStorage;
    AccessoryController accessoryController;

    BodyStorage bodyStorage;
    BodyController bodyController;

    EngineStorage engineStorage;
    EngineController engineController;

    CarStorage carStorage;
    CarController carController;

    WorkersThreadPool workersThreadPool;

    public StorageController(final int dealersQuantity, final int accessoryCapacity, final int bodyCapacity, final int engineCapacity, final int carCapacity, final boolean isLogging) {
        this.dealersQuantity = dealersQuantity;
        accessoriesStorage = new AccessoriesStorage(accessoryCapacity);
        bodyStorage = new BodyStorage(bodyCapacity);
        engineStorage = new EngineStorage(engineCapacity);
        workersThreadPool = new WorkersThreadPool(dealersQuantity);
        carStorage = new CarStorage(carCapacity);
        carsCounter = new AtomicInteger(0);
        carController = new CarController(workersThreadPool, accessoriesStorage, bodyStorage, engineStorage, carStorage, carsCounter);
        threads = new ArrayList<>(dealersQuantity + 1);
        this.isLogging = isLogging;
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
        Thread tmp = new Thread(accessoryController);
        tmp.setName("Accessory Controller");
        threads.add(tmp);
        tmp.start();

        bodyController = new BodyController(bodyStorage, numberOfBody);
        tmp = new Thread(bodyController);
        tmp.setName("Body Controller");
        threads.add(tmp);
        tmp.start();

        engineController = new EngineController(engineStorage, numberOfEngine);
        tmp = new Thread(engineController);
        tmp.setName("Engine Controller");
        threads.add(tmp);
        tmp.start();

        for (int i = 0; i < workersQuantity; i++) {
            workersThreadPool.addTask(new Thread(new Worker(accessoriesStorage, bodyStorage, engineStorage, carStorage, carsCounter)));
        }

        tmp = new Thread(carController);
        tmp.setName("Car Controller");
        threads.add(tmp);
        tmp.start();

        for (int i = 0; i < dealersQuantity; i++) {
            tmp = new Thread(new Dealer(carStorage, carController, isLogging));
            tmp.setName("Dealer " + i);
            threads.add(tmp);
            tmp.start();
        }

    }

    public void stopFactory() {
        isActive = false;
        accessoryController.stop();
        bodyController.stop();
        engineController.stop();
        workersThreadPool.shutdown();
        threads.forEach(Thread::interrupt);
    }

    public boolean isActive() {
        return isActive;
    }
}
