package org.xbib.datastructures.yaml.tiny.test;

import org.junit.jupiter.api.Test;
import org.xbib.datastructures.api.Builder;
import org.xbib.datastructures.api.DataStructure;
import org.xbib.datastructures.tiny.TinyMap;
import org.xbib.datastructures.yaml.tiny.Yaml;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class YamlBuilderTest {

    @Test
    public void testSimpleMap() throws Exception {
        DataStructure structure = new Yaml();
        Builder builder = structure.createBuilder();
        builder.buildMap(Map.of("a", "b"));
        assertEquals("a: b\n", builder.build());
    }

    @Test
    public void testDoubleMap() throws Exception {
        DataStructure structure = new Yaml();
        Builder builder = structure.createBuilder();
        TinyMap.Builder<String, Object> mapBuilder = TinyMap.builder();
        mapBuilder.put("a", "b");
        mapBuilder.put("c", "d");
        builder.buildMap(mapBuilder.build());
        assertEquals("a: b\nc: d\n", builder.build());
    }

    @Test
    public void testNestedMap() throws Exception {
        DataStructure structure = new Yaml();
        Builder builder = structure.createBuilder();
        builder.buildMap(Map.of("a", Map.of("b", "c")));
        assertEquals("a: \n  b: c\n", builder.build());
    }

    @Test
    public void testMixedMap() throws Exception {
        DataStructure structure = new Yaml();
        Builder builder = structure.createBuilder();
        TinyMap.Builder<String, Object> mapBuilder = TinyMap.builder();
        mapBuilder.put("a", Map.of("b", "c"));
        mapBuilder.put("d", "e");
        builder.buildMap(mapBuilder.build());
        assertEquals("a: \n  b: c\nd: e\n", builder.build());
    }

    @Test
    public void testCollection() throws Exception {
        DataStructure structure = new Yaml();
        Builder builder = structure.createBuilder();
        builder.buildCollection(List.of("a", "b", "c"));
        assertEquals("- a\n- b\n- c\n", builder.build());
    }

    @Test
    public void testNestedCollection() throws Exception {
        DataStructure structure = new Yaml();
        Builder builder = structure.createBuilder();
        builder.buildCollection(List.of("a", "b", "c", List.of("d", "e", "f")));
        assertEquals("- a\n- b\n- c\n  - d\n  - e\n  - f\n", builder.build());
    }

    @Test
    public void testMapOfCollections() throws Exception {
        DataStructure structure = new Yaml();
        Builder builder = structure.createBuilder();
        TinyMap.Builder<String, Object> mapBuilder = TinyMap.builder();
        mapBuilder.put("a", List.of("b", "c"));
        mapBuilder.put("d", List.of("e", "f"));
        builder.buildMap(mapBuilder.build());
        assertEquals("a: \n  - b\n  - c\nd: \n  - e\n  - f\n", builder.build());
    }

    @Test
    public void testCollectionOfMaps() throws Exception {
        DataStructure structure = new Yaml();
        Builder builder = structure.createBuilder();
        builder.buildCollection(List.of(Map.of("a", "b"), Map.of("c", "d"), Map.of("e", "f")));
        assertEquals("- a: b\n" +
                "- c: d\n" +
                "- e: f\n", builder.build());
    }

    @Test
    public void testCollectionOfDoubleMaps() throws Exception {
        DataStructure structure = new Yaml();
        Builder builder = structure.createBuilder();
        TinyMap.Builder<String, Object> mapBuilder1 = TinyMap.builder();
        mapBuilder1.put("a", "b");
        mapBuilder1.put("c", "d");
        TinyMap.Builder<String, Object> mapBuilder2 = TinyMap.builder();
        mapBuilder2.put("e", "f");
        mapBuilder2.put("g", "h");
        builder.buildCollection(List.of(mapBuilder1.build(), mapBuilder2.build()));
        assertEquals("- a: b\n" +
                "  c: d\n" +
                "- e: f\n" +
                "  g: h\n", builder.build());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testYamlToMap() throws IOException {
        Map<String, Object> map = Yaml.toMap("map:\n  a: b\n");
        assertTrue(map.get("map") instanceof Map);
        assertEquals("b", ((Map<String, Object>) map.get("map")).get("a"));
    }
}
