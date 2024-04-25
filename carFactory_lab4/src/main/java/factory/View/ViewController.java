package factory.View;

import factory.Storage.StorageController;

import javax.swing.*;

public class ViewController {
    private final JFrame frame;
    private final FactoryPanel factoryPanel;
    public ViewController (StorageController storageController) {
        factoryPanel = new FactoryPanel(storageController);
        frame = new CreateFrame("Bel9sh production", factoryPanel);
    }

    public void update() {
        factoryPanel.updateData();
    }
}
