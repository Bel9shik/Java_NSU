package Controller;

import Model.Block;
import Model.PairCoords;

import java.awt.*;
import java.util.*;
import java.util.List;

public final class FigureFactory {
    Map<String, List<Block>> mapOfFigures;
    List<String> backOfFigures;
    private int count = 0;

    public FigureFactory()  {
        setDefaultFigures();
    }

    private void setDefaultFigures(){
        mapOfFigures = new HashMap<>();
        backOfFigures = new ArrayList<>();

        mapOfFigures.put("I", new ArrayList<>(4));
        mapOfFigures.put("J", new ArrayList<>(4));
        mapOfFigures.put("L", new ArrayList<>(4));
        mapOfFigures.put("O", new ArrayList<>(4));
        mapOfFigures.put("Z", new ArrayList<>(4));
        mapOfFigures.put("S", new ArrayList<>(4));
        mapOfFigures.put("T", new ArrayList<>(4));

        for (Map.Entry<String, List<Block>> entry : mapOfFigures.entrySet()) {
            backOfFigures.add(entry.getKey());
            if (entry.getKey().equals("I")) {
                // o o o o

                // 1 0 2 3
                entry.getValue().add(new Block(Color.CYAN, new PairCoords(0,0)));
                entry.getValue().add(new Block(Color.CYAN, new PairCoords(-1, 0)));
                entry.getValue().add(new Block(Color.CYAN, new PairCoords(1, 0)));
                entry.getValue().add(new Block(Color.CYAN, new PairCoords(2, 0)));
            } else if (entry.getKey().equals("J")) {
                // o o o
                //     o

                // 1 0 2
                //     3
                entry.getValue().add(new Block(Color.BLUE, new PairCoords(0, 0)));
                entry.getValue().add(new Block(Color.BLUE, new PairCoords(-1, 0)));
                entry.getValue().add(new Block(Color.BLUE, new PairCoords(1, 0)));
                entry.getValue().add(new Block(Color.BLUE, new PairCoords(1, 1)));
            }else if (entry.getKey().equals("L")) {
                //     o
                // o o o

                //     3
                // 1 0 2
                entry.getValue().add(new Block(Color.ORANGE, new PairCoords(0, 0)));
                entry.getValue().add(new Block(Color.ORANGE, new PairCoords(-1, 0)));
                entry.getValue().add(new Block(Color.ORANGE, new PairCoords(1, 0)));
                entry.getValue().add(new Block(Color.ORANGE, new PairCoords(1, -1)));
            }else if (entry.getKey().equals("O")) {
                // o o
                // o o

                // 0 1
                // 2 3
                entry.getValue().add(new Block(Color.YELLOW, new PairCoords(0, 0)));
                entry.getValue().add(new Block(Color.YELLOW, new PairCoords(1, 0)));
                entry.getValue().add(new Block(Color.YELLOW, new PairCoords(0, 1)));
                entry.getValue().add(new Block(Color.YELLOW, new PairCoords(1, 1)));
            }else if (entry.getKey().equals("Z")) {
                // o o
                //   o o

                // 3 2
                //   0 1
                entry.getValue().add(new Block(Color.RED, new PairCoords(0, 0)));
                entry.getValue().add(new Block(Color.RED, new PairCoords(1, 0)));
                entry.getValue().add(new Block(Color.RED, new PairCoords(0, -1)));
                entry.getValue().add(new Block(Color.RED, new PairCoords(-1, -1)));
            }else if (entry.getKey().equals("S")) {
                //   o o
                // o o

                //   2 3
                // 1 0
                entry.getValue().add(new Block(Color.GREEN, new PairCoords(0, 0)));
                entry.getValue().add(new Block(Color.GREEN, new PairCoords(-1, 0)));
                entry.getValue().add(new Block(Color.GREEN, new PairCoords(0, -1)));
                entry.getValue().add(new Block(Color.GREEN, new PairCoords(1, -1)));
            }else if (entry.getKey().equals("T")) {
                //   o
                // o o o

                //   3
                // 1 0 2
                entry.getValue().add(new Block(Color.MAGENTA, new PairCoords(0, 0)));
                entry.getValue().add(new Block(Color.MAGENTA, new PairCoords(-1, 0)));
                entry.getValue().add(new Block(Color.MAGENTA, new PairCoords(1, 0)));
                entry.getValue().add(new Block(Color.MAGENTA, new PairCoords(0, -1)));
            }
            Collections.shuffle(backOfFigures);
        }
    }

    public List<Block> getNextFigure() {
        if (count == backOfFigures.size() - 1) {
            count = -1;
            setDefaultFigures();
        }
        count++;
        return mapOfFigures.get(backOfFigures.get(count));
    }
}
