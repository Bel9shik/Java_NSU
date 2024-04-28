package factory.Storage;

import factory.Storage.CarWarehouse.Car;
import factory.Storage.CarWarehouse.CarStorage;

public class Dealer implements Runnable {
    CarStorage carStorage;

    public Dealer (CarStorage carStorage) {
        this.carStorage = carStorage;
    }

    @Override
    public void run() {
        synchronized (this) {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    if (carStorage.getFrequency() == 0) continue;
                    wait(carStorage.getFrequency());
                    Car car = carStorage.getCar();
                    if (car != null) {
                        System.out.println(Thread.currentThread().getName() + ": " + car);
                    }
                } catch (InterruptedException ignored) {}
            }
        }
    }
}
