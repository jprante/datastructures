package org.xbib.datastructures.common;

import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ImmutableSetTest {

    @Test
    public void testArray() {
        ImmutableSet<Integer> immutableSet = ImmutableSet.of();
        assertEquals("[]", immutableSet.toString());
        Integer[] i = new Integer[] { };
        immutableSet = ImmutableSet.of(i);
        assertEquals("[]", immutableSet.toString());
        i = new Integer[] { 1 };
        immutableSet = ImmutableSet.of(i);
        assertEquals("[1]", immutableSet.toString());
        i = new Integer[] { 1, 1 };
        immutableSet = ImmutableSet.of(i);
        assertEquals("[1]", immutableSet.toString());
        i = new Integer[] { 1, 2 };
        immutableSet = ImmutableSet.of(i);
        assertEquals("[1, 2]", immutableSet.toString());
    }

    @Test
    public void testSet() {
        ImmutableSet<Integer> immutableSet = ImmutableSet.of(Set.of(1), new Integer[0]);
        assertEquals("[1]", immutableSet.toString());
        immutableSet = ImmutableSet.of(Set.of(1, 2), new Integer[0]);
        assertEquals("[1, 2]", immutableSet.toString());
        immutableSet = ImmutableSet.of(Set.of(1, 2, 3), new Integer[0]);
        assertEquals("[1, 2, 3]", immutableSet.toString());
    }

    @Test
    public void testStream() {
        ImmutableSet<Integer> immutableSet = ImmutableSet.of(Set.of(1, 2, 3), new Integer[0]);
        assertEquals(3L, immutableSet.size());
        assertEquals("[2, 3, 4]", immutableSet.stream().map(i -> i + 1).collect(Collectors.toList()).toString());
    }
}
