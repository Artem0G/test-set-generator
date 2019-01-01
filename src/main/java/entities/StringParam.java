package entities;

import types.NullIs;
import types.Range;
import types.StringType;

import java.util.Set;

public class StringParam extends Parameter {
    private Range lengthRange;
    private StringType stringType;

    public StringParam(NullIs isNullable, StringType stringType, Range lengthRange) {
        super(isNullable);
        this.stringType = stringType;
        this.lengthRange = lengthRange;
    }

    public StringParam(NullIs isNullable) {
        super(isNullable);
    }

    @Override
    public synchronized void initValues() {
        super.initValues();
    }

    @Override
    public Object[] getPossibleValues(int quantity) {
        return super.getPossibleValues(quantity);
    }

    @Override
    public Object[] getImpossibleValues(int quantity) {
        return super.getImpossibleValues(quantity);
    }

    @Override
    protected void defineMandatoryValues() {

    }

    @Override
    protected Object[] getPossibleValuesInternal(int quantity) {
        return new Object[0];
    }

    @Override
    protected Object[] getImpossibleValuesInternal(int quantity) {
        return new Object[0];
    }

    @Override
    public long getMaxImpossibleQuantity() {
        return 0;
    }

    @Override
    public long getMaxPossibleQuantity() {
        return 0;
    }

    @Override
    public int getMandatoryPossibleQuantity() {
        return super.getMandatoryPossibleQuantity();
    }

    @Override
    public int getMandatoryImpossibleQuantity() {
        return super.getMandatoryImpossibleQuantity();
    }

    @Override
    protected void setMandatoryPossibleQuantity(int mandatoryPossibleQuantity) {
        super.setMandatoryPossibleQuantity(mandatoryPossibleQuantity);
    }

    @Override
    protected void setMandatoryImpossibleQuantity(int mandatoryImpossibleQuantity) {
        super.setMandatoryImpossibleQuantity(mandatoryImpossibleQuantity);
    }

    @Override
    protected void increaseMandatoryPossibleQuantity(int mandatoryPositiveQuantity) {
        super.increaseMandatoryPossibleQuantity(mandatoryPositiveQuantity);
    }

    @Override
    protected void increaseMandatoryImpossibleQuantity(int mandatoryNegativeQuantity) {
        super.increaseMandatoryImpossibleQuantity(mandatoryNegativeQuantity);
    }

    @Override
    protected <T> Object[] getValuesFromSet(int quantity, Set<T> values) {
        return super.getValuesFromSet(quantity, values);
    }
}
