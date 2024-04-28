package factory.Storage.AccessoriesWarehouse;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class AccessoriesStorage {
    private final ReentrantLock lock;
    private final ArrayList<Accessory> accessories;

    private final AtomicInteger numOfAccessories;
    private int totalProduced;
    private final int maxCapacity;
    private int frequency;

    public AccessoriesStorage(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        numOfAccessories = new AtomicInteger(0);
        totalProduced = 0;
        accessories = new ArrayList<>(maxCapacity);
        lock = new ReentrantLock();
    }

    public synchronized boolean isEmpty() {
        return numOfAccessories.get() <= 0;
    }

    public synchronized Accessory getAccessory() {
        lock.lock();
        if (numOfAccessories.get() == 0) {
            try {
                return null;
            } finally {
                lock.unlock();
            }
        } else {
            try {
                Accessory accessory = accessories.get(numOfAccessories.getAndDecrement() - 1);
                accessories.remove(accessory);
                return accessory;
            } finally {
                lock.unlock();
            }
        }
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getFrequency() {
        return frequency;
    }

    public synchronized void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getNumOfAccessories() {
        return numOfAccessories.get();
    }

    public synchronized void increaseNumberOfAccessories(Accessory accessory) {
        numOfAccessories.incrementAndGet();
        accessories.add(accessory);
        totalProduced++;
    }

    public int getTotalProduced() {
        return totalProduced;
    }
}
