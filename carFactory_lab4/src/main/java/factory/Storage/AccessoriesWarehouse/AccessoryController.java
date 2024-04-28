package factory.Storage.AccessoriesWarehouse;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class AccessoryController implements Runnable {
    private final AccessoriesStorage accessoriesStorage;
    private final ArrayList<Thread> suppliers;
    private final int countAccessory;
    private final AtomicInteger counter = new AtomicInteger(0);

    public AccessoryController(AccessoriesStorage accessoriesStorage, int countAccessory) {
        this.countAccessory = countAccessory;
        this.accessoriesStorage = accessoriesStorage;
        suppliers = new ArrayList<>(countAccessory);
    }

    public void stop() {
        synchronized (this) {
            notify();
        }
    }

    @Override
    public void run() {
        synchronized (this) {
            for (int i = 0; i < countAccessory; i++) {
                Thread tmp = new Thread(new AccessorySupplier(accessoriesStorage, counter));
                tmp.setName("AccessorySupplier " + (i + 1));
                suppliers.add(tmp);
                tmp.start();
            }

            try {
                wait();
            } catch (InterruptedException e) {
                suppliers.forEach(Thread::interrupt);
            }
        }
    }
}

