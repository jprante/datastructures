package org.xbib.datastructures.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import java.util.LinkedHashMap;
import java.util.Map;

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

    @Test
    public void testHashMultiMap() {
        LinkedHashSetMultiMap<String, String> hashSetMultiMap = new LinkedHashSetMultiMap<>();
        assertTrue(hashSetMultiMap.isEmpty());
        hashSetMultiMap.put("a", "b");
        assertEquals("[b]", hashSetMultiMap.get("a").toString());
        hashSetMultiMap.put("a", "b");
        assertEquals("[b]", hashSetMultiMap.get("a").toString());
        hashSetMultiMap.put("a", "c");
        assertEquals("[b, c]", hashSetMultiMap.get("a").toString());
        hashSetMultiMap.put("b", "b");
        assertEquals("[b]", hashSetMultiMap.get("b").toString());
        hashSetMultiMap.put("b", "c");
        assertEquals("[b, c]", hashSetMultiMap.get("b").toString());
        hashSetMultiMap.put("b", "c");
        assertEquals("[b, c]", hashSetMultiMap.get("b").toString());
    }


    @Test
    public void testTreeMultiMap() {
        TreeMultiMap<String, String> treeMultiMap = new TreeMultiMap<>();
        assertTrue(treeMultiMap.isEmpty());
        treeMultiMap.put("a", "b");
        assertEquals("[b]", treeMultiMap.get("a").toString());
        treeMultiMap.put("a", "b");
        assertEquals("[b]", treeMultiMap.get("a").toString());
        treeMultiMap.put("a", "c");
        assertEquals("[b, c]", treeMultiMap.get("a").toString());
        treeMultiMap.put("b", "b");
        assertEquals("[b]", treeMultiMap.get("b").toString());
        treeMultiMap.put("b", "c");
        assertEquals("[b, c]", treeMultiMap.get("b").toString());
        treeMultiMap.put("b", "c");
        assertEquals("[b, c]", treeMultiMap.get("b").toString());
    }
}
