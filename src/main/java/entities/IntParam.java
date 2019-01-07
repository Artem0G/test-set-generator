package entities;

import types.NullIs;
import types.Range;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IntParam extends Parameter {
    private List<Range> ranges;
    private Set<Integer> possibleValues;
    private Set<Integer> impossibleValues;

    public IntParam(NullIs nullIs, Range...ranges) {
        super(nullIs);
        if (ranges.length == 0) {
            throw new IllegalArgumentException("At least one Range should be specified");
        }
        this.ranges = Arrays.asList(ranges);
        possibleValues = new LinkedHashSet<>();
    }

    public IntParam(NullIs nullIs, int...values) {
        super(nullIs);
        if (values.length == 0) {
            throw new IllegalArgumentException("At least one values should be specified");
        }
        possibleValues = Arrays.stream(values).boxed().collect(Collectors.toCollection(LinkedHashSet::new));
        setMandatoryPossibleQuantity(values.length);
        ranges = new ArrayList<>();
    }

    @Override
    protected void defineMandatoryValues() {
        defineImpossibleMandatoryValuesFromPossible();
        ranges.sort((left, right) -> left.getEnd() - right.getStart());
        defineAllMandatoryValuesFromRanges();
        if (getNullIs() == NullIs.POSITIVE) {
            addMandatoryPossibleValue(null);
        } else if (getNullIs() == NullIs.NEGATIVE) {
            addMandatoryImpossibleValue(null);
        }
    }

    @Override
    protected Object[] getPossibleValuesInternal(int quantity) {
        if (quantity > possibleValues.size() && !ranges.isEmpty()) {
            extractRestPossibleFromRanges(quantity);
        }
        return getPossibleFromValues(quantity);
    }

    @Override
    protected Object[] getImpossibleValuesInternal(int quantity) {
        if (quantity > impossibleValues.size()) {
            if (!ranges.isEmpty()) {
                extractRestImpossibleFromRanges(quantity);
            } else {
                extractRestImpossibleFromValues(quantity);
            }
        }
        return getImpossibleFromValues(quantity);
    }

    private Object[] getPossibleFromValues(int quantity) {
        return getValuesFromSet(quantity, possibleValues);
    }

    private Object[] getImpossibleFromValues(int quantity) {
        return getValuesFromSet(quantity, impossibleValues);
    }

    private void extractRestPossibleFromRanges(int quantity) {
        int restQuantity = quantity - possibleValues.size();
        int numberOfDefinedNotMandatoryValues = possibleValues.size() - getMandatoryPossibleQuantity();
        int numberOfUncheckedDefinedNotMandatoryValues = numberOfDefinedNotMandatoryValues;
        List<Integer> restValues = new ArrayList<>();
        for (Range range : ranges) {
            if (numberOfUncheckedDefinedNotMandatoryValues >= range.getInnerSize()) {
                numberOfUncheckedDefinedNotMandatoryValues -= range.getInnerSize();
                continue;
            }
            List<Integer> rangeValues = IntStream.range(range.getStart() + 1,range.getEnd())
                    .skip(numberOfUncheckedDefinedNotMandatoryValues)
                    .limit((long) restQuantity - numberOfDefinedNotMandatoryValues)
                    .boxed()
                    .collect(Collectors.toList());
            numberOfDefinedNotMandatoryValues += rangeValues.size();
            restValues.addAll(rangeValues);
            if (numberOfDefinedNotMandatoryValues >= restQuantity) {
                break;
            }
        }
        possibleValues.addAll(restValues);
    }

    private void extractRestImpossibleFromRanges(int quantity) {
        int restQuantity = quantity - impossibleValues.size();
        int numberOfDefinedNotMandatoryValues = impossibleValues.size() - getMandatoryImpossibleQuantity();
        int numberOfUncheckedDefinedNotMandatoryValues = numberOfDefinedNotMandatoryValues;
        Set<Integer> restValues = new LinkedHashSet<>();
        int lastMin = Integer.MIN_VALUE;
        for (Range range : ranges) {
            int numberOfAvailableValues = range.getStart() - 1 - lastMin;
            if (numberOfUncheckedDefinedNotMandatoryValues >= numberOfAvailableValues) {
                numberOfUncheckedDefinedNotMandatoryValues -= numberOfAvailableValues;
                lastMin = range.getEnd() <= Integer.MAX_VALUE - 2 ? range.getEnd() + 2 : Integer.MAX_VALUE;
                continue;
            }
            List<Integer> rangeValues = IntStream.range(lastMin,range.getStart() - 1)
                    .skip(numberOfUncheckedDefinedNotMandatoryValues)
                    .limit((long) restQuantity - numberOfDefinedNotMandatoryValues)
                    .boxed()
                    .collect(Collectors.toList());
            numberOfDefinedNotMandatoryValues += numberOfAvailableValues;
            lastMin = range.getEnd() <= Integer.MAX_VALUE - 2 ? range.getEnd() + 2 : Integer.MAX_VALUE;
            restValues.addAll(rangeValues);
            if (numberOfDefinedNotMandatoryValues >= restQuantity) {
                break;
            }

        }
        if (numberOfDefinedNotMandatoryValues < restQuantity && lastMin < Integer.MAX_VALUE) {
            restValues.addAll(IntStream.rangeClosed(lastMin,Integer.MAX_VALUE)
                    .skip(numberOfUncheckedDefinedNotMandatoryValues)
                    .limit((long) restQuantity - numberOfDefinedNotMandatoryValues)
                    .boxed()
                    .collect(Collectors.toList()));
        }
        impossibleValues.addAll(restValues);
    }

    private void extractRestImpossibleFromValues(int quantity) {
        int restQuantity = quantity - impossibleValues.size();
        int numberOfDefinedNotMandatoryValues = impossibleValues.size() - getMandatoryImpossibleQuantity();
        List<Integer> restValues = IntStream.rangeClosed(Integer.MIN_VALUE, Integer.MAX_VALUE)
                .filter(value->!possibleValues.contains(value) && !impossibleValues.contains(value))
                .skip(numberOfDefinedNotMandatoryValues)
                .limit((long) restQuantity - numberOfDefinedNotMandatoryValues)
                .boxed()
                .collect(Collectors.toList());
        impossibleValues.addAll(restValues);
    }

    private void defineAllMandatoryValuesFromRanges() {
        // If this object was created with ranges then ranges isn't empty
        for (Range range : ranges) {
            addMandatoryPossibleValue(range.getStart());
            addMandatoryPossibleValue(range.getEnd());
            if (range.getStart() > Integer.MIN_VALUE) {
                addMandatoryImpossibleValue(range.getStart() - 1);
            }
            if (range.getEnd() < Integer.MAX_VALUE) {
                addMandatoryImpossibleValue(range.getEnd() + 1);
            }
        }
    }

    private void defineImpossibleMandatoryValuesFromPossible() {
        impossibleValues = new LinkedHashSet<>();
        // If this object was created with possibleValues then possibleValues isn't empty
        possibleValues.forEach(possibleValue ->{
            if (possibleValue > Integer.MIN_VALUE && !possibleValues.contains(possibleValue - 1)) {
                addMandatoryImpossibleValue(possibleValue - 1);
            }
            if (possibleValue < Integer.MAX_VALUE && !possibleValues.contains(possibleValue + 1)) {
                addMandatoryImpossibleValue(possibleValue + 1);
            }
        });
    }

    private void addMandatoryPossibleValue(Integer value) {
        if (possibleValues.add(value)) {
            increaseMandatoryPossibleQuantity(1);
        }
    }

    private void addMandatoryImpossibleValue(Integer value) {
        if (impossibleValues.add(value)) {
            increaseMandatoryImpossibleQuantity(1);
        }
    }

}
