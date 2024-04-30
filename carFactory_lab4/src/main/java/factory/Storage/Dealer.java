package factory.Storage;

import factory.Storage.CarWarehouse.Car;
import factory.Storage.CarWarehouse.CarController;
import factory.Storage.CarWarehouse.CarStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;


public class Dealer implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Dealer.class);
    private final CarStorage carStorage;
    private final boolean isLogging;
    CarController carController;

    public Dealer(final CarStorage carStorage, final CarController carController, final boolean isLogging) {
        this.carStorage = carStorage;
        this.isLogging = isLogging;
        this.carController = carController;
    }

    @Override
    public void run() {
        synchronized (this) {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    carController.setNewTaskExist();
                    wait(carStorage.getDelay() + 1);
                    Car car = carStorage.getCar();
                    if (isLogging) {
                        logger.info("{}, {}", Thread.currentThread().getName(), car);
                    }
                } catch (InterruptedException ignored) {
                }
            }
        }
    }
}

