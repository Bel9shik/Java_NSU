package factory.Storage.BodyWarehouse;

import factory.Storage.StorageController;

import java.util.concurrent.atomic.AtomicInteger;

public class BodyController implements Runnable {
    private final BodyStorage bodyStorage;
    private final int countBody;
    AtomicInteger counter = new AtomicInteger(0);
    private boolean isActive = true;

    public BodyController(BodyStorage bodyStorage, int countBody) {
        this.countBody = countBody;
        this.bodyStorage = bodyStorage;
    }

    public void start() {
        for (int i = 0; i < countBody; i++) {
            Thread thread = new Thread(this);
            thread.setName("BodySupplier" + i);
            thread.start();
        }
    }

    public void stop() {
        isActive = false;
        notifyAll();
    }

    @Override
    public void run() {

        while (isActive) {
            synchronized (this) {
                if (bodyStorage.getNumOfBodies() < bodyStorage.getMaxCapacity()) {
                    if (bodyStorage.getFrequency() == 0) continue;
                    bodyStorage.increaseNumOfBodies(new Body(counter.getAndIncrement()));
                }

                try {
                    wait(StorageController.BODY_FREQUENCY - bodyStorage.getFrequency() + 1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (bodyStorage.getNumOfBodies() >= bodyStorage.getMaxCapacity()) { //TODO: notify all processes
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

