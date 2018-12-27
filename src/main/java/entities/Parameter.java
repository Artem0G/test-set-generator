package entities;

public abstract class Parameter {
    private boolean isNullable;

    public Parameter(boolean isNullable) {
        this.isNullable = isNullable;
    }

    public boolean isNullable() {
        return isNullable;
    }

    public abstract Object[] getPositiveData(int numberOfNegativeTests);

    public abstract Object[] getNegativeData(int numberOfNegativeTests);

    public abstract int getMinRequiredNegativeQuantity();

    public abstract int getMaxNegativeQuantity();

    public abstract int getMinRequiredPositiveQuantity();

    public abstract int getMaxPositiveQuantity();

}
