package factory.Storage;

import factory.Storage.AccessoriesWarehouse.AccessoriesStorage;
import factory.Storage.AccessoriesWarehouse.Accessory;
import factory.Storage.BodyWarehouse.Body;
import factory.Storage.BodyWarehouse.BodyStorage;
import factory.Storage.CarWarehouse.Car;
import factory.Storage.CarWarehouse.CarStorage;
import factory.Storage.EngineWarehouse.Engine;
import factory.Storage.EngineWarehouse.EngineStorage;

import java.util.concurrent.atomic.AtomicInteger;

public class Worker implements Runnable {
    private final AccessoriesStorage accessoriesStorage;
    private final BodyStorage bodyStorage;
    private final EngineStorage engineStorage;
    private final CarStorage carStorage;
    private final AtomicInteger counter;

    public Worker(AccessoriesStorage accessoriesStorage, BodyStorage bodyStorage, EngineStorage engineStorage, CarStorage carStorage, AtomicInteger counter) {
        this.accessoriesStorage = accessoriesStorage;
        this.bodyStorage = bodyStorage;
        this.engineStorage = engineStorage;
        this.carStorage = carStorage;
        this.counter = counter;
    }

    @Override
    public void run() {
        Accessory accessory = null;
        Body body = null;
        Engine engine = null;
        Car car;
        synchronized (this) {
            while(!Thread.currentThread().isInterrupted()) {
                if (carStorage.isFull()) {
                    try {
                        wait(10);
                    } catch (InterruptedException ignored) {} //mb log needed
                } else {
                    if (!accessoriesStorage.isEmpty() && accessory == null) {
                        accessory = accessoriesStorage.getAccessory();
                    }
                    if (!bodyStorage.isEmpty() && body == null) {
                        body = bodyStorage.getBody();
                    }
                    if (!engineStorage.isEmpty() && engine == null) {
                        engine = engineStorage.getEngine();
                    }
                    if (engine != null && body != null && accessory != null) {
                        car = new Car(accessory, body, engine, counter.getAndIncrement());
                        carStorage.increaseNumberOfCars(car);
                        accessory = null;
                        body = null;
                        engine = null;
                    }
                }
                try {
                    wait(100);
                } catch (InterruptedException ignored) {}
            }
        }
    }
}
