package factory.Storage.CarWarehouse;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class CarStorage {
    private final AtomicInteger numberOfCars;
    private int totalProduced;
    private final int maxCapacity;
    private final ArrayList<Car> cars;
    private int delay;
    private final AtomicInteger waitedCars;

    public CarStorage(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        numberOfCars = new AtomicInteger(0);
        totalProduced = 0;
        cars = new ArrayList<>(maxCapacity);
        waitedCars = new AtomicInteger(0);
    }

    public synchronized void setDelay(int delay) {
        this.delay = delay;
    }

    public synchronized int getDelay() {
        return delay;
    }

    public synchronized void isFull() throws InterruptedException {
            while(numberOfCars.get() == maxCapacity) {
                wait();
            }
    }

    public synchronized Car getCar() throws InterruptedException {
        waitedCars.incrementAndGet();
        while (numberOfCars.get() == 0) {
            wait();
        }
        Car car = cars.get(numberOfCars.getAndDecrement() - 1);
        cars.remove(car);
        waitedCars.decrementAndGet();
        notifyAll();
        return car;
    }

    public synchronized int getWaitedCars() {
        return waitedCars.get();
    }

    public synchronized int getNumberOfCars() {
        return numberOfCars.get();
    }

    public synchronized void addCar(Car car) throws InterruptedException {
        while (numberOfCars.get() == maxCapacity) {
            wait();
        }
        numberOfCars.incrementAndGet();
        cars.add(car);
        totalProduced++;
        notifyAll();
    }

    public synchronized int getTotalProduced() {
        return totalProduced;
    }

}
