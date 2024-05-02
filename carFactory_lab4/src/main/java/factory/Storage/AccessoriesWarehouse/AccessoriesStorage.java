package factory.Storage.AccessoriesWarehouse;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class AccessoriesStorage {
    private final ArrayList<Accessory> accessoryStorage;

    private final AtomicInteger numOfAccessories;
    private int totalProduced;
    private final int maxCapacity;
    private int delay;

    public AccessoriesStorage(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        numOfAccessories = new AtomicInteger(0);
        totalProduced = 0;
        accessoryStorage = new ArrayList<>(maxCapacity);
    }

    public synchronized Accessory getAccessory() throws InterruptedException {
        while (numOfAccessories.get() == 0) {
            wait();
        }
        Accessory accessory = accessoryStorage.get(numOfAccessories.getAndDecrement() - 1);
        accessoryStorage.remove(accessory);
        notifyAll();
        return accessory;
    }

    public synchronized int getDelay() {
        return delay;
    }

    public synchronized void setDelay(int delay) {
        this.delay = delay;
    }

    public int getNumOfAccessories() {
        return numOfAccessories.get();
    }

    public synchronized void addAccessory(Accessory accessory) throws InterruptedException {
        while (numOfAccessories.get() == maxCapacity) {
            wait();
        }
        accessoryStorage.add(accessory);
        totalProduced++;
        numOfAccessories.incrementAndGet();
        notifyAll();
    }

    public int getTotalProduced() {
        return totalProduced;
    }
}
