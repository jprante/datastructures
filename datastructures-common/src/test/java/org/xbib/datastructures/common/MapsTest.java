package org.xbib.datastructures.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 */
public class MapsTest {

    @Test
    public void testDeepMerge() {
        Map<String, Object> m1 = new LinkedHashMap<>();
        Map<String, Object> m2 = new LinkedHashMap<>();
        Map<String, Object> m3 = new LinkedHashMap<>();
        Map<String, Object> m4 = new LinkedHashMap<>();
        m1.put("a", "b");
        m2.put("c", m1);
        m3.put("d", "e");
        m4.put("e", "f");
        m3.put("c", m4);
        assertEquals("{d=e, c={e=f, a=b}}", Maps.deepMerge(m3, m2).toString());
    }
}
