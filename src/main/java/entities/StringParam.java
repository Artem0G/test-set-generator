package entities;

import types.Range;

import java.util.ArrayList;
import java.util.List;

public class IntParam extends Parameter {
    private Range range;
    private List<Integer> possibleValues;

    public IntParam(Range range) {
        this.range = range;
    }

    public IntParam(int...values) {
        possibleValues = new ArrayList<>();
        for (int i : values) {
            possibleValues.add(i);
        }
    }
}
