package factory.Storage;

import factory.Storage.AccessoriesWarehouse.AccessoriesStorage;
import factory.Storage.AccessoriesWarehouse.AccessoryController;
import factory.Storage.BodyWarehouse.BodyController;
import factory.Storage.BodyWarehouse.BodyStorage;
import factory.Storage.CarWarehouse.CarController;
import factory.Storage.CarWarehouse.CarStorage;
import factory.Storage.EngineWarehouse.EngineController;
import factory.Storage.EngineWarehouse.EngineStorage;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class StorageController {
    public final static int ACCESSORY_FREQUENCY = 100;
    public final static int BODY_FREQUENCY = 100;
    public final static int ENGINES_FREQUENCY = 100;
    public final static int DEALERS_FREQUENCY = 100;
    private final int dealersQuantity;
    private int curDealersFreq;
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

    ExecutorService executorService;

    public StorageController(final int dealersQuantity, final int accessoryCapacity, final int bodyCapacity, final int engineCapacity, final int carCapacity, final boolean isLogging) {
        this.dealersQuantity = dealersQuantity;
        accessoriesStorage = new AccessoriesStorage(accessoryCapacity);
        bodyStorage = new BodyStorage(bodyCapacity);
        engineStorage = new EngineStorage(engineCapacity);
        carStorage = new CarStorage(carCapacity);
        threads = new ArrayList<>(dealersQuantity + 1);
        carController = new CarController(carStorage);
        curDealersFreq = dealersQuantity;
        carsCounter = new AtomicInteger(0);
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

//        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < workersQuantity; i++) {
            executorService.submit(new Worker(accessoriesStorage, bodyStorage, engineStorage, carStorage, carsCounter));
        }

        for (int i = 0; i < dealersQuantity; i++) {
            tmp = new Thread(new Dealer(carStorage, isLogging));
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
        executorService.shutdown();
        threads.forEach(Thread::interrupt);
    }

    public boolean isActive() {
        return isActive;
    }
}
