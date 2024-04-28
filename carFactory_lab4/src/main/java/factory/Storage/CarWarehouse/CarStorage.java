package factory.Storage.CarWarehouse;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class CarStorage {
    private AtomicInteger numberOfCars;
    private int totalProduced;
    private final int maxCapacity;
    private final ArrayList<Car> cars;
    private int frequency;

    public CarStorage(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        numberOfCars = new AtomicInteger(0);
        totalProduced = 0;
        cars = new ArrayList<>(maxCapacity);
    }

    public synchronized void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public synchronized int getFrequency() {
        return frequency;
    }

    public synchronized boolean isFull() {
        return numberOfCars.get() == maxCapacity;
    }

    public synchronized Car getCar() {
        if (numberOfCars.get() == 0) {
            return null;
        } else {
            Car car = cars.get(numberOfCars.getAndDecrement() - 1);
            cars.remove(car);
            return car;
        }
    }

    public synchronized int getNumberOfCars() {
        return numberOfCars.get();
    }

    public synchronized void increaseNumberOfCars(Car car) {
        numberOfCars.getAndIncrement();
        cars.add(car);
        totalProduced++;
    }

    public synchronized int getTotalProduced() {
        return totalProduced;
    }

}
