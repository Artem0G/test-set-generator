package entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import types.NullIs;
import types.Range;

import java.util.stream.Stream;

public class IntParamTest {

    private static Stream<Arguments> getPossibleValuesData(){
        return Stream.of(
//                Arguments.of(false, NullIs.NOT_ALLOWED, new int[]{1, 3, 99, Integer.MAX_VALUE}, -1, new Object[0]), // -> ParameterTest
//                Arguments.of(false, NullIs.NOT_ALLOWED, new int[]{1, 3, 99, Integer.MAX_VALUE}, 0, new Object[0]), // -> ParameterTest
//                Arguments.of(false, NullIs.NOT_ALLOWED, new int[0], 1, new Object[0]), // -> testConstructor
                // values
                Arguments.of(false, NullIs.NOT_ALLOWED, new int[]{3}, 1, new Object[]{3}),
                Arguments.of(false, NullIs.NOT_ALLOWED, new int[]{3}, 2, new Object[]{3,3}),
                Arguments.of(false, NullIs.NOT_ALLOWED, new int[]{1, 3, 99, Integer.MAX_VALUE}, 1, new Object[]{1}),
                Arguments.of(false, NullIs.NEGATIVE, new int[]{1, 3, 99, Integer.MAX_VALUE}, 2, new Object[]{1, 3}),
                Arguments.of(false, NullIs.POSITIVE, new int[]{1, 3, 99, Integer.MAX_VALUE}, 4, new Object[]{1, 3, 99, Integer.MAX_VALUE}),
                Arguments.of(false, NullIs.NOT_ALLOWED, new int[]{1, 3, 99, Integer.MAX_VALUE}, 4, new Object[]{1, 3, 99, Integer.MAX_VALUE}),
                Arguments.of(false, NullIs.NEGATIVE, new int[]{1, 3, 99, Integer.MAX_VALUE}, 5, new Object[]{1, 3, 99, Integer.MAX_VALUE, 1}),
                Arguments.of(false, NullIs.NOT_ALLOWED, new int[]{1, 3, 99, Integer.MAX_VALUE}, 8, new Object[]{1, 3, 99, Integer.MAX_VALUE, 1, 3, 99, Integer.MAX_VALUE}),
                Arguments.of(false, NullIs.POSITIVE, new int[]{1, 3, 99, Integer.MAX_VALUE}, 12, new Object[]{1, 3, 99, Integer.MAX_VALUE, null, 1, 3, 99, Integer.MAX_VALUE, null, 1, 3}),
                Arguments.of(false, NullIs.NOT_ALLOWED, new int[]{-1, 3, -99, 0, Integer.MIN_VALUE}, 1, new Object[]{-1}),
                Arguments.of(false, NullIs.NEGATIVE, new int[]{-1, 3, -99, 0, Integer.MIN_VALUE}, 2, new Object[]{-1, 3}),
                Arguments.of(false, NullIs.NOT_ALLOWED, new int[]{-1, 3, -99, 0, Integer.MIN_VALUE}, 3, new Object[]{-1, 3, -99}),
                Arguments.of(false, NullIs.POSITIVE, new int[]{-1, 3, -99, 0, Integer.MIN_VALUE}, 5, new Object[]{-1, 3, -99, 0, Integer.MIN_VALUE}),
                Arguments.of(false, NullIs.NOT_ALLOWED, new int[]{-1, 3, -99, 0, Integer.MIN_VALUE}, 6, new Object[]{-1, 3, -99, 0, Integer.MIN_VALUE, -1}),
                Arguments.of(false, NullIs.POSITIVE, new int[]{-1, 3, -99, 0, Integer.MIN_VALUE}, 8, new Object[]{-1, 3, -99, 0, Integer.MIN_VALUE, null, -1, 3}),
                Arguments.of(false, NullIs.NEGATIVE, new int[]{-1, 3, -99, 0, Integer.MIN_VALUE}, 11, new Object[]{-1, 3, -99, 0, Integer.MIN_VALUE, -1, 3, -99, 0, Integer.MIN_VALUE, -1}),
                // ranges
                Arguments.of(true, NullIs.NOT_ALLOWED, new Range[]{new Range(Integer.MIN_VALUE, Integer.MAX_VALUE)}, 1, new Object[]{Integer.MIN_VALUE}),
                Arguments.of(true, NullIs.NOT_ALLOWED, new Range[]{new Range(Integer.MIN_VALUE, Integer.MAX_VALUE)}, 2, new Object[]{Integer.MIN_VALUE, Integer.MAX_VALUE}),
                Arguments.of(true, NullIs.NEGATIVE, new Range[]{new Range(Integer.MIN_VALUE, Integer.MAX_VALUE)}, 3, new Object[]{Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE + 1}),
                Arguments.of(true, NullIs.POSITIVE, new Range[]{new Range(Integer.MIN_VALUE, Integer.MAX_VALUE)}, 3, new Object[]{Integer.MIN_VALUE, Integer.MAX_VALUE, null}),
                Arguments.of(true, NullIs.POSITIVE, new Range[]{new Range(-300, -5)}, 5, new Object[]{-300, -5, null, -299, -298}),
                Arguments.of(true, NullIs.NEGATIVE, new Range[]{new Range(300, 1000)}, 5, new Object[]{300, 1000, 301, 302, 303}),
                Arguments.of(true, NullIs.NOT_ALLOWED, new Range[]{new Range(-1, 1)}, 4, new Object[]{-1, 1, 0, -1}),
                Arguments.of(true, NullIs.POSITIVE, new Range[]{new Range(28)}, 4, new Object[]{28, null, 28, null}),
                Arguments.of(true, NullIs.POSITIVE, new Range[]{new Range(13, 14), new Range(0), new Range(-2, 2), new Range(-300, -5)}, 8, new Object[]{-300, -5, 0, -2, 2, 13, 14, null}),
                Arguments.of(true, NullIs.POSITIVE, new Range[]{new Range(13, 14), new Range(0), new Range(-2, 2), new Range(-300, -298)}, 12, new Object[]{-300, -298, 0, -2, 2, 13, 14, null, -299, -1, 1, -300})
        );
    }

    private static Stream<Arguments> getImpossibleValuesData(){
        return Stream.of(
                // values
                Arguments.of(false, NullIs.NOT_ALLOWED, new int[]{3}, 1, new Object[]{2}),
                Arguments.of(false, NullIs.NOT_ALLOWED, new int[]{3}, 2, new Object[]{2, 4}),
                Arguments.of(false, NullIs.POSITIVE, new int[]{3}, 3, new Object[]{2, 4, Integer.MIN_VALUE}),
                Arguments.of(false, NullIs.NEGATIVE, new int[]{3}, 3, new Object[]{2, 4, null}),
                Arguments.of(false, NullIs.NEGATIVE, new int[]{3}, 5, new Object[]{2, 4, null, Integer.MIN_VALUE, Integer.MIN_VALUE + 1})
                // ranges
//                Arguments.of(true, NullIs.NOT_ALLOWED, new Range[]{new Range(Integer.MIN_VALUE, Integer.MAX_VALUE)}, 1, new Object[]{Integer.MIN_VALUE}),
        );
    }

    @ParameterizedTest
    @MethodSource("getPossibleValuesData")
    void testGetPossibleValues(boolean isRanges, NullIs nullIs, Object initValues, int quantity, Object[] expectedValue){
        Parameter parameter = initParameter(isRanges, nullIs, initValues);
        Assertions.assertArrayEquals(expectedValue, parameter.getPossibleValues(quantity));
    }

    @ParameterizedTest
    @MethodSource("getImpossibleValuesData")
    void getImpossibleValuesTest(boolean isRanges, NullIs nullIs, Object initValues, int quantity, Object[] expectedValue){
        Parameter parameter = initParameter(isRanges, nullIs, initValues);
        Assertions.assertArrayEquals(expectedValue, parameter.getImpossibleValues(quantity));
    }

    @Test
    void getMandatoryPossibleQuantityTest(){

    }

    @Test
    void getMandatoryImpossibleQuantity(){

    }

    @Test
    void getMaxPossibleQuantityTest(){

    }

    @Test
    void getMaxImpossibleQuantityTest(){

    }

    private Parameter initParameter(boolean isRanges, NullIs nullIs, Object initValues) {
        Parameter parameter;
        if (isRanges) {
            Range[] ranges = (Range[]) initValues;
            if (ranges.length == 1) {
                parameter = new IntParam(nullIs, ranges[0]);
            } else {
                parameter = new IntParam(nullIs, ranges);
            }
        } else {
            int[] values = (int[]) initValues;
            if (values.length == 1) {
                parameter = new IntParam(nullIs, values[0]);
            } else {
                parameter = new IntParam(nullIs, values);
            }
        }
        return parameter;
    }
}
