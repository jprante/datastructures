package org.xbib.datastructures.trie.ahocorasick;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class IntervalTree {

    private final IntervalNode rootNode;

    public IntervalTree(List<Interval> intervals) {
        this.rootNode = new IntervalNode(intervals);
    }

    public List<Interval> removeOverlaps(List<Interval> intervals) {
        intervals.sort((i1, i2) -> {
            int i = (i2.getEnd() - i2.getStart() + 1) - (i1.getEnd() - i1.getStart() + 1);
            if (i == 0) {
                i = i1.getStart() - i2.getStart();
            }
            return i;
        });
        Set<Interval> removeIntervals = new TreeSet<>();
        for (final Interval interval : intervals) {
            if (removeIntervals.contains(interval)) {
                continue;
            }
            removeIntervals.addAll(findOverlaps(interval));
        }
        for (final Interval removeInterval : removeIntervals) {
            intervals.remove(removeInterval);
        }
        intervals.sort(Comparator.comparingInt(Interval::getStart));
        return intervals;
    }

    public List<Interval> findOverlaps(Interval interval) {
        return rootNode.findOverlaps(interval);
    }

}
