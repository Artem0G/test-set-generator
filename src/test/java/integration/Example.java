package integration;

import generator.TestObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import types.Range;
import types.StringType;

import java.util.stream.IntStream;
import java.util.stream.Stream;

class Example {
    private class SomeClass{
        boolean someMethod(int int1, int int2/*, String string*/) {
            if (int1 > 5 && int1 <= 15) {
                return true;
            } else
                return IntStream.of(1, 3, 5, 55).anyMatch(i -> i == int2)/* && string.length() > 0 && string.length() <= 10 && string.matches("^[a-zA-Z0-9_]*$")*/;
        }
    }

    private static final TestObject TEST_OBJECT_CASE_1 = new TestObject();
    private static final TestObject TEST_OBJECT_CASE_2 = new TestObject();


    @BeforeAll
    private static void defineData(){
        TEST_OBJECT_CASE_1.addInt(new Range(6,15)).addInt()/*.addString(StringType.ALPHANUMERIC, new Range(1, 10))*/;
        TEST_OBJECT_CASE_2.addInt().addInt(1,3,5,55)/*.addString(StringType.ALPHANUMERIC, new Range(1, 10))*/;
    }

    private static Stream<Arguments> goodData() {
        return Stream.concat(
                TEST_OBJECT_CASE_1.generatePositiveTestCases().map(Arguments::of),
                TEST_OBJECT_CASE_2.generatePositiveTestCases().map(Arguments::of));
    }

    private static Stream<Arguments> badData() {
        return Stream.concat(
                TEST_OBJECT_CASE_1.generateNegativeTestCases().map(Arguments::of),
                TEST_OBJECT_CASE_2.generateNegativeTestCases().map(Arguments::of));
    }

    @ParameterizedTest
    @MethodSource("goodData")
    void testTrue(int int1, int int2/*, String string*/) {
        SomeClass clazz = new SomeClass();
        Assertions.assertTrue(clazz.someMethod(int1, int2/*, string*/));
    }


    @ParameterizedTest
    @MethodSource("badData")
    void testFalse(int int1, int int2/*, String string*/) {
        SomeClass clazz = new SomeClass();
        Assertions.assertFalse(clazz.someMethod(int1, int2/*, string*/));
    }






}
