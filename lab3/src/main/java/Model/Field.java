package Model;

import java.util.HashMap;

public class Field {

    private HashMap<PairCoords, Boolean> field = new HashMap<>(10 * 18);

    public HashMap<PairCoords, Boolean> getField() {
        return field;
    }

    public void setField(HashMap<PairCoords, Boolean> field) {
        this.field = field;
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
