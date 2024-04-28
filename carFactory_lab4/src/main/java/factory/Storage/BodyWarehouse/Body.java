package factory.Storage.BodyWarehouse;

public class Body {
    private int ID;
    public Body(int ID) {
        this.ID = ID;
    }

    public int getID() { //synchronized ?
        return ID;
    }
}
