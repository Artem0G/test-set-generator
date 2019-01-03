package generator;

import entities.IntParam;
import entities.Parameter;
import entities.StringParam;
import exceptions.PoorCoverageException;
import types.NullIs;
import types.Range;
import types.StringType;
import utils.ArrayHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


public class TestObject {
    private List<Parameter> params = new ArrayList<>();

    public TestObject addInteger(boolean canBeNull) {
        addIntParam(new IntParam(getNullIs(canBeNull), new Range(Integer.MIN_VALUE, Integer.MAX_VALUE)));
        return this;
    }

    public TestObject addInteger(boolean canBeNull, Range...ranges) {
        addIntParam(new IntParam(getNullIs(canBeNull), ranges));
        return this;
    }

    public TestObject addInteger(boolean canBeNull, int...args) {
        addIntParam(new IntParam(getNullIs(canBeNull), args));
        return this;
    }

    public TestObject addInt() {
        addIntParam(new IntParam(NullIs.NOT_ALLOWED, new Range(Integer.MIN_VALUE, Integer.MAX_VALUE)));
        return this;
    }

    public TestObject addInt(Range...ranges) {
        addIntParam(new IntParam(NullIs.NOT_ALLOWED, ranges));
        return this;
    }

    public TestObject addInt(int...args) {
        addIntParam(new IntParam(NullIs.NOT_ALLOWED, args));
        return this;
    }

    public TestObject addString(StringType stringType, Range range) {
        Parameter newParam = new StringParam(NullIs.NOT_ALLOWED, stringType, range);
        newParam.initValues();
        params.add(newParam);
        return this;
    }

    private void addIntParam(IntParam newParam) {
        newParam.initValues();
        params.add(newParam);
    }

    public Stream<Object[]> generatePositiveTestCases() throws PoorCoverageException {
        return generatePositiveTestCases(0);
    }

    public Stream<Object[]> generatePositiveTestCases(int quantity) throws PoorCoverageException {
        int minRequired = getMaxFromMandatoryRequiredPositive();
        if (quantity > 0 && quantity < minRequired) {
            throw new PoorCoverageException(String.format("Requested quantity: %d, minimum required quantity: %d", quantity, minRequired));
        } else if (quantity <= 0) {
            quantity = minRequired;
        }
        Object[][] positiveValues = new Object[params.size()][];
        for (int i = 0; i < params.size(); i++) {
            positiveValues[i] = params.get(i).getPossibleValues(quantity);
        }
        return Stream.of(ArrayHelper.transposeMatrix(positiveValues));
    }

    public Stream<Object[]> generateNegativeTestCases() throws PoorCoverageException {
        return generateNegativeTestCases(0);
    }

    public Stream<Object[]> generateNegativeTestCases(int quantity) throws PoorCoverageException {
        List<Object[][]> testCases = new ArrayList<>();
        int numberOfTests = 0;
        for (int i = 0; i < params.size(); i++) {
            Parameter paramForValidation = params.get(i);
            int minRequired = paramForValidation.getMandatoryImpossibleQuantity();
            int numberOfRequiredTests = quantity;
            if (quantity > 0 && quantity < minRequired) {
                throw new PoorCoverageException(String.format("Parameter number: %d, requested quantity: %d, minimum required quantity: %d", i, quantity, minRequired));
            } else if (quantity <= 0) {
                numberOfRequiredTests = minRequired;
            }
            numberOfTests += numberOfRequiredTests;
            Object[][] testCasesForCurrentParam = new Object[params.size()][];
            for (int j = 0; j < params.size(); j++) {
                Object[] paramValues = j == i ? paramForValidation.getImpossibleValues(numberOfRequiredTests) : params.get(j).getPossibleValues(numberOfRequiredTests);
                testCasesForCurrentParam[j] = paramValues;
            }
            testCases.add(testCasesForCurrentParam);
        }

        Object[][] finalTestCases = new Object[numberOfTests][params.size()];
        int startIndex = 0;
        for (Object[][] testCasesForCurrentParam : testCases) {
            ArrayHelper.transposeMatrix(testCasesForCurrentParam, finalTestCases, startIndex);
            startIndex += testCasesForCurrentParam[0].length;
        }

        return Stream.of(finalTestCases);
    }

    private int getMaxFromMandatoryRequiredPositive() {
        int max = 0;
        for (Parameter param : params) {
            max = Math.max(max, param.getMandatoryPossibleQuantity());
        }
        return max;
    }

    private NullIs getNullIs(boolean canBeNull) {
        return canBeNull ? NullIs.POSITIVE : NullIs.NEGATIVE;
    }
}
