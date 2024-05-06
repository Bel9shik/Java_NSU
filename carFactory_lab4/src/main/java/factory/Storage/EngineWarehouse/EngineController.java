package factory.Storage.EngineWarehouse;

import factory.Storage.StorageController;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class EngineController implements Runnable{
    private final EngineStorage engineStorage;
    private final int countEngine;
    private final ArrayList<Thread> threads;
    private final AtomicInteger counter;

    public EngineController(EngineStorage engineStorage, int countEngine) {
        this.engineStorage = engineStorage;
        this.countEngine = countEngine;
        threads = new ArrayList<>(countEngine);
        counter = new AtomicInteger(0);
    }

    public void stop() {
        synchronized (this) {
            notifyAll();
        }
    }

    @Override
    public void run() {
        synchronized (this) {
            for (int i = 0; i < countEngine; i++) {
                Thread thread = new Thread(new EngineSupplier(engineStorage, counter));
                thread.setName("EngineSupplier" + (i + 1));
                threads.add(thread);
                thread.start();
            }

            try {
                wait();
            } catch (InterruptedException ignored) {
                threads.forEach(Thread::interrupt);
            }
        }
    }
}
