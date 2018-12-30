package entities;

import java.util.Set;

public abstract class Parameter {
    private boolean isNullable;
    private int mandatoryPossibleQuantity;
    private int mandatoryImpossibleQuantity;
    private boolean isInitiated;

    public Parameter(boolean isNullable) {
        this.isNullable = isNullable;
    }

    public boolean isNullable() {
        return isNullable;
    }

    public synchronized void initValues() {
        if (!isInitiated) {
            defineMandatoryValues();
            isInitiated = true;
        }
    }

    public Object[] getPossibleValues(int quantity){
        initValues();
        return getPossibleValuesInternal(quantity);
    }

    public Object[] getImpossibleValues(int quantity){
        initValues();
        return getImpossibleValuesInternal(quantity);
    }

    protected abstract void defineMandatoryValues();

    protected abstract Object[] getPossibleValuesInternal(int quantity);

    protected abstract Object[] getImpossibleValuesInternal(int quantity);

    public abstract long getMaxImpossibleQuantity();

    public abstract long getMaxPossibleQuantity();

    public int getMandatoryPossibleQuantity() {
        return mandatoryPossibleQuantity;
    }

    public int getMandatoryImpossibleQuantity() {
        return mandatoryImpossibleQuantity;
    }

    protected void setMandatoryPossibleQuantity(int mandatoryPossibleQuantity) {
        this.mandatoryPossibleQuantity = mandatoryPossibleQuantity;
    }

    protected void setMandatoryImpossibleQuantity(int mandatoryImpossibleQuantity) {
        this.mandatoryImpossibleQuantity = mandatoryImpossibleQuantity;
    }

    protected void increaseMandatoryPossibleQuantity(int value) {
        this.mandatoryPossibleQuantity += value;
    }

    protected void increaseMandatoryImpossibleQuantity(int value) {
        this.mandatoryImpossibleQuantity += value;
    }

    protected <T> Object[] getValuesFromSet(int quantity, Set<T> values) {
        if (quantity <= values.size()) {
            return values.stream().limit(quantity).toArray();
        } else {
            Object[] valuesForReturn = new Object[quantity];
            Object[] possibleValuesArray = values.toArray();
            int i = 0;
            int number = 0;
            while (number < quantity) {
                valuesForReturn[number] = possibleValuesArray[i];
                number++;
                i++;
                if (i == possibleValuesArray.length) {
                    i = 0;
                }
            }
            return valuesForReturn;
        }
    }
}
