package org.xbib.datastructures.tiny;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.xbib.datastructures.common.MultiMap;

public class TinyMultiMapTest {

    @Test
    public void testMultiMap() {
        MultiMap<String, String> multiMap = new TinyMultiMap<>();
        multiMap.put("a", "a");
        multiMap.put("a", "b");
        multiMap.put("a", "c");
        multiMap.put("b", "d");
        multiMap.put("b", "e");
        multiMap.put("b", "f");
        assertEquals("{a=[a, b, c], b=[d, e, f]}", multiMap.asMap().toString());
    }
}
