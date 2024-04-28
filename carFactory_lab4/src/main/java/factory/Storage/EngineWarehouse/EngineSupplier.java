package factory.Storage.EngineWarehouse;

import java.util.concurrent.atomic.AtomicInteger;

public class EngineSupplier implements  Runnable {
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
                if (engineStorage.getNumOfEngines() < engineStorage.getMaxCapacity()) {
                    if (engineStorage.getFrequency() == 0) continue;
                    engineStorage.increaseNumOfEngines(new Engine(counter.getAndIncrement()));
                }

                try {
                    wait(engineStorage.getFrequency());
                } catch (InterruptedException ignored) {}
            }
        }
    }
}
