package org.xbib.datastructures.common;

import java.util.Arrays;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ImmutableListTest {

    @Test
    public void testArray() {
        ImmutableList<Integer> immutableList = ImmutableList.of();
        assertEquals(0, immutableList.size());
        assertEquals("[]", immutableList.toString());
        Integer[] i = new Integer[] { 1, 2, 3 };
        immutableList = ImmutableList.of(i);
        assertEquals("[1, 2, 3]", immutableList.toString());
    }

    @Test
    public void testList() {
        ImmutableList<Integer> immutableList = ImmutableList.of(Arrays.asList(1, 2, 3), new Integer[0]);
        assertEquals("[1, 2, 3]", immutableList.toString());
    }

    @Test
    public void testStream() {
        ImmutableList<Integer> immutableList = ImmutableList.of(Arrays.asList(1, 2, 3), new Integer[0]);
        assertEquals(3L, immutableList.size());
        assertEquals("[2, 3, 4]", immutableList.stream().map(i -> i + 1).collect(Collectors.toList()).toString());
    }
}
