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
        boolean someMethod(int int1, int int2, String string) {
            if (int1 > 5 && int1 <= 15) {
                return false;
            } else
                return IntStream.of(1, 3, 5, 55).anyMatch(i -> i == int2) && string.length() > 0 && string.length() <= 10 && string.matches("^[a-zA-Z0-9_]*$");
        }
    }

    private final static TestObject TEST_OBJECT = new TestObject();

    @BeforeAll
    private static void defineData(){
        TEST_OBJECT.addInt(new Range(6,15)).addInt(1,3,5,55).addString(StringType.ALPHANUMERIC, new Range(1, 10));
    }

    private static Stream<Arguments> goodData() {
        return TEST_OBJECT.generatePositiveTestCases().map(Arguments::of);
    }

    private static Stream<Arguments> badData() {
        return TEST_OBJECT.generateNegativeTestCases(20).map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("goodData")
    void testTrue(int int1, int int2, String string) {
        SomeClass clazz = new SomeClass();
        Assertions.assertTrue(clazz.someMethod(int1, int2, string));
    }


    @ParameterizedTest
    @MethodSource("badData")
    void testFalse(int int1, int int2, String string) {
        SomeClass clazz = new SomeClass();
        Assertions.assertFalse(clazz.someMethod(int1, int2, string));
    }






}
