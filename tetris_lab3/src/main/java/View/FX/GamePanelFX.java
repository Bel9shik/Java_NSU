package View.FX;

import Model.Block;
import Model.ModelController;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.canvas.Canvas;
import javafx.scene.text.Font;

import java.awt.Color;
import java.util.ArrayList;

public class GamePanelFX extends Pane {

    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 720;
    public static final int PLAY_WIDTH = 300; // need: PLAY_WIDTH % Block.SIZE = 0
    public final static int PLAY_HEIGHT = 600;
    public final static int LEFT_X = (WINDOW_WIDTH / 2) - (PLAY_WIDTH / 2); // 1280/2 - 300/2
    public final static int RIGHT_X = LEFT_X + PLAY_WIDTH;
    public final static int TOP_Y = 50;
    public final static int BOTTOM_Y = TOP_Y + PLAY_HEIGHT;;
    public static final int FIGURE_START_X = LEFT_X + (PLAY_WIDTH / 2) - Block.WIDTH;
    public static final int FIGURE_START_Y = TOP_Y + Block.HEIGHT;
    public static final int NEXT_FIGURE_X = RIGHT_X + 175;
    public static final int NEXT_FIGURE_Y = TOP_Y + 500;

    private ModelController model;
    private Canvas canvas;

    public  GamePanelFX(ModelController model) {
        this.model = model;

        this.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        getChildren().add(canvas);

        setFocusTraversable(true);
//        setOnKeyPressed(this::handle);
//        this.addKeyListener(new KeyHandlerSwing());


//        this.setBackground(Color.BLACK);

    }

//    private void handleKeyPress(KeyEvent event) {
//        KeyHandlerSwing.(event.getCode());
//    }


    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        drawPlayArea();
    }

    private void drawBlock(GraphicsContext gc, Block block) {
        Color swingColor = block.color;
        int rgb = swingColor.getRGB();
        gc.setFill(javafx.scene.paint.Color.rgb((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF));
        gc.fillRect(block.coords.getX() + block.margin, block.coords.getY() + block.margin, Block.WIDTH - (block.margin * 2), Block.HEIGHT - (block.margin * 2));
    }

    private void drawFigure (GraphicsContext gc, ArrayList<Block> figure) {
        for (Block block : figure) {
            drawBlock(gc, block);
        }
    }


    private void drawStaticBlocks (GraphicsContext gc) {
        for (Block block : model.staticBlocks) {
            drawBlock(gc, block);
        }
    }

    private void drawPlayArea() { //draw play area frame
        //game field
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0,0, WINDOW_WIDTH, WINDOW_HEIGHT);

        gc.setStroke(javafx.scene.paint.Color.WHITE);
        gc.setLineWidth(4);
        gc.strokeRect(LEFT_X - 4, TOP_Y - 4, PLAY_WIDTH + 8, PLAY_HEIGHT + 8);

        //the next figure
        int x = RIGHT_X + 100;
        int y = BOTTOM_Y - 200;
        gc.strokeRect(x, y, 200, 200);
        gc.setFill(javafx.scene.paint.Color.WHITE);
        gc.setFont(new Font("Arial", 30));
        gc.fillText("NEXT", x + 60, y + 60);

        //score
        gc.strokeRect(x, TOP_Y, 250, 300);
        x += 40;
        y = TOP_Y + 90;
        gc.fillText("LINES: " + model.lines, x, y);
        y += 70;
        gc.fillText("SCORE: " + model.score, x, y);


        if (model.currentFigure != null) {
            drawFigure(gc, (ArrayList<Block>) model.currentFigure);
        }

        if (model.nextFigure != null) {
            drawFigure(gc, (ArrayList<Block>) model.nextFigure);
        }

        drawStaticBlocks(gc);

        if (model.gameOver) {
            x = LEFT_X + 25;
            y = TOP_Y + 325;
            gc.setFill(javafx.scene.paint.Color.BLACK);
            gc.fillRect(x, y - 25, 250, 75);
            gc.setFill(javafx.scene.paint.Color.WHITE);
            gc.fillText("GAME OVER", x + 10, y + 25 );
        }

    }
}
