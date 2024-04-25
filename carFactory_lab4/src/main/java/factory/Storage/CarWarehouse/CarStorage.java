package factory.Storage.CarWarehouse;

public class CarStorage { //аккуратно с асинхронностью
    private int numberOfCars;
    private int totalProduced;
    private final int maxCapacity;

    public CarStorage(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        numberOfCars = 0;
        totalProduced = 0;
    }

    public int getNumberOfCars() {
        return numberOfCars;
    }

    public void increaseNumberOfCars() {
        numberOfCars++;
    }

    public void decreaseNumberOfCars() {
        numberOfCars--;
    }

    public int getTotalProduced() {
        return totalProduced;
    }

    public void increaseTotalProduced() {
        totalProduced++;
    }

    public void decreaseTotalProduced() {
        totalProduced--;
    }

}
