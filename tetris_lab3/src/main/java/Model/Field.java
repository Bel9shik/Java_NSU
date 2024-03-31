package Model;

import java.util.HashMap;

public final class Field {

    private HashMap<PairCoords, Boolean> field = new HashMap<>(12 * 22); // 10 - horizontal, 20 - vertical

    public HashMap<PairCoords, Boolean> getField() {
        return field;
    }

    public boolean setCellAlive(PairCoords coords) {
        if (field.containsKey(coords)) return false;
        else {
            field.put(coords, true);
            return true;
        }
    }

    public boolean containsCell (PairCoords coords) {
        if (field.containsKey(coords)) return true;
        else return false;
    }
}
