package factory.Storage.AccessoriesWarehouse;

public class AccessoriesStorage {
    private int numberOfAccessories;
    private int totalProduced;
    private final int maxCapacity;

    public AccessoriesStorage(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        numberOfAccessories = 0;
        totalProduced = 0;
    }

    public int getNumberOfAccessories() {
        return numberOfAccessories;
    }

    public void increaseNumberOfAccessories() {
        numberOfAccessories++;
    }

    public void decreaseNumberOfAccessories() {
        numberOfAccessories--;
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
