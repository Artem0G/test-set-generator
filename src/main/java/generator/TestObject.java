package generator;

import entities.IntParam;
import entities.Parameter;
import entities.StringParam;
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
        NullIs nullIs = canBeNull ? NullIs.POSITIVE : NullIs.NEGATIVE;
        addIntParam(new IntParam(nullIs, new Range(Integer.MIN_VALUE, Integer.MAX_VALUE)));
        return this;
    }

    public TestObject addInteger(boolean canBeNull, Range...ranges) {
        NullIs nullIs = canBeNull ? NullIs.POSITIVE : NullIs.NEGATIVE;
        addIntParam(new IntParam(nullIs, ranges));
        return this;
    }

    public TestObject addInteger(boolean canBeNull, int...args) {
        NullIs nullIs = canBeNull ? NullIs.POSITIVE : NullIs.NEGATIVE;
        addIntParam(new IntParam(nullIs, args));
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

    public Stream<Object[]> generatePositiveTestCases() {
        return generatePositiveTestCases(0);
    }

    public Stream<Object[]> generatePositiveTestCases(int quantity) {
        int minRequired = getMaxFromMandatoryRequiredPositive();
        if (quantity > 0 && quantity < minRequired) {
            System.err.println(String.format("Warning!!! Test cases quantity (%d) is less than required (%d) for good coverage", quantity, minRequired));
        } else if (quantity <= 0) {
            quantity = minRequired;
        }
        Object[][] positiveParamValues = new Object[params.size()][];
        for (int i = 0; i < params.size(); i++) {
            Object[] possibleValues = params.get(i).getPossibleValues(quantity);
            positiveParamValues[i] = possibleValues;
        }
        return Stream.of(ArrayHelper.transposeMatrix(positiveParamValues));
    }

    public Stream<Object[]> generateNegativeTestCases() {
        return generateNegativeTestCases(0);
    }

    public Stream<Object[]> generateNegativeTestCases(int quantity) {
        List<Object[][]> testCases = new ArrayList<>();
        int numberOfTests = 0;
        for (int i = 0; i < params.size(); i++) {
            Parameter paramForValidation = params.get(i);
            int minRequired = paramForValidation.getMandatoryImpossibleQuantity();
            int numberOfRequiredTests = quantity;
            if (quantity > 0 && quantity < minRequired) {
                System.err.println(String.format("Warning!!! Test cases quantity (%d) for parameter #%d is less than required (%d) for good coverage", quantity, i+1, minRequired));
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
        int index = 0;
        for (Object[][] testCasesForCurrentParam : testCases) {
            ArrayHelper.transposeMatrix(testCasesForCurrentParam, finalTestCases, index);
            index += testCasesForCurrentParam[0].length;
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



}
