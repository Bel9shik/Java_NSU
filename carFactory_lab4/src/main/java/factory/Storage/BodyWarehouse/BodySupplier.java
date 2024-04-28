package factory.Storage.BodyWarehouse;

import factory.Storage.AccessoriesWarehouse.Accessory;

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
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        bodyStorage.addBody(new Body(counter.getAndIncrement()));
                        wait(bodyStorage.getFrequency() + 1);
                    } catch (InterruptedException ignored) {}
                }
            }
        }
    }
}
