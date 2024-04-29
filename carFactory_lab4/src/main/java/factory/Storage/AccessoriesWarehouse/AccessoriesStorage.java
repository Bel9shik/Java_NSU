package factory.Storage.AccessoriesWarehouse;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class AccessoriesStorage {
    private final ArrayList<Accessory> accessories;

    private final AtomicInteger numOfAccessories;
    private int totalProduced;
    private final int maxCapacity;
    private int delay;

    public AccessoriesStorage(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        numOfAccessories = new AtomicInteger(0);
        totalProduced = 0;
        accessories = new ArrayList<>(maxCapacity);
    }

    public synchronized Accessory getAccessory() throws InterruptedException {
        while (numOfAccessories.get() == 0) {
            wait();
        }
        Accessory accessory = accessories.get(numOfAccessories.getAndDecrement() - 1);
        accessories.remove(accessory);
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
        accessories.add(accessory);
        totalProduced++;
        numOfAccessories.incrementAndGet();
        notifyAll();
    }

    public int getTotalProduced() {
        return totalProduced;
    }
}
