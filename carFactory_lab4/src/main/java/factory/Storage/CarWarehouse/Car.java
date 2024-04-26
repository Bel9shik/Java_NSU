package factory.Storage.CarWarehouse;

import factory.Storage.AccessoriesWarehouse.Accessory;
import factory.Storage.BodyWarehouse.Body;
import factory.Storage.EngineWarehouse.Engine;

public class Car {
    private Accessory accessory;
    private Body body;
    private Engine engine;
    public Car(Accessory accessory, Body body, Engine engine) {
        this.accessory = accessory;
        this.body = body;
        this.engine = engine;
    }
}
