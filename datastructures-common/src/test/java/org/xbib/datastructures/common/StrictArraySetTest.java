package org.xbib.datastructures.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class StrictArraySetTest {

    @Test
    public void simpleSet() {
        StrictArraySet<String> strictArraySet = new StrictArraySet<>(new String[]{"a"});
        assertEquals("[a]", strictArraySet.toString());
    }

    @Test
    public void longerSet() {
        StrictArraySet<String> strictArraySet = new StrictArraySet<>(new String[]{"a", "b", "c"});
        assertEquals("[a, b, c]", strictArraySet.toString());
    }

    @Test
    public void multipleKeySet() {
        StrictArraySet<String> strictArraySet = new StrictArraySet<>(new String[]{"a", "a", "a"});
        assertEquals("[a, a, a]", strictArraySet.toString());
    }

    @Test
    public void nulleKeySet() {
        StrictArraySet<String> strictArraySet = new StrictArraySet<>(new String[]{null});
        assertEquals("[null]", strictArraySet.toString());
    }
}
