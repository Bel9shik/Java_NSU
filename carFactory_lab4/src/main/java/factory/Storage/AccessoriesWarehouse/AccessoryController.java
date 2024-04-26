package factory.Storage.AccessoriesWarehouse;

import factory.Storage.StorageController;

import java.util.concurrent.atomic.AtomicInteger;

public class AccessoryController implements Runnable {
    private AccessoriesStorage accessoriesStorage;
    private final int countAccessory;
    private boolean isActive = true;
    AtomicInteger counter = new AtomicInteger(0);

    public AccessoryController(AccessoriesStorage accessoriesStorage, int countAccessory) {
        this.countAccessory = countAccessory;
        this.accessoriesStorage = accessoriesStorage;
    }

    public void start() {
        for (int i = 0; i < countAccessory; i++) {
            Thread tmp = new Thread(this);
            tmp.setName("AccessorySupplier " + (i + 1));
            tmp.start();
        }

    }

    public void stop() {
        isActive = false;
    }

    @Override
    public void run() {
        while (isActive) {
            synchronized (this) {
                if (accessoriesStorage.getNumOfAccessories() < accessoriesStorage.getMaxCapacity()) {
                    if (accessoriesStorage.getFrequency() == 0) continue;
                    accessoriesStorage.increaseNumberOfAccessories(new Accessory(counter.getAndIncrement()));
                }
                try {
                    wait(StorageController.ACCESSORY_FREQUENCY - accessoriesStorage.getFrequency() + 1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

