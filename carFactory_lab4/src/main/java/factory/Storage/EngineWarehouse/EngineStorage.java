package factory.Storage.EngineWarehouse;

public class EngineStorage {
    private int numOfEngines;
    private int totalProduced;
    private final int maxCapacity;

    public EngineStorage(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        numOfEngines = 0;
        totalProduced = 0;
    }

    public int getNumOfEngines() {
        return numOfEngines;
    }

    public void increaseNumOfEngines() {
        numOfEngines++;
    }

    public void decreaseNumOfEngines() {
        numOfEngines--;
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
