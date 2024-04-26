package factory.Storage.EngineWarehouse;

import factory.Storage.BodyWarehouse.Body;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class EngineStorage {
    private AtomicInteger numOfEngines;
    private int totalProduced;
    private final int maxCapacity;
    private int frequency;
    ArrayList<Engine> engines;
    private final ReentrantLock lock;

    public EngineStorage(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        numOfEngines = new AtomicInteger(0);
        totalProduced = 0;
        engines = new ArrayList<>(maxCapacity);
        lock = new ReentrantLock();
    }

    public synchronized boolean isEmpty() {
        return numOfEngines.get() <= 0;
    }

    public synchronized Engine getEngine() {
        lock.lock();
        Engine engine = engines.get(numOfEngines.getAndDecrement() - 1);
        engines.remove(engine);
        lock.unlock();
        return engine;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getNumOfEngines() {
        return numOfEngines.get();
    }

    public synchronized void increaseNumOfEngines(Engine engine) {
        numOfEngines.incrementAndGet();
        engines.add(engine);
        totalProduced++;
    }

    public int getTotalProduced() {
        return totalProduced;
    }

}
