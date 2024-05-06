package factory.View;

import factory.Storage.StorageController;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ViewController {
    private final JFrame frame;
    private final FactoryPanel factoryPanel;
    public ViewController (StorageController storageController) {
        factoryPanel = new FactoryPanel(storageController);
        frame = new CreateFrame("Bel9sh production", factoryPanel);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                storageController.stopFactory();
                frame.dispose();
            }
        });
    }

    public void update() {
        factoryPanel.updateData();
    }
}
