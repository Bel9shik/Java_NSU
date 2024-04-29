package factory.Storage.CarWarehouse;

import ThreadPool.WorkersThreadPool;
import factory.Storage.AccessoriesWarehouse.AccessoriesStorage;
import factory.Storage.BodyWarehouse.BodyStorage;
import factory.Storage.EngineWarehouse.EngineStorage;
import factory.Storage.Worker;

import java.util.concurrent.atomic.AtomicInteger;

public class CarController implements Runnable{
    private final WorkersThreadPool workersThreadPool;
    private final AccessoriesStorage accessoriesStorage;
    private final BodyStorage bodyStorage;
    private final EngineStorage engineStorage;
    private final CarStorage carStorage;
    private final AtomicInteger counter;

    public CarController(WorkersThreadPool workersThreadPool, AccessoriesStorage accessoriesStorage, BodyStorage bodyStorage, EngineStorage engineStorage, CarStorage carStorage, AtomicInteger counter) {
        this.workersThreadPool = workersThreadPool;
        this.accessoriesStorage = accessoriesStorage;
        this.bodyStorage = bodyStorage;
        this.engineStorage = engineStorage;
        this.carStorage = carStorage;
        this.counter = counter;
    }

    @Override
    public void run() {
        synchronized (this) {
            while (!Thread.currentThread().isInterrupted()) {
                while (!carStorage.isFull()){
                    try {
                        wait(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    workersThreadPool.addTask(new Worker(accessoriesStorage, bodyStorage, engineStorage, carStorage, counter));
                }
            }
        }
    }
}
