package types;

public class Range {
    private int start;
    private int end;

    public Range(int start, int end) {
        if (end <= start) {
            throw new IllegalArgumentException("The end value less than (or equal to) the start value");
        }
        this.start = start;
        this.end = end;
    }

    public Range(int singleValue) {
        this.start = singleValue;
        this.end = singleValue;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int getSize() {
        return end - start + 1;
    }

    public int getInnerSize() {
        if (end == start) {
            return 0;
        } else {
            return end - start - 1;
        }
    }

    public boolean contains(int number) {
        return number >= start && number <= end;
    }
}
