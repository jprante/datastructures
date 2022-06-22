package org.xbib.datastructures.trie.ahocorasick;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IntervalNode {

    private IntervalNode left;

    private IntervalNode right;

    private final int point;

    private final List<Interval> intervals;

    public IntervalNode(List<Interval> intervals) {
        this.intervals = new ArrayList<>();
        this.point = determineMedian(intervals);
        List<Interval> toLeft = new ArrayList<>();
        List<Interval> toRight = new ArrayList<>();
        for (Interval interval : intervals) {
            if (interval.getEnd() < point) {
                toLeft.add(interval);
            } else if (interval.getStart() > point) {
                toRight.add(interval);
            } else {
                this.intervals.add(interval);
            }
        }
        if (toLeft.size() > 0) {
            left = new IntervalNode(toLeft);
        }
        if (toRight.size() > 0) {
            right = new IntervalNode(toRight);
        }
    }

    public int determineMedian(List<Interval> intervals) {
        int start = -1;
        int end = -1;
        for (Interval interval : intervals) {
            int currentStart = interval.getStart();
            int currentEnd = interval.getEnd();
            if (start == -1 || currentStart < start) {
                start = currentStart;
            }
            if (end == -1 || currentEnd > end) {
                end = currentEnd;
            }
        }
        return (start + end) / 2;
    }

    public List<Interval> findOverlaps(Interval interval) {
        List<Interval> overlaps = new ArrayList<>();
        if (point < interval.getStart()) {
            addToOverlaps(interval, overlaps, findOverlappingRanges(right, interval));
            addToOverlaps(interval, overlaps, checkForOverlapsToTheRight(interval));
        } else if (point > interval.getEnd()) {
            addToOverlaps(interval, overlaps, findOverlappingRanges(left, interval));
            addToOverlaps(interval, overlaps, checkForOverlapsToTheLeft(interval));
        } else {
            addToOverlaps(interval, overlaps, intervals);
            addToOverlaps(interval, overlaps, findOverlappingRanges(left, interval));
            addToOverlaps(interval, overlaps, findOverlappingRanges(right, interval));
        }
        return overlaps;
    }

    protected void addToOverlaps(Interval interval, List<Interval> overlaps, List<Interval> newOverlaps) {
        for (Interval currentInterval : newOverlaps) {
            if (!currentInterval.equals(interval)) {
                overlaps.add(currentInterval);
            }
        }
    }

    protected List<Interval> checkForOverlapsToTheLeft(Interval interval) {
        return checkForOverlaps(interval, Direction.LEFT);
    }

    protected List<Interval> checkForOverlapsToTheRight(Interval interval) {
        return checkForOverlaps(interval, Direction.RIGHT);
    }

    protected List<Interval> checkForOverlaps(Interval interval, Direction direction) {
        List<Interval> overlaps = new ArrayList<>();
        for (Interval currentInterval : intervals) {
            switch (direction) {
                case LEFT:
                    if (currentInterval.getStart() <= interval.getEnd()) {
                        overlaps.add(currentInterval);
                    }
                    break;
                case RIGHT:
                    if (currentInterval.getEnd() >= interval.getStart()) {
                        overlaps.add(currentInterval);
                    }
                    break;
            }
        }
        return overlaps;
    }

    protected List<Interval> findOverlappingRanges(IntervalNode node, Interval interval) {
        return node == null ? Collections.emptyList() : node.findOverlaps(interval);
    }

}
