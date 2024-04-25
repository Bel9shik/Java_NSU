package factory.View;

import factory.Storage.StorageController;

import javax.swing.*;
import java.awt.*;

public class FactoryPanel extends JPanel {

    private JPanel mainPanel;
    private StorageController storageController;

    private JLabel accessoryCurrent;
    private JLabel accessoryTotal;
    private JLabel accessoryLabel;

    private JLabel bodyCurrent;
    private JLabel bodyTotal;
    private JLabel bodyLabel;

    private JLabel engineCurrent;
    private JLabel engineTotal;
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
        accessoryCurrent = new JLabel(String.valueOf(storageController.getAccessoryStorage().getNumberOfAccessories()));
        accessoryTotal = new JLabel(String.valueOf(storageController.getAccessoryStorage().getTotalProduced()));
        accessoryLabel = new JLabel("Produce accessories: " + speedSlider.getValue());
        storageController.getAccessoryStorage().setFrequency(10);
        speedSlider.addChangeListener(e -> {
            int frequency = ((JSlider) e.getSource()).getValue();
            storageController.getAccessoryStorage().setFrequency(frequency);
            accessoryLabel.setText("Produce bodies: " + frequency);
        });
        speedSlider.setPaintTrack(false);
        speedSlider.setMajorTickSpacing(20);
        speedSlider.setPaintLabels(true);
        speedSlider.setPaintTicks(true);
        grid.add(speedSlider);
        grid.add(accessoryCurrent);
        grid.add(accessoryTotal);


        grid.add(new JLabel("Bodies:"));
        speedSlider = new JSlider(0, 100, 10);
        bodyCurrent = new JLabel(String.valueOf(storageController.getBodyStorage().getNumOfBodies()));
        bodyTotal = new JLabel(String.valueOf(storageController.getBodyStorage().getTotalProduced()));
        bodyLabel = new JLabel("Produce bodies: " + speedSlider.getValue());
        storageController.getBodyStorage().setFrequency(10);
        speedSlider.addChangeListener(e -> {
            int frequency = ((JSlider) e.getSource()).getValue();
            storageController.getBodyStorage().setFrequency(frequency);
            bodyLabel.setText("Produce bodies: " + frequency);
        });
        speedSlider.setPaintTrack(false);
        speedSlider.setMajorTickSpacing(20);
        speedSlider.setPaintLabels(true);
        speedSlider.setPaintTicks(true);
        grid.add(speedSlider);
        grid.add(bodyCurrent);
        grid.add(bodyTotal);


        grid.add(new JLabel("Engines:"));
        speedSlider = new JSlider(0, 100, 10);
        engineCurrent = new JLabel(String.valueOf(storageController.getEngineStorage().getNumOfEngines()));
        engineTotal = new JLabel(String.valueOf(storageController.getEngineStorage().getTotalProduced()));
        engineLabel = new JLabel("Produce engines: " + speedSlider.getValue());
        storageController.getEngineStorage().setFrequency(10);
        speedSlider.addChangeListener(e -> {
            int frequency = ((JSlider) e.getSource()).getValue();
            storageController.getEngineStorage().setFrequency(frequency);
            engineLabel.setText("Produce engines: " + frequency);
        });
        speedSlider.setPaintTrack(false);
        speedSlider.setMajorTickSpacing(20);
        speedSlider.setPaintLabels(true);
        speedSlider.setPaintTicks(true);
        grid.add(speedSlider);
        grid.add(engineCurrent);
        grid.add(engineTotal);


        JPanel flow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        flow.add(grid);
        flow.add(new JLabel("Number of cars in stock: " + storageController.getCarStorage().getNumberOfCars())); //needed update
        flow.add(new JLabel("Total cars produced: " + storageController.getCarStorage().getTotalProduced()));
        flow.add(accessoryLabel);
        flow.add(bodyLabel);
        flow.add(engineLabel);

        mainPanel = flow;
    }

    public void updateData() {
        accessoryCurrent.setText(String.valueOf(storageController.getAccessoryStorage().getNumberOfAccessories()));
        accessoryTotal.setText(String.valueOf(storageController.getAccessoryStorage().getTotalProduced()));
        bodyCurrent.setText(String.valueOf(storageController.getBodyStorage().getNumOfBodies()));
        bodyTotal.setText(String.valueOf(storageController.getBodyStorage().getTotalProduced()));
        engineCurrent.setText(String.valueOf(storageController.getEngineStorage().getNumOfEngines()));
        engineTotal.setText(String.valueOf(storageController.getEngineStorage().getTotalProduced()));

        revalidate();
        repaint();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
