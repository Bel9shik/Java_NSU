package factory.Storage.AccessoriesWarehouse;

import java.util.concurrent.atomic.AtomicInteger;

public class AccessorySupplier implements Runnable {
    private final AccessoriesStorage accessoriesStorage;
    private final AtomicInteger counter;

    public AccessorySupplier(AccessoriesStorage accessoriesStorage, AtomicInteger counter) {
        this.accessoriesStorage = accessoriesStorage;
        this.counter = counter;
    }

    public void wakeUp() {
        synchronized (this) {
            notify();
        }
    }

    @Override
    public void run() {
        synchronized (this) {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    accessoriesStorage.addAccessory(new Accessory(counter.getAndIncrement()));
                    wait(accessoriesStorage.getDelay() + 1);
                } catch (InterruptedException ignored) {}
            }
        }
    }
}

