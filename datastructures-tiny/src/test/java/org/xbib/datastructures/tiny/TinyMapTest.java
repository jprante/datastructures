package org.xbib.datastructures.tiny;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class TinyMapTest {

    @Test
    public void simpleTest() {
        TinyMap.Builder<String, Object> builder = TinyMap.builder();
        assertEquals(0, builder.size());
        builder.put("a", 1);
        builder.put("b", 2);
        builder.put("c", 3);
        assertEquals("{a=1, b=2, c=3}", builder.build().toString());
    }

    @Test
    public void testBuildAndGet() {
        TinyMap.Builder<String, Object> builder = TinyMap.builder();
        assertEquals(0, builder.size());
        builder.put("aaa", 333);
        builder.put("bbb", 456.0);
        builder.put("aaa", 123);
        assertEquals(2, builder.size());
        TinyMap<String, Object> map = builder.build();
        assertEquals(123, map.get("aaa"));
        assertEquals(456.0, map.get("bbb"));
        assertNull(map.get("ccc"));
        assertEquals("def", map.getOrDefault("ccc", "def"));
        assertEquals("def", map.getOrDefault(null, "def"));
        assertEquals(2, map.size());
        assertTrue(map.containsKey("aaa"));
        assertTrue(map.containsKey("bbb"));
    }

    @Test
    public void canBuildWithDuplicateKeys() {
        TinyMap.Builder<String, Object> builder = TinyMap.builder();
        builder.put("aaa", 123);
        builder.put("aaa", 456.0);
        builder.put("bbb", 789.0);
        assertThat(builder.size(), equalTo(2));
        assertThat(builder.build(), equalTo(Map.of("aaa", 456.0, "bbb", 789.0)));
        assertThat(builder.size(), equalTo(2));
        assertThat(builder.build(), equalTo(Map.of("aaa", 456.0, "bbb", 789.0)));
    }

    @Test
    public void canBuildWithNull() {
        TinyMap.Builder<String, Object> builder = TinyMap.builder();
        builder.put(null, 123);
        assertThat(builder.build(), equalTo(Collections.singletonMap(null, 123)));
    }

    @Test
    public void canBuildMediumWithDuplicateKeys() {
        TinyMap.Builder<String, Object> builder = TinyMap.builder();
        builder.put("aaa", 123);
        builder.put("aaa", 456.0);
        for (int i = 0; i < 1000; i++) {
            builder.put("aaa" + i, i);
        }
        assertThat(builder.build().size(), equalTo(1001));
    }

    @Test
    public void canBuildLargeWithDuplicateKeys() {
        TinyMap.Builder<String, Object> builder = TinyMap.builder();
        builder.put("aaa", 123);
        builder.put("aaa", 456.0);
        for (int i = 0; i < 0x10000; i++) {
            builder.put("aaa" + i, i);
        }
        assertThat(builder.build().size(), equalTo(65537));
    }

    @Test
    public void testContains() {
        TinyMap.Builder<String, Object> builder = TinyMap.builder();
        builder.put("aaa", null);
        builder.put("bbb", 456.0);
        TinyMap<String, Object> map = builder.build();
        assertTrue(map.containsKey("aaa"));
        assertFalse(map.containsKey("ccc"));
        assertTrue(map.containsValue(null));
        assertFalse(map.containsValue(123.0));
        assertTrue(map.containsKey("aaa"));
        assertFalse(map.containsKey("ccc"));
        assertTrue(map.containsValue(null));
        assertFalse(map.containsValue(123.0));
        assertTrue(map.entrySet().contains(new AbstractMap.SimpleEntry<>("aaa", null)));
        assertFalse(map.entrySet().contains(new AbstractMap.SimpleEntry<>("aaa", 123.0)));
        assertFalse(map.entrySet().contains(new AbstractMap.SimpleEntry<>("ccc", null)));
        assertFalse(map.entrySet().contains(new Object()));
    }

    @Test
    public void testBuildSmallEnough() throws Exception {
        testCount(0, true);

        for (int i = 0; i <= 16; i++) {
            testCount(i, false);
        }
    }

    @Test
    public void testBuildMedium() throws Exception {
        testCount(1000, false);
        testCount(1000, true);
    }

    @Test
    public void testBuildAlmostThere() throws Exception {
        testCount(255, false);
        testCount(255, true);
    }

    @Test
    public void testBuildSmall() throws Exception {
        testCount(123, false);
        testCount(123, true);
    }

    @Test
    public void testValueArrayTwoDifferentMaps() {
        TinyMap.Builder<String, Object> builder1 = TinyMap.builder();
        TinyMap.Builder<String, Object> builder2 = TinyMap.builder();
        for (int i = 0; i < 100; i++) {
            builder1.put("aaa" + i, i);
            builder2.put("aaa" + i, i);
        }
        TinyMap<String, Object> map1 = builder1.build();
        TinyMap<String, Object> map2 = builder2.build();
        assertThat(map1.keySet(), equalTo(map2.keySet()));
    }

    @Test
    public void testGiantShortProblem() {
        TinyMap.Builder<String, Object> builder = TinyMap.builder();
        for (int i = 0; i < 100000; i++) {
            builder.put("aaa" + i, i);
        }
        TinyMap<String, Object> map = builder.build();
        assertEquals(99999, map.get("aaa99999"));
    }

    @Test
    public void testCompareKeys() {
        TinyMap.Builder<String, Object> builder1 = TinyMap.builder();
        for (int i = 0; i < 100; i++) {
            builder1.put("" + i, i);
        }
        TinyMap.Builder<String, Object> builder2 = TinyMap.builder();
        for (int i = 0; i < 100; i++) {
            builder2.put("" + i, i);
        }
        assertEquals(0,  builder1.keySet().compareTo(builder2.keySet()));
    }

    @Test
    public void testCompareBuilder() {
        TinyMap.Builder<String, Object> builder1 = TinyMap.builder();
        for (int i = 0; i < 100; i++) {
            builder1.put("" + i, i);
        }
        TinyMap.Builder<String, Object> builder2 = TinyMap.builder();
        for (int i = 0; i < 100; i++) {
            builder2.put("" + i, i);
        }
        assertEquals(0,  builder1.compareTo(builder2));
    }

    @Test
    public void testFailCompareBuilder() {
        TinyMap.Builder<String, Object> builder1 = TinyMap.builder();
        for (int i = 0; i < 100; i++) {
            builder1.put("" + i, i);
        }
        TinyMap.Builder<String, Object> builder2 = TinyMap.builder();
        for (int i = 0; i < 101; i++) {
            builder2.put("" + i, i);
        }
        assertNotEquals(0,  builder1.compareTo(builder2));
    }

    @Test
    public void testSortKeys() {
        TinyMap.Builder<String, Object> builder = TinyMap.builder();
        for (int i = 0; i < 100; i++) {
            builder.put("" + i, i);
        }
        assertEquals(100, builder.keySet().stream().sorted().count());
    }

    @Test
    public void testSortEntries() {
        TinyMap.Builder<String, Object> builder = TinyMap.builder();
        for (int i = 0; i < 100; i++) {
            builder.put("" + i, i);
        }
        Set<Map.Entry<String, Object>> set = new HashSet<>(builder.entrySet());
        assertEquals(100, set.size());
    }

    @Test
    public void testPutAllNull() {
        TinyMap.Builder<String, Object> builder = TinyMap.builder();
        builder.put("a", "b");
        builder.putAll(null);
        assertEquals(1, builder.build().size());
    }

    private void testCount(int count, boolean withNull) {
        TinyMap.Builder<String, Object> builder = TinyMap.builder();
        LinkedHashMap<String, Object> expectedMap = new LinkedHashMap<>();
        for (int i = 0; i < count; i++) {
            if (count < 1000)
                builder.build();

            builder.putAll(Collections.singletonMap("aaa" + i, i));
            expectedMap.put("aaa" + i, i);
        }
        if (withNull) {
            builder.put(null, null);
            expectedMap.put(null, null);
        }
        TinyMap<String, Object> map = builder.build();
        assertThat(expectedMap, is(map));
    }
}