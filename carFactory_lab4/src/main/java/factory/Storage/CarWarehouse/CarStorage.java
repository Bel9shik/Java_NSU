package factory.Storage.CarWarehouse;

import java.util.ArrayList;

public class CarStorage { //аккуратно с асинхронностью
    private int numberOfCars;
    private int totalProduced;
    private final int maxCapacity;
    private ArrayList<Car> cars;

    public CarStorage(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        numberOfCars = 0;
        totalProduced = 0;
        cars = new ArrayList<>(maxCapacity);
    }

    public synchronized boolean isFull() {
        return numberOfCars == maxCapacity;
    }

    public synchronized int getNumberOfCars() {
        return numberOfCars;
    }

    public synchronized void increaseNumberOfCars(Car car) {
        numberOfCars++;
        cars.add(car);
        totalProduced++;
    }

    public void decreaseNumberOfCars() {
        numberOfCars--;
    }

    public synchronized int getTotalProduced() {
        return totalProduced;
    }

}
