package factory.Storage.EngineWarehouse;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class EngineStorage {
    private final AtomicInteger numOfEngines;
    private int totalProduced;
    private final int maxCapacity;
    private int frequency;
    private final ArrayList<Engine> engines;

    public EngineStorage(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        numOfEngines = new AtomicInteger(0);
        totalProduced = 0;
        engines = new ArrayList<>(maxCapacity);
    }

    public synchronized boolean isEmpty() {
        return numOfEngines.get() <= 0;
    }

    public synchronized Engine getEngine() throws InterruptedException {
        if (numOfEngines.get() == 0) {
            wait();
        }
        Engine engine = engines.get(numOfEngines.getAndDecrement() - 1);
        engines.remove(engine);
        notifyAll();
        return engine;
    }

    public synchronized int getFrequency() {
        return frequency;
    }

    public synchronized void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public synchronized int getNumOfEngines() {
        return numOfEngines.get();
    }

    public synchronized void addEngine(Engine engine) throws InterruptedException {
        while (numOfEngines.get() == maxCapacity) {
            wait();
        }
        numOfEngines.incrementAndGet();
        engines.add(engine);
        totalProduced++;
        notifyAll();
    }

    public int getTotalProduced() {
        return totalProduced;
    }

}
