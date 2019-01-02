package entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import types.NullIs;
import types.Range;

import java.util.stream.Stream;

public class IntParamTest {

    private static Stream<Arguments> getPossibleValuesData(){
        return Stream.of(
                Arguments.of(false, NullIs.NOT_ALLOWED, new int[]{1, 3, 99, Integer.MAX_VALUE}, 0, new Object[0]),
                Arguments.of(false, NullIs.NOT_ALLOWED, new int[]{1, 3, 99, Integer.MAX_VALUE}, 4, new Object[]{1, 3, 99, Integer.MAX_VALUE})
        );
    }


    @ParameterizedTest
    @MethodSource("getPossibleValuesData")
    void getPossibleValuesTest(boolean isRanges, NullIs nullIs, Object initValues, int quantity, Object[] expectedValue){
        Parameter parameter;
        if (isRanges) {
            parameter = new IntParam(nullIs, (Range[]) initValues);
        } else {
            parameter = new IntParam(nullIs, (int[]) initValues);
        }
        Assertions.assertArrayEquals(expectedValue, parameter.getPossibleValues(quantity));
    }

    @Test
    void getImpossibleValuesTest(){

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
}
