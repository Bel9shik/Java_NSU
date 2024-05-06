package factory.Storage.EngineWarehouse;

import java.util.concurrent.atomic.AtomicInteger;

public class EngineSupplier implements Runnable {
    EngineStorage engineStorage;
    AtomicInteger counter;

    public EngineSupplier(EngineStorage engineStorage, AtomicInteger counter) {
        this.engineStorage = engineStorage;
        this.counter = counter;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            synchronized (this) {
                try {
                    engineStorage.addEngine(new Engine(counter.getAndIncrement()));
                    wait(engineStorage.getDelay() + 1);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }
}
