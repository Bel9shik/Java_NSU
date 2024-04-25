package factory.Storage.BodyWarehouse;

public class BodyStorage {
    private int numOfBodies;
    private int totalProduced;
    private final int maxCapacity;
    private int frequency;

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public BodyStorage(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        numOfBodies = 0;
        totalProduced = 0;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getNumOfBodies() {
        return numOfBodies;
    }

    public void increaseNumOfBodies() {
        numOfBodies++;
    }

    public void decreaseNumOfBodies() {
        numOfBodies--;
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
