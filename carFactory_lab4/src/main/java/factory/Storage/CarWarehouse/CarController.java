package factory.Storage.CarWarehouse;

public class CarController implements Runnable {
    CarStorage carStorage;
    public CarController(CarStorage carStorage) {
        this.carStorage = carStorage;
    }

    public void wakeUp() {
        synchronized (this) {
            notify();
        }
    }


    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            if (carStorage.isFull()) {
                try {
                    wait();
                } catch (InterruptedException ignored) {}
            }
        }
    }
}
