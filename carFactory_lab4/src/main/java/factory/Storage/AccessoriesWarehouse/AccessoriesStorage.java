package factory.Storage.AccessoriesWarehouse;

import java.util.ArrayList;

public class AccessoriesStorage {
    private ArrayList<Accessory> accessories;

    private int numberOfAccessories;
    private int totalProduced;
    private final int maxCapacity;
    private int frequency;

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public AccessoriesStorage(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        numberOfAccessories = 0;
        totalProduced = 0;
        accessories = new ArrayList<>();
    }

    public int getFrequency() {
        return frequency;
    }

    public synchronized void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getNumberOfAccessories() {
        return numberOfAccessories;
    }

    public synchronized void increaseNumberOfAccessories(Accessory accessory) {
        numberOfAccessories++;
        accessories.add(accessory);
        increaseTotalProduced();
    }

    public synchronized void decreaseNumberOfAccessories() {
        numberOfAccessories--;
    }

    public int getTotalProduced() {
        return totalProduced;
    }

    public synchronized void increaseTotalProduced() {
        totalProduced++;
    }

    public synchronized void decreaseTotalProduced() {
        totalProduced--;
    }
}
