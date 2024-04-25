package factory.Storage.EngineWarehouse;

public class EngineStorage {
    private int numOfEngines;
    private int totalProduced;
    private final int maxCapacity;
    private int frequency;

    public EngineStorage(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        numOfEngines = 0;
        totalProduced = 0;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
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
