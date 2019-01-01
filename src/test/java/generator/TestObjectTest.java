package generator;

import entities.Parameter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestObjectTest {
    private TestObject testObject;

    @BeforeEach
    void init(){
        testObject = new TestObject();
    }

    private static Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of(new int[]{1, 2, 3}, 5, 5),
                Arguments.of(new int[]{1, 2, 3, 3}, 5, 5),
                Arguments.of(new int[]{6, 3, 4}, 1, 1),
                Arguments.of(new int[]{5}, 5, 5),
                Arguments.of(new int[]{0}, 5, 5),
                Arguments.of(new int[]{0, 2}, 10, 10),
                Arguments.of(new int[]{0, 2, 0}, 10, 10),
                Arguments.of(new int[]{1, 2, 10}, 0, 10),
                Arguments.of(new int[]{1, 2, 9, 10}, 0, 10)
        );
    }

    @ParameterizedTest
    @MethodSource("testData")
    void generatePositiveTestCasesTest(int[] getMandatoryPossibleQuantityValues, int quantitySet, int quantityExpected) throws NoSuchFieldException, IllegalAccessException {
        List<Parameter> parameters = new ArrayList<>(getMandatoryPossibleQuantityValues.length);
        for (int getMandatoryPossibleQuantityValue : getMandatoryPossibleQuantityValues) {
            Parameter param = Mockito.mock(Parameter.class);
            when(param.getPossibleValues(anyInt())).thenAnswer(i -> generateMocks((int) i.getArguments()[0], param.toString()));
            when(param.getMandatoryPossibleQuantity()).thenReturn(getMandatoryPossibleQuantityValue);
            parameters.add(param);
        }
        Field params = testObject.getClass().getDeclaredField("params");
        params.setAccessible(true);
        params.set(testObject, parameters);
        List<Object[]> result = testObject.generatePositiveTestCases(quantitySet).collect(Collectors.toList());
        Assertions.assertEquals(quantityExpected, result.size(), "Number of positive test cases should be equal to requested quantity");
        result.forEach(testCase -> {
            Assertions.assertEquals(parameters.size(), testCase.length, "Each test case should contain specified number of parameters");
            for (int i = 0; i < testCase.length; i++) {
                Assertions.assertEquals("Belong to " + parameters.get(i).toString(), testCase[i].toString(), "Value number #" + i +" should belong to parameter #" + i);
            }
        });

    }

    @ParameterizedTest
    @MethodSource("testData")
    void generateNegativeTestCasesTest(int[] getMandatoryImpossibleQuantityValues, int quantitySet, int quantityExpected) throws NoSuchFieldException, IllegalAccessException {
        List<Parameter> parameters = new ArrayList<>(getMandatoryImpossibleQuantityValues.length);
        for (int getMandatoryImpossibleQuantityValue : getMandatoryImpossibleQuantityValues) {
            Parameter param = Mockito.mock(Parameter.class);
            Mockito.lenient().when(param.getPossibleValues(anyInt())).thenAnswer(i -> generateMocks((int) i.getArguments()[0], param.toString()));
            Mockito.lenient().when(param.getImpossibleValues(anyInt())).thenAnswer(i -> generateMocks((int) i.getArguments()[0], param.toString()));
            when(param.getMandatoryImpossibleQuantity()).thenReturn(getMandatoryImpossibleQuantityValue);
            parameters.add(param);
        }
        Field params = testObject.getClass().getDeclaredField("params");
        params.setAccessible(true);
        params.set(testObject, parameters);
        List<Object[]> result = testObject.generateNegativeTestCases(quantitySet).collect(Collectors.toList());
        if (quantitySet > 0) {
            Assertions.assertEquals(quantityExpected * getMandatoryImpossibleQuantityValues.length, result.size(), "Number of negative test cases should be equal to requested quantity * number of parameters");
        } else {
            Assertions.assertEquals(sumOfArray(getMandatoryImpossibleQuantityValues), result.size(),"Number of negative test cases should be equal to sum of getMandatoryImpossibleQuantityValues");
        }
        result.forEach(testCase -> {
            Assertions.assertEquals(parameters.size(), testCase.length, "Each test case should contain specified number of parameters");
            for (int i = 0; i < testCase.length; i++) {
                Assertions.assertEquals("Belong to " + parameters.get(i).toString(), testCase[i].toString(), "Value number #" + i +" should belong to parameter #" + i);
            }
        });

    }



    private Object[] generateMocks(int quantity, String parent) {
        Object[] retValue = new Object[quantity];
        for (int i = 0; i < quantity; i++ ) {
            retValue[i] = Mockito.mock(Object.class);
            when(retValue[i].toString()).thenReturn("Belong to " + parent);
        }
        return retValue;
    }

    private int sumOfArray(int[] intArray) {
        int sum = 0;
        for (int value : intArray) {
            sum += value;
        }
        return sum;
    }
}
