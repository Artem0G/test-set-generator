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
    private long maxPossibleValues = -1;
    private long maxImpossibleValues = -1;

    public IntParam(NullIs nullIs, Range...ranges) {
        super(nullIs);
        this.ranges = Arrays.asList(ranges);
        possibleValues = new LinkedHashSet<>();
    }

    public IntParam(NullIs nullIs, int...values) {
        super(nullIs);
        possibleValues = Arrays.stream(values).boxed().collect(Collectors.toCollection(LinkedHashSet::new));
        maxPossibleValues = values.length;
        setMandatoryPossibleQuantity(values.length);
        ranges = new ArrayList<>();
    }

    @Override
    protected void defineMandatoryValues() {
        defineImpossibleMandatoryValuesFromPossible();
        ranges.sort((left, right) -> right.getStart() - left.getEnd());
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
        if (quantity > impossibleValues.size() && !ranges.isEmpty()) {
            extractRestImpossibleFromRanges(quantity);
        }
        return getImpossibleFromValues(quantity);
    }

    @Override
    public long getMaxImpossibleQuantity() {
        if (maxImpossibleValues < 0) {
            maxImpossibleValues = (long) Integer.MAX_VALUE - Integer.MIN_VALUE + 1 - getMaxPossibleQuantity();
        }
        return maxImpossibleValues;
    }

    @Override
    public long getMaxPossibleQuantity() {
        if (maxPossibleValues < 0) {
            maxPossibleValues = ranges.stream().mapToInt(Range::getSize).sum();
        }
        return maxPossibleValues;
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
        List<Integer> restValues = new ArrayList<>(restQuantity);
        for (Range range : ranges) {
            if (numberOfUncheckedDefinedNotMandatoryValues >= range.getInnerSize()) {
                numberOfUncheckedDefinedNotMandatoryValues -= range.getInnerSize();
                continue;
            }
            List<Integer> rangeValues = IntStream.range(range.getStart() + 1,range.getEnd())
                    .skip(numberOfUncheckedDefinedNotMandatoryValues)
                    .limit((long) quantity - numberOfDefinedNotMandatoryValues)
                    .boxed()
                    .collect(Collectors.toList());
            numberOfDefinedNotMandatoryValues += rangeValues.size();
            restValues.addAll(rangeValues);
            if (numberOfDefinedNotMandatoryValues >= quantity) {
                break;
            }
        }
        possibleValues.addAll(restValues);
    }

    private void extractRestImpossibleFromRanges(int quantity) {
        int restQuantity = quantity - impossibleValues.size();
        int numberOfDefinedNotMandatoryValues = impossibleValues.size() - getMandatoryImpossibleQuantity();
        int numberOfUncheckedDefinedNotMandatoryValues = numberOfDefinedNotMandatoryValues;
        List<Integer> restValues = new ArrayList<>(restQuantity);
        int lastMin = Integer.MIN_VALUE;
        for (Range range : ranges) {
            int numberOfAvailableValues = range.getStart() - 1 - lastMin;
            if (numberOfUncheckedDefinedNotMandatoryValues >= numberOfAvailableValues) {
                numberOfUncheckedDefinedNotMandatoryValues -= numberOfAvailableValues;
                continue;
            }
            List<Integer> rangeValues = IntStream.range(lastMin,range.getStart() - 1)
                    .skip(numberOfUncheckedDefinedNotMandatoryValues)
                    .limit((long) quantity - numberOfDefinedNotMandatoryValues)
                    .boxed()
                    .collect(Collectors.toList());
            numberOfDefinedNotMandatoryValues += numberOfAvailableValues;
            lastMin = range.getEnd() <= Integer.MAX_VALUE - 2 ? range.getEnd() + 2 : Integer.MAX_VALUE;
            restValues.addAll(rangeValues);
            if (numberOfDefinedNotMandatoryValues >= quantity) {
                break;
            }

        }
        if (numberOfDefinedNotMandatoryValues < quantity && lastMin < Integer.MAX_VALUE) {
            restValues.addAll(IntStream.rangeClosed(lastMin,Integer.MAX_VALUE)
                    .skip(numberOfUncheckedDefinedNotMandatoryValues)
                    .limit((long) quantity - numberOfDefinedNotMandatoryValues)
                    .boxed()
                    .collect(Collectors.toList()));
        }
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
        possibleValues.forEach(value ->{
            if (value > Integer.MIN_VALUE) {
                addMandatoryImpossibleValue(value - 1);
            }
            if (value < Integer.MAX_VALUE) {
                addMandatoryImpossibleValue(value + 1);
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
