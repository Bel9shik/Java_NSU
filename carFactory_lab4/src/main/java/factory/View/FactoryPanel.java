package factory.View;

import factory.Storage.StorageController;

import javax.swing.*;
import java.awt.*;

public class FactoryPanel extends JPanel {

    private final JPanel mainPanel;
    private final StorageController storageController;

    private final JLabel accessoryCurrent;
    private final JLabel accessoryTotal;
    private final JLabel accessoryLabel;

    private final JLabel bodyCurrent;
    private final JLabel bodyTotal;
    private final JLabel bodyLabel;

    private final JLabel engineCurrent;
    private final JLabel engineTotal;
    private final JLabel engineLabel;

    private final JLabel dealersCurrent;

    private final JLabel carInStock;
    private final JLabel carTotalProduced;
    private final JLabel waitedCars;

    public FactoryPanel(StorageController storageController) {
        this.storageController = storageController;

        JPanel grid = new JPanel(new GridLayout(5, 4, 4, 4));
        grid.setBounds(0, 0, 800, 200);
        JSlider speedSlider = new JSlider(0, StorageController.ACCESSORY_FREQUENCY, 10);

        grid.add(new JLabel("Type"));
        grid.add(new JLabel("Delay"));
        grid.add(new JLabel("Available"));
        grid.add(new JLabel("Total"));

        grid.add(new JLabel("Accessories:"));
        accessoryCurrent = new JLabel(String.valueOf(storageController.getAccessoryStorage().getNumOfAccessories()));
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
        speedSlider = new JSlider(0, StorageController.BODY_FREQUENCY, 10);
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
        speedSlider = new JSlider(0, StorageController.ENGINES_FREQUENCY, 10);
        engineCurrent = new JLabel(String.valueOf(storageController.getEngineStorage().getNumOfEngines()));
        engineTotal = new JLabel(String.valueOf(storageController.getEngineStorage().getTotalProduced()));
        engineLabel = new JLabel("Produce engines: " + speedSlider.getValue());
        storageController.getEngineStorage().setFrequency(10);
        speedSlider.addChangeListener(e -> {
            int frequency = ((JSlider) e.getSource()).getValue();
            storageController.getEngineStorage().setFrequency(frequency);//TODO: freq gets from storageController. Not engine/body storage
            engineLabel.setText("Produce engines: " + frequency);
        });
        speedSlider.setPaintTrack(false);
        speedSlider.setMajorTickSpacing(20);
        speedSlider.setPaintLabels(true);
        speedSlider.setPaintTicks(true);
        grid.add(speedSlider);
        grid.add(engineCurrent);
        grid.add(engineTotal);

        grid.add(new JLabel("Dealers: "));
        speedSlider = new JSlider(0, StorageController.DEALERS_FREQUENCY, storageController.getDealersQuantity());
        JLabel dealersTotal = new JLabel(String.valueOf(StorageController.DEALERS_FREQUENCY)); // change dealers on panel
        dealersCurrent = new JLabel("" + speedSlider.getValue());
        storageController.getCarStorage().setFrequency(speedSlider.getValue());
        speedSlider.addChangeListener(e -> {
            storageController.getCarStorage().setFrequency(((JSlider) e.getSource()).getValue());
            dealersCurrent.setText("" + ((JSlider) e.getSource()).getValue());
        });
        speedSlider.setPaintTrack(false);
        speedSlider.setMajorTickSpacing(20);
        speedSlider.setPaintLabels(true);
        speedSlider.setPaintTicks(true);
        grid.add(speedSlider);
        grid.add(dealersCurrent);
        grid.add(dealersTotal);


        JPanel flow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        flow.add(grid);
        carInStock = new JLabel("Number of cars in stock: " + storageController.getCarStorage().getNumberOfCars());
        flow.add(carInStock);
        carTotalProduced = new JLabel("Total cars produced: " + storageController.getCarStorage().getTotalProduced());
        waitedCars = new JLabel("Waited cars: " + storageController.getCarStorage().getWaitedCars());
        flow.add(carTotalProduced);
        flow.add(accessoryLabel);
        flow.add(bodyLabel);
        flow.add(engineLabel);
        flow.add(waitedCars);

        mainPanel = flow;
    }

    public void updateData() {
        accessoryCurrent.setText(String.valueOf(storageController.getAccessoryStorage().getNumOfAccessories()));
        accessoryTotal.setText(String.valueOf(storageController.getAccessoryStorage().getTotalProduced()));
        bodyCurrent.setText(String.valueOf(storageController.getBodyStorage().getNumOfBodies()));
        bodyTotal.setText(String.valueOf(storageController.getBodyStorage().getTotalProduced()));
        engineCurrent.setText(String.valueOf(storageController.getEngineStorage().getNumOfEngines()));
        engineTotal.setText(String.valueOf(storageController.getEngineStorage().getTotalProduced()));
        carInStock.setText("Number of cars in stock: " + storageController.getCarStorage().getNumberOfCars());
        carTotalProduced.setText("Total cars produced: " + storageController.getCarStorage().getTotalProduced());
        waitedCars.setText("Waited cars: " + storageController.getCarStorage().getWaitedCars());

        revalidate();
        repaint();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
