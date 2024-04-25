package factory.Storage.AccessoriesWarehouse;

public class AccessoryController implements Runnable {
    AccessoriesStorage accessoriesStorage;
    private final int countAccessory;
    boolean isActive = true;

    public AccessoryController(AccessoriesStorage accessoriesStorage, int countAccessory) {
        this.countAccessory = countAccessory;
        this.accessoriesStorage = accessoriesStorage;
    }

    public void start() {
        int curCount = 0;

        for (int i = 0; i < countAccessory; i++) {
            Thread tmp = new Thread(this);
            tmp.setName("Accessory " + (curCount++));
            tmp.start();
        }

    }

    @Override
    public void run() {

        long lastTime = System.currentTimeMillis();
        long curTime;
        double delta = 0;
        while (isActive) {
            synchronized (this) {
                if (accessoriesStorage.getNumberOfAccessories() < accessoriesStorage.getMaxCapacity()) {
                    curTime = System.currentTimeMillis();
                    delta += (double) (curTime - lastTime) / accessoriesStorage.getFrequency();
                    lastTime = curTime;

                    if (delta >= 1) {
                        accessoriesStorage.increaseNumberOfAccessories(new Accessory(228));
                        delta--;
                    }
                } else {
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

