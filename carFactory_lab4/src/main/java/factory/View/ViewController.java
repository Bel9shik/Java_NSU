package factory.View;

import factory.Storage.StorageController;

import javax.swing.*;

public class ViewController {
    private JFrame frame;
    private FactoryPanel factory;
    private StorageController storageController;
    public ViewController (StorageController storageController) {
        this.storageController = storageController;
        factory = new FactoryPanel(storageController);
        frame = new CreateFrame("Bel9sh production", factory);
    }
}
