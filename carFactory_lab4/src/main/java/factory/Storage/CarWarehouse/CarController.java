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

    boolean newTaskIsExist = false;
    Worker worker;


    public CarController(WorkersThreadPool workersThreadPool, AccessoriesStorage accessoriesStorage, BodyStorage bodyStorage, EngineStorage engineStorage, CarStorage carStorage, AtomicInteger counter) {
        this.workersThreadPool = workersThreadPool;
        this.accessoriesStorage = accessoriesStorage;
        this.bodyStorage = bodyStorage;
        this.engineStorage = engineStorage;
        this.carStorage = carStorage;
        this.counter = counter;
        worker = new Worker(accessoriesStorage, bodyStorage, engineStorage, carStorage, counter);
    }

    private synchronized void waitNewTask() throws InterruptedException {
        while (!newTaskIsExist) {
            wait();
        }
        this.newTaskIsExist = false;
    }

    public synchronized void setNewTaskExist() {
        this.newTaskIsExist = true;
        notifyAll();
    }

    @Override
    public void run() {
        synchronized (this) {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    waitNewTask();
                    carStorage.isFull();
                } catch (InterruptedException ignored) {}

                workersThreadPool.addTask(() -> worker.run());
                workersThreadPool.addTask(() -> worker.run());
            }
        }
    }
}
