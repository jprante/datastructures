package org.xbib.datastructures.trie.ahocorasick;

import java.util.Objects;

/**
 * Responsible for tracking the start and end bounds.
 */
public class Interval implements Comparable<Interval> {

    private final int start;

    private final int end;

    public Interval(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public boolean overlapsWith(final Interval other) {
        return start <= other.getEnd() && end >= other.getStart();
    }

    public boolean overlapsWith(int point) {
        return start <= point && point <= end;
    }

    @Override
    public int compareTo(Interval other) {
        int comparison = start - other.getStart();
        return comparison != 0 ? comparison : end - other.getEnd();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Interval interval = (Interval) o;
        return start == interval.start && end == interval.end;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}
