package generator;

import entities.IntParam;
import entities.Parameter;
import entities.StringParam;
import types.Range;
import types.StringType;
import utils.ArrayHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


public class TestObject {
    private List<Parameter> params = new ArrayList<>();

    //TODO: Integer, nullable?

    public TestObject addInt() {
        addIntParam(new IntParam(false, new Range(Integer.MIN_VALUE, Integer.MAX_VALUE)));
        return this;
    }

    public TestObject addInt(Range range) {
        addIntParam(new IntParam(false, range));
        return this;
    }

    public TestObject addInt(Range...ranges) {
        addIntParam(new IntParam(false, ranges));
        return this;
    }

    public TestObject addInt(int...args) {
        addIntParam(new IntParam(false, args));
        return this;
    }

    private void addIntParam(IntParam newParam) {
        newParam.initValues();
        params.add(newParam);
    }

    public TestObject addString(StringType stringType, Range range) {
        Parameter newParam = new StringParam(false, stringType, range);
        newParam.initValues();
        params.add(newParam);
        return this;
    }

    public Stream<Object[]> generatePositiveTestCases() {
        return generatePositiveTestCases(0);
    }

    public Stream<Object[]> generatePositiveTestCases(int maxQuantity) {
        int minRequired = getMaxFromMandatoryRequiredPositive();
        if (maxQuantity > 0 && maxQuantity < minRequired) {
            System.err.println(String.format("Warning!!! Maximum test cases quantity (%d) is less than required (%d) for good coverage", maxQuantity, minRequired));
        } else if (maxQuantity <= 0) {
            maxQuantity = minRequired;
        }
        Object[][] positiveParamValues = new Object[params.size()][];
        for (int i = 0; i < params.size(); i++) {
            Object[] negativeValues = params.get(i).getPossibleValues(maxQuantity);
            positiveParamValues[i] = negativeValues;
        }
        return Stream.of(ArrayHelper.transposeMatrix(positiveParamValues));
    }

    public Stream<Object[]> generateNegativeTestCases() {
        return generateNegativeTestCases(0);
    }

    public Stream<Object[]> generateNegativeTestCases(int maxQuantity) {
        List<Object[][]> testCases = new ArrayList<>();
        int numberOfTests = 0;
        for (int i = 0; i < params.size(); i++) {
            Parameter paramForValidation = params.get(i);
            int minRequired = paramForValidation.getMandatoryImpossibleQuantity();
            int numberOfRequiredTests = maxQuantity;
            if (maxQuantity > 0 && maxQuantity < minRequired) {
                System.err.println(String.format("Warning!!! Maximum test cases quantity (%d) for parameter #%d is less than required (%d) for good coverage", maxQuantity, i+1, minRequired));
            } else if (maxQuantity <= 0) {
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
