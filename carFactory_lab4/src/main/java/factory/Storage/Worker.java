package factory.Storage;

import factory.Storage.AccessoriesWarehouse.AccessoriesStorage;
import factory.Storage.AccessoriesWarehouse.Accessory;
import factory.Storage.BodyWarehouse.Body;
import factory.Storage.BodyWarehouse.BodyStorage;
import factory.Storage.CarWarehouse.Car;
import factory.Storage.CarWarehouse.CarStorage;
import factory.Storage.EngineWarehouse.Engine;
import factory.Storage.EngineWarehouse.EngineStorage;

public class Worker implements Runnable {
    AccessoriesStorage accessoriesStorage;
    BodyStorage bodyStorage;
    EngineStorage engineStorage;
    CarStorage carStorage;

    public Worker(AccessoriesStorage accessoriesStorage, BodyStorage bodyStorage, EngineStorage engineStorage, CarStorage carStorage) {
        this.accessoriesStorage = accessoriesStorage;
        this.bodyStorage = bodyStorage;
        this.engineStorage = engineStorage;
        this.carStorage = carStorage;
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
                        wait(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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
                        car = new Car(accessory, body, engine);
                        carStorage.increaseNumberOfCars(car);
                        accessory = null;
                        body = null;
                        engine = null;
                    }
                }
                try {
                    wait(1000);
                } catch (InterruptedException ignored) {}
            }

        }
    }
}
