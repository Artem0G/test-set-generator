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

    public TestObject addInt(Range range) {
        params.add(new IntParam(false, range));
        return this;
    }

    public TestObject addInt(int...args) {
        params.add(new IntParam(false, args));
        return this;
    }

    public TestObject addString(StringType stringType, Range range) {
        params.add(new StringParam(false, stringType, range));
        return this;
    }

    public Stream<Object[]> generatePositiveTestCases() {
        return generatePositiveTestCases(0);
    }

    public Stream<Object[]> generatePositiveTestCases(int maxQuantity) {
        int minRequired = getMaxFromMinRequiredPositive();
        if (maxQuantity > 0 && maxQuantity < minRequired) {
            System.err.println(String.format("Warning!!! Maximum test cases quantity (%d) is less than required (%d) for good coverage", maxQuantity, minRequired));
        }
        int numOfTestCases = Math.min(maxQuantity, getMaxFromMaxPositive());
        Object[][] positiveParamValues = new Object[params.size()][];
        for (int i = 0; i < params.size(); i++) {
            Object[] negativeValues = params.get(i).getPositiveData(numOfTestCases);
            positiveParamValues[i] = negativeValues;
        }
        return Stream.of(ArrayHelper.transposeMatrix(positiveParamValues));
    }


    public Stream<Object[]> generateNegativeTestCases(int maxQuantity) {
        List<Object[][]> testCases = new ArrayList<>();
        int numberOfTests = 0;
        for (int i = 0; i < params.size(); i++) {
            Parameter paramForValidation = params.get(i);
            int minRequired = paramForValidation.getMinRequiredNegativeQuantity();
            if (maxQuantity > 0 && maxQuantity < minRequired) {
                System.err.println(String.format("Warning!!! Maximum test cases quantity (%d) for parameter #%d is less than required (%d) for good coverage", maxQuantity, i+1, minRequired));
            }
            int numberOfNegativeTests = Math.min(maxQuantity, paramForValidation.getMaxNegativeQuantity());
            numberOfTests += numberOfNegativeTests;
            Object[][] testCasesForCurrentParam = new Object[params.size()][];
            for (int j = 0; j < params.size(); j++) {
                Object[] paramValues = j == i ? paramForValidation.getNegativeData(numberOfNegativeTests) : paramForValidation.getPositiveData(numberOfNegativeTests);
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

    private int getMaxFromMaxPositive() {
        int max = 0;
        for (Parameter param : params) {
            max = Math.max(max, param.getMaxPositiveQuantity());
        }
        return max;
    }

    private int getMaxFromMinRequiredPositive() {
        int max = 0;
        for (Parameter param : params) {
            max = Math.max(max, param.getMinRequiredPositiveQuantity());
        }
        return max;
    }



}
