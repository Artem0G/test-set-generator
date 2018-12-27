package entities;

import types.Range;
import types.StringType;

public class StringParam extends Parameter {
    private Range lengthRange;
    private StringType stringType;

    public StringParam(boolean isNullable, StringType stringType, Range lengthRange) {
        super(isNullable);
        this.stringType = stringType;
        this.lengthRange = lengthRange;
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
