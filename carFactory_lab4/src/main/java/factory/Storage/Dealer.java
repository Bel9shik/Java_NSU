package factory.Storage;

import factory.Storage.CarWarehouse.Car;
import factory.Storage.CarWarehouse.CarStorage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Dealer implements Runnable {
    private final CarStorage carStorage;
    private final boolean isLogging;

    public Dealer(final CarStorage carStorage, final boolean isLogging) {
        this.carStorage = carStorage;
        this.isLogging = isLogging;
    }

    @Override
    public void run() {
        synchronized (this) {
            BufferedWriter bufferedWriter = null;
            if (isLogging) {
                try {
                    bufferedWriter = new BufferedWriter(new FileWriter("cars.txt"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    wait(carStorage.getFrequency() + 1);
                    Car car = carStorage.getCar();
                    if (bufferedWriter != null) {
                        bufferedWriter.write(Thread.currentThread().getName() + ": " + car + "\n");
                    }
                } catch (InterruptedException | IOException ignored) {
                }
            }
        }
    }
}

