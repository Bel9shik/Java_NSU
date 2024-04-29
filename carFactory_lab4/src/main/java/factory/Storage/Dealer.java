package factory.Storage;

import factory.Storage.CarWarehouse.Car;
import factory.Storage.CarWarehouse.CarController;
import factory.Storage.CarWarehouse.CarStorage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Dealer implements Runnable {
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
            BufferedWriter bufferedWriter = null;
            if (isLogging) {
                try {
                    bufferedWriter = new BufferedWriter(new FileWriter("cars.txt", true)); //logging
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    carController.setNewTaskExist();
                    wait(carStorage.getDelay() + 1);
                    Car car = carStorage.getCar();
                    if (bufferedWriter != null) {
                        bufferedWriter.write(Thread.currentThread().getName() + ": " + car + "\n");
                        bufferedWriter.close();
                    }
                } catch (InterruptedException | IOException ignored) {
                }
            }
        }
    }
}

