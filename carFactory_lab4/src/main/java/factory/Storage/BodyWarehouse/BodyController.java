package factory.Storage.BodyWarehouse;

import factory.Storage.StorageController;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class BodyController implements Runnable {
    private final BodyStorage bodyStorage;
    private final int countBody;
    private final ArrayList<Thread> threads;
    private final AtomicInteger counter;

    public BodyController(BodyStorage bodyStorage, int countBody) {
        this.countBody = countBody;
        this.bodyStorage = bodyStorage;
        this.threads = new ArrayList<>();
        this.counter = new AtomicInteger(0);
    }

    public void stop() {
        synchronized (this) {
            notify();
        }
    }

    @Override
    public void run() {
        synchronized (this) {
            for (int i = 0; i < countBody; i++) {
                Thread thread = new Thread(new BodySupplier(bodyStorage, counter));
                thread.setName("BodySupplier" + i);
                threads.add(thread);
                thread.start();
            }

            try {
                wait();
            } catch (InterruptedException ignored) {
                threads.forEach(Thread::interrupt);
            }
        }
    }
}

