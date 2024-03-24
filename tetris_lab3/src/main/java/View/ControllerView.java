package View;

import Model.Field;
import Model.PairCoords;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ControllerView extends JFrame {

    private JFrame mainFrame;
    private String iconPath;
    //    private ImageIcon iconFile;
    private JPanel buttonsPanel;
    private JButton start;
    private final int width, height;

    private Field seeField = new Field();

    public ControllerView(String winTitle, String path, int w, int h) throws IOException {
        mainFrame = new JFrame(winTitle);
        iconPath = path;
        width = w;
        height = h;
        //why dont work?
//        mainFrame.setIconImage(new ImageIcon(String.valueOf(ControllerView.class.getClassLoader().getResourceAsStream(path))).getImage());
//        mainFrame.setIconImage(new ImageIcon(path).getImage());
//        mainFrame.setIconImage(new Image());
//        mainFrame.setIconImage(new ImageIcon(ControllerView.class.getResource(path)).getImage());
        var img = ImageIO.read(new File(path));
        mainFrame.add(new JLabel(new ImageIcon(img)));
        mainFrame.setLayout(new GridLayout());
        mainFrame.setIconImage(img);
        mainFrame.setSize(width, height);
        buttonsPanel = new JPanel();
        start = new JButton("Старт");
//        buttonsPanel.add(start);
        mainFrame.getContentPane().add(BorderLayout.CENTER, buttonsPanel);
        GridLayout grid = new GridLayout(10, 20);
        grid.setVgap(1);
//        grid.setVgap(2);
        JPanel mainPanel = new JPanel(grid);
//        mainPanel.add(new JFigure());
        mainFrame.getContentPane().add(mainPanel);

        for (int i = 0; i < 10; ++i) {
            for (int j = 0; i < 20; ++i) {
                PairCoords coords = new PairCoords(i, j);
                seeField.setCellAlive(coords);
//                mainPanel.add(coords);
            }
        }
    }

    public void createApplicationWindow() {
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
//        mainFrame.pack();
        checkSwingFunc();
    }


    public void checkSwingFunc() {

    }
}
