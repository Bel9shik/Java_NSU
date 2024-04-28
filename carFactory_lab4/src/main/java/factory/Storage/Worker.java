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

    public Worker(final AccessoriesStorage accessoriesStorage, final BodyStorage bodyStorage, final EngineStorage engineStorage, final CarStorage carStorage, final AtomicInteger counter) {
        this.accessoriesStorage = accessoriesStorage;
        this.bodyStorage = bodyStorage;
        this.engineStorage = engineStorage;
        this.carStorage = carStorage;
        this.counter = counter;
    }

    @Override
    public void run() {
        synchronized (this) {
            Accessory accessory;
            Body body;
            Engine engine;
            Car car;
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    accessory = accessoriesStorage.getAccessory();
                    body = bodyStorage.getBody();
                    engine = engineStorage.getEngine();
                    car = new Car(accessory, body, engine, counter.getAndIncrement());
                    carStorage.addCar(car);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }
}
