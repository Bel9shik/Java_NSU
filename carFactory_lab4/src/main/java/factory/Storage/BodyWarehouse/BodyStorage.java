package factory.Storage.BodyWarehouse;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class BodyStorage {
    private AtomicInteger numOfBodies;
    private int totalProduced;
    private final int maxCapacity;
    private int frequency;
    private final ArrayList<Body> bodyStorage;
    private final ReentrantLock lock;

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public BodyStorage(int maxCapacity) {
        lock = new ReentrantLock();
        this.maxCapacity = maxCapacity;
        numOfBodies = new AtomicInteger(0);
        totalProduced = 0;
        bodyStorage = new ArrayList<>(maxCapacity + 1);
    }

    public synchronized boolean isEmpty() {
        return numOfBodies.get() <= 0;
    }

    public synchronized Body getBody() {
        lock.lock();
        if (numOfBodies.get() == 0) {
            try {
                return null;
            } finally {
                lock.unlock();
            }
        } else {
            try {
                Body body = bodyStorage.get(numOfBodies.getAndDecrement() - 1);
                bodyStorage.remove(body);
                return body;
            } finally {
                lock.unlock();
            }
        }
    }

    public synchronized int getFrequency() {
        return frequency;
    }

    public synchronized void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public synchronized int getNumOfBodies() {
        return numOfBodies.get();
    }

    public synchronized void increaseNumOfBodies(Body newBody) {
        numOfBodies.incrementAndGet();
        bodyStorage.add(newBody);
        totalProduced++;
    }

    public synchronized int getTotalProduced() {
        return totalProduced;
    }
}
