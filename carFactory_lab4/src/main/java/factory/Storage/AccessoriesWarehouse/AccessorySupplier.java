package factory.Storage.AccessoriesWarehouse;

import java.util.concurrent.atomic.AtomicInteger;

public class AccessorySupplier implements Runnable{
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
        while (!Thread.currentThread().isInterrupted()) {
            synchronized (this) {
                if (accessoriesStorage.getNumOfAccessories() < accessoriesStorage.getMaxCapacity()) {
                    if (accessoriesStorage.getFrequency() == 0) continue;
                    accessoriesStorage.increaseNumberOfAccessories(new Accessory(counter.getAndIncrement()));
                }
                try {
                    wait(accessoriesStorage.getFrequency());
                } catch (InterruptedException ignored) {}
            }
        }
    }
}
