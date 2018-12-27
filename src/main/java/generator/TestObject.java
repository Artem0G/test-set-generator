package generator;

import entities.IntParam;
import entities.Parameter;
import entities.StringParam;
import types.Range;
import types.StringType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;



public class TestData {
    private List<Parameter> params;

    public TestData addInt(Range range) {
        params.add(new IntParam(false, range));
        return this;
    }

    public TestData addInt(int...args) {
        params.add(new IntParam(false, args));
        return this;
    }

    public TestData addString(StringType stringType, Range range) {
        params.add(new StringParam(false, stringType, range));
        return this;
    }

    public Stream<Object[]> generatePositiveStreamOfArguments(int limit) {
        params

        return Stream.of(

        );
    }

    public Stream<Object[]> generateNegativeStreamOfValues(int argumentsNumber) {
        List<List<Object>> negativeParamsValues = new ArrayList<>();
        for (Parameter parameter : params) {
            parameter.getNegativeValues(argumentsNumber)
        }


        return Stream.of(
                new Object[] {},
                new Object[] {}
        );
    }

}
