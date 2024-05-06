package factory.Storage.CarWarehouse;

import factory.Storage.AccessoriesWarehouse.Accessory;
import factory.Storage.BodyWarehouse.Body;
import factory.Storage.EngineWarehouse.Engine;

public class Car {
    private final Accessory accessory;
    private final Body body;
    private final Engine engine;
    private final int ID;
    public Car(final Accessory accessory, final Body body,  final Engine engine, final int ID) {
        this.accessory = accessory;
        this.body = body;
        this.engine = engine;
        this.ID = ID;
    }

    public Body getBody() {
        return body;
    }

    public Accessory getAccessory() {
        return accessory;
    }

    public Engine getEngine() {
        return engine;
    }

    @Override
    public String toString() {
        return "Auto " + ID + " (Body: " + body.getID() + ", Engine: " + engine.getID() + ", Accessory: " + accessory.getID() + ")";
    }
}
