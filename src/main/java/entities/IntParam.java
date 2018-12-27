package entities;

import types.Range;

import java.util.ArrayList;
import java.util.List;

public class IntParam extends Parameter {
    private Range range;
    private List<Integer> possibleValues;

    public IntParam(boolean isNullable, Range range) {
        super(isNullable);
        this.range = range;
    }

    public IntParam(boolean isNullable, int...values) {
        super(isNullable);
        possibleValues = new ArrayList<>();
        for (int i : values) {
            possibleValues.add(i);
        }
    }

    @Override
    public Object[] getPositiveData(int numberOfNegativeTests) {
        return new Object[0];
    }

    @Override
    public Object[] getNegativeData(int numberOfNegativeTests) {
        return new Object[0];
    }

    @Override
    public int getMinRequiredNegativeQuantity() {
        return 0;
    }

    @Override
    public int getMaxNegativeQuantity() {
        return 0;
    }

    @Override
    public int getMinRequiredPositiveQuantity() {
        return 0;
    }

    @Override
    public int getMaxPositiveQuantity() {
        return 0;
    }
}
