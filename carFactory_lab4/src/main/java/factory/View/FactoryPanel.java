package factory.View;

import factory.Storage.StorageController;

import javax.swing.*;
import java.awt.*;

public class FactoryPanel extends JPanel {

    private StorageController storageController;

    private JPanel mainPanel;

    private JLabel accessoryLabel;
    private JLabel bodyLabel;
    private JLabel engineLabel;

    public FactoryPanel(StorageController storageController) {
        this.storageController = storageController;
        JPanel grid = new JPanel(new GridLayout(4, 4, 4, 4));
        JSlider speedSlider = new JSlider(0, 100, 10);

        grid.add(new JLabel("Type"));
        grid.add(new JLabel("Speed"));
        grid.add(new JLabel("Warehouse"));
        grid.add(new JLabel("Total Produced"));

        grid.add(new JLabel("Accessories:"));
        accessoryLabel = new JLabel("Produce accessories: " + speedSlider.getValue());
        speedSlider.addChangeListener((e) -> accessoryLabel.setText("Produce accessories: " + ((JSlider)e.getSource()).getValue()));
        speedSlider.setPaintTrack(false);
        speedSlider.setMajorTickSpacing(20);
        speedSlider.setPaintLabels(true);
        speedSlider.setPaintTicks(true);
        grid.add(speedSlider);
        grid.add(new JLabel(String.valueOf(storageController.getAccessoriesCount())));
        grid.add(new JLabel(String.valueOf(storageController.getTotalAccessoriesProduced())));

        grid.add(new JLabel("Bodies:"));
        speedSlider = new JSlider(0, 100, 10);
        bodyLabel = new JLabel("Produce bodies: " + speedSlider.getValue());
        speedSlider.addChangeListener((e) -> bodyLabel.setText("Produce bodies: " + ((JSlider)e.getSource()).getValue()));
        speedSlider.setPaintTrack(false);
        speedSlider.setMajorTickSpacing(20);
        speedSlider.setPaintLabels(true);
        speedSlider.setPaintTicks(true);
        grid.add(speedSlider);
        grid.add(new JLabel(String.valueOf(storageController.getBodyCount())));
        grid.add(new JLabel(String.valueOf(storageController.getTotalBodyProduced())));

        grid.add(new JLabel("Engines:"));
        speedSlider = new JSlider(0, 100, 10);
        engineLabel = new JLabel("Produce engines: " + speedSlider.getValue());
        speedSlider.addChangeListener((e) -> engineLabel.setText("Produce engines: " + ((JSlider)e.getSource()).getValue()));
        speedSlider.setPaintTrack(false);
        speedSlider.setMajorTickSpacing(20);
        speedSlider.setPaintLabels(true);
        speedSlider.setPaintTicks(true);
        grid.add(speedSlider);
        grid.add(new JLabel(String.valueOf(storageController.getEngineCount())));
        grid.add(new JLabel(String.valueOf(storageController.getTotalEngineProduced())));

        JPanel flow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        flow.add(grid);
        flow.add(new JLabel("Number of cars in stock: " + storageController.getCarsCount()));
        flow.add(new JLabel("Total cars produced: " + storageController.getTotalCarProduced()));
        flow.add(accessoryLabel);
        flow.add(bodyLabel);
        flow.add(engineLabel);

        mainPanel = flow;
    }


    public JPanel getMainPanel() {
        return mainPanel;
    }
}
