package factory.Storage.BodyWarehouse;

import java.util.concurrent.atomic.AtomicInteger;

public class BodySupplier implements Runnable {
    private  final BodyStorage bodyStorage;
    private final AtomicInteger counter;

    public BodySupplier(BodyStorage bodyStorage, AtomicInteger counter) {
        this.bodyStorage = bodyStorage;
        this.counter = counter;
    }


    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            synchronized (this) {
                try {
                    if (bodyStorage.getNumOfBodies() < bodyStorage.getMaxCapacity()) {
                        if (bodyStorage.getFrequency() == 0) continue;
                        bodyStorage.increaseNumOfBodies(new Body(counter.getAndIncrement()));
                    }

                    wait(bodyStorage.getFrequency());

                } catch (InterruptedException ignored) {}
            }
        }
    }
}
