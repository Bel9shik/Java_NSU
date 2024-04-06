package View;

import Controller.KeyHandler;
import Model.Figures.Block;
import Model.Figures.GeneralFigure;
import Model.PlayManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 720;
    public static final int PLAY_WIDTH = 300; // need: PLAY_WIDTH % Block.SIZE = 0
    public final static int PLAY_HEIGHT = 600;
    public final static int LEFT_X = (WINDOW_WIDTH / 2) - (PLAY_WIDTH / 2); // 1280/2 - 300/2
    public final static int RIGHT_X = LEFT_X + PLAY_WIDTH;
    public final static int TOP_Y = 50;
    public final static int BOTTOM_Y = TOP_Y + PLAY_HEIGHT;;
    public static final int FIGURE_START_X = LEFT_X + (PLAY_WIDTH / 2) - Block.SIZE;
    public static final int FIGURE_START_Y = TOP_Y + Block.SIZE;
    public static final int NEXT_FIGURE_X = RIGHT_X + 175;
    public static final int NEXT_FIGURE_Y = TOP_Y + 500;

    PlayManager playManager;

    public GamePanel(PlayManager playManager) {

        this.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setLayout(null);

        this.addKeyListener(new KeyHandler());
        this.setFocusable(true);

        this.playManager = playManager;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawPlayArea((Graphics2D) g);
    }

    private static void drawBlock(Graphics2D g2, Block block) {
        g2.setColor(block.color);
        g2.fillRect(block.coords.getX() + block.margin, block.coords.getY() + block.margin, Block.SIZE - (block.margin * 2), Block.SIZE - (block.margin * 2));
    }

    public static void drawFigure (Graphics2D g2, GeneralFigure figure) {
        g2.setColor(figure.mino.get(0).color);
        for (Block block : figure.mino) {
            drawBlock(g2, block);
        }
    }

    private void drawStaticBlocks (Graphics2D g2) {
        for (Block block : PlayManager.staticBlocks) {
            drawBlock(g2, block);
        }
    }

    public void drawPlayArea(Graphics2D g2) { //draw play area frame
        //game field
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRect(LEFT_X - 4, TOP_Y - 4, PLAY_WIDTH + 8, PLAY_HEIGHT + 8);

        //the next figure
        int x = RIGHT_X + 100;
        int y = BOTTOM_Y - 200;
        g2.drawRect(x, y, 200, 200);
        g2.setFont(new Font("Arial", Font.PLAIN, 30));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawString("NEXT", x + 60, y + 60);

        //score
        g2.drawRect(x, TOP_Y, 250, 300);
        x += 40;
        y = TOP_Y + 90;
        g2.drawString("LINES: " + playManager.lines, x, y);
        y += 70;
        g2.drawString("SCORE: " + playManager.score, x, y);


        if (playManager.currentFigure != null) {
            GamePanel.drawFigure(g2, playManager.currentFigure);
        }

        if (playManager.nextFigure != null) {
            GamePanel.drawFigure(g2, playManager.nextFigure);
        }

        drawStaticBlocks(g2);

        if (playManager.gameOver) {
            x = LEFT_X + 25;
            y = TOP_Y + 325;
            g2.setColor(Color.BLACK);
            g2.fillRect(x, y - 25, 250, 75);
            g2.setColor(Color.white);
            g2.drawString("GAME OVER", x + 10, y + 25 );
        }

    }
}
