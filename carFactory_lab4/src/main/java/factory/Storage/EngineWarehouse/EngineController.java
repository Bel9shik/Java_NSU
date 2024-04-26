package factory.Storage.EngineWarehouse;

import factory.Storage.StorageController;

import java.util.concurrent.atomic.AtomicInteger;

public class EngineController implements Runnable{
    private final EngineStorage engineStorage;
    private final int countEngine;
    private boolean isActive = true;
    AtomicInteger counter = new AtomicInteger(0);

    public EngineController(EngineStorage engineStorage, int countEngine) {
        this.engineStorage = engineStorage;
        this.countEngine = countEngine;
    }

    public void start() {
        for (int i = 0; i < countEngine; i++) {
            Thread thread = new Thread(this);
            thread.setName("EngineSupplier" + (i + 1));
            thread.start();
        }
    }

    public void stop() {
        isActive = false;
    }

    @Override
    public void run() {
        while (isActive) {
            synchronized (this) {
                if (engineStorage.getNumOfEngines() < engineStorage.getMaxCapacity()) {
                    if (engineStorage.getFrequency() == 0) continue;
                    engineStorage.increaseNumOfEngines(new Engine(counter.getAndIncrement()));
                }

                try {
                    wait(StorageController.ENGINES_FREQUENCY - engineStorage.getFrequency() + 1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
