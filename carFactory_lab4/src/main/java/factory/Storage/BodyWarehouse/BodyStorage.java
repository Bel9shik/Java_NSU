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

        Body body = bodyStorage.get(numOfBodies.getAndDecrement() - 1);
        bodyStorage.remove(body);
        lock.unlock();
        return body;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getNumOfBodies() {
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
