package View;

import Controller.KeyHandler;
import Model.Figures.Block;
import Model.Figures.GeneralFigure;
import Model.PlayManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 720;
    public static final int PLAY_WIDTH = 300; // need: PLAY_WIDTH % Block.SIZE = 0
    public final static int PLAY_HEIGHT = 600;
    public static int left_x = (WINDOW_WIDTH / 2) - (PLAY_WIDTH / 2); // 1280/2 - 300/2
    public static int right_x = left_x + PLAY_WIDTH;
    public static int top_y = 50;
    public static int bottom_y = top_y + PLAY_HEIGHT;;
    public static final int FIGURE_START_X = left_x + (PLAY_WIDTH / 2) - Block.SIZE;
    public static final int FIGURE_START_Y = top_y + Block.SIZE;
    public static final int NEXT_FIGURE_X = right_x + 175;
    public static final int NEXT_FIGURE_Y = top_y + 500;
    Thread gameThread;

    private final int FPS = 60;

    PlayManager playManager;
    public GamePanel() {
        this.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setLayout(null);

        this.addKeyListener(new KeyHandler());
        this.setFocusable(true);

        playManager = new PlayManager();
    }

    public void launchGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() { //game loop
        System.out.println("run");
        double drawInterval = 1000000000.0 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    private void update() {
        if (!playManager.gameOver) {
            playManager.update();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawPlayArea((Graphics2D) g);
    }

    public static void draw(Graphics2D g2, GeneralFigure figure) {
        g2.setColor(figure.mino.get(0).color);
        for (Block block : figure.mino) {
            block.draw(g2);
        }
    }

    public void drawPlayArea(Graphics2D g2) { //draw play area frame
        //game field
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRect(left_x - 4, top_y - 4, PLAY_WIDTH + 8, PLAY_HEIGHT + 8);

        //the next figure
        int x = right_x + 100;
        int y = bottom_y - 200;
        g2.drawRect(x, y, 200, 200);
        g2.setFont(new Font("Arial", Font.PLAIN, 30));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawString("NEXT", x + 60, y + 60);

        //score
        g2.drawRect(x, top_y, 250, 300);
        x += 40;
        y = top_y + 90;
        g2.drawString("LINES: " + playManager.lines, x, y);
        y += 70;
        g2.drawString("SCORE: " + playManager.score, x, y);



        if (playManager.gameOver) {
            x = left_x + 25;
            y = top_y + 325;
            g2.drawString("GAME OVER", x, y );
        }

        if (playManager.currentFigure != null) {
            GamePanel.draw(g2, playManager.currentFigure);
        }

        if (playManager.nextFigure != null) {
            GamePanel.draw(g2, playManager.nextFigure);
        }

        for (int i = 0; i < PlayManager.staticBlocks.size(); i++) {
            PlayManager.staticBlocks.get(i).draw(g2);
        }


    }
}
