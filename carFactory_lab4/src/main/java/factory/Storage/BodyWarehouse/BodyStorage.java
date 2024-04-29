package factory.Storage.BodyWarehouse;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class BodyStorage {
    private AtomicInteger numOfBodies;
    private int totalProduced;
    private final int maxCapacity;
    private int delay;
    private final ArrayList<Body> bodyStorage;

    public BodyStorage(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        numOfBodies = new AtomicInteger(0);
        totalProduced = 0;
        bodyStorage = new ArrayList<>(maxCapacity + 1);
    }

    public synchronized Body getBody() throws InterruptedException {
        while (numOfBodies.get() == 0) {
            wait();
        }
        Body body = bodyStorage.get(numOfBodies.getAndDecrement() - 1);
        bodyStorage.remove(body);
        notifyAll();
        return body;
    }


    public synchronized int getDelay() {
        return delay;
    }

    public synchronized void setDelay(int delay) {
        this.delay = delay;
    }

    public synchronized int getNumOfBodies() {
        return numOfBodies.get();
    }

    public synchronized void addBody(Body newBody) throws InterruptedException {
        while (numOfBodies.get() == maxCapacity) {
            wait();
        }
        bodyStorage.add(newBody);
        totalProduced++;
        numOfBodies.incrementAndGet();
        notifyAll();
    }

    public synchronized int getTotalProduced() {
        return totalProduced;
    }
}
