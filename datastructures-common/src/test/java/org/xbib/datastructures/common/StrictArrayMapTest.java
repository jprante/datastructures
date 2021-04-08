package org.xbib.datastructures.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import java.util.List;

public class StrictArrayMapTest {

    @Test
    public void simpleMap() {
        StrictArrayMap<String, Object> strictArrayMap = new StrictArrayMap<>(List.of("a"), List.of("b"));
        assertEquals("{a=b}", strictArrayMap.toString());
    }

    @Test
    public void longerMap() {
        StrictArrayMap<String, Object> strictArrayMap = new StrictArrayMap<>(List.of("a", "b", "c"), List.of("d", "e", "f"));
        assertEquals("{a=d, b=e, c=f}", strictArrayMap.toString());
    }

    @Test
    public void doubleKeyMap() {
        StrictArrayMap<String, Object> strictArrayMap = new StrictArrayMap<>(List.of("a", "a", "a"), List.of("b", "b", "b"));
        assertEquals("{a=b, a=b, a=b}", strictArrayMap.toString());
    }

    @Test
    public void nullKeyMap() {
        StrictArrayMap<String, Object> strictArrayMap = new StrictArrayMap<>(new String[] {null }, new Object[] { null });
        assertEquals("{null=null}", strictArrayMap.toString());
    }
}
