package org.xbib.datastructures.json.tiny.test;

import org.junit.jupiter.api.Test;
import org.xbib.datastructures.json.tiny.JsonBuilder;
import org.xbib.datastructures.json.tiny.StreamParser;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonBuilderTest {

    @Test
    public void testObjectStrFromMap() throws IOException {
        JsonBuilder jsonBuilder = new JsonBuilder();
        jsonBuilder.buildMap(Map.of("a", "b"));
        assertEquals("{\"a\":\"b\"}", jsonBuilder.build());
    }

    @Test
    public void testObjectNumFromMap() throws IOException {
        JsonBuilder jsonBuilder = new JsonBuilder();
        jsonBuilder.buildMap(Map.of("a", 2));
        assertEquals("{\"a\":2}", jsonBuilder.build());
    }

    @Test
    public void testArrayFromCollection() throws IOException {
        JsonBuilder jsonBuilder = new JsonBuilder();
        jsonBuilder.buildCollection(List.of("a", "b", "c"));
        assertEquals("[\"a\",\"b\",\"c\"]", jsonBuilder.build());
    }

    @Test
    public void testArrayStr() throws IOException {
        JsonBuilder jsonBuilder = new JsonBuilder();
        jsonBuilder.beginCollection()
                .buildValue("a")
                .buildValue("b")
                .buildValue("c")
                .endCollection();
        String s = jsonBuilder.build();
        assertEquals("[\"a\",\"b\",\"c\"]", s);
        StreamParser streamParser = new StreamParser();
        assertEquals("[a, b, c]", streamParser.parse(new StringReader(s)).get().toString());
    }

    @Test
    public void testArrayNum() throws IOException {
        JsonBuilder jsonBuilder = new JsonBuilder();
        jsonBuilder.beginCollection()
                .buildValue(1)
                .buildValue(2)
                .buildValue(3)
                .endCollection();
        String s = jsonBuilder.build();
        assertEquals("[1,2,3]", s);
        StreamParser streamParser = new StreamParser();
        assertEquals("[1, 2, 3]", streamParser.parse(new StringReader(s)).get().toString());
    }

    @Test
    public void testArrayFloat() throws IOException {
        JsonBuilder jsonBuilder = new JsonBuilder();
        jsonBuilder.beginCollection()
                .buildValue(1.0)
                .buildValue(2.0)
                .buildValue(3.0)
                .endCollection();
        String s = jsonBuilder.build();
        assertEquals("[1.0,2.0,3.0]", s);
        StreamParser streamParser = new StreamParser();
        assertEquals("[1.0, 2.0, 3.0]", streamParser.parse(new StringReader(s)).get().toString());
    }

    @Test
    public void testObjectStr() throws IOException {
        JsonBuilder jsonBuilder = new JsonBuilder();
        jsonBuilder.beginMap()
                .buildKey("a")
                .buildValue("b")
                .endMap();
        String s = jsonBuilder.build();
        assertEquals("{\"a\":\"b\"}", s);
        StreamParser streamParser = new StreamParser();
        assertEquals("{a=b}", streamParser.parse(new StringReader(s)).get().toString());
    }

    @Test
    public void testObjectNum() throws IOException {
        JsonBuilder jsonBuilder = new JsonBuilder();
        jsonBuilder.beginMap()
                .buildKey("a")
                .buildValue(1)
                .endMap();
        String s = jsonBuilder.build();
        assertEquals("{\"a\":1}", s);
        StreamParser streamParser = new StreamParser();
        assertEquals("{a=1}", streamParser.parse(new StringReader(s)).get().toString());
    }

    @Test
    public void testBuild() throws Exception {
        JsonBuilder jsonBuilder = new JsonBuilder();
        jsonBuilder.buildMap(Map.of("a", "b"));
        assertEquals("{\"a\":\"b\"}", jsonBuilder.build());
    }

    @Test
    public void testKeyValue() throws Exception {
        JsonBuilder jsonBuilder = new JsonBuilder();
        jsonBuilder.beginMap();
        jsonBuilder.buildKey("a");
        jsonBuilder.buildValue("b");
        // test comma separation
        jsonBuilder.buildKey("c");
        jsonBuilder.buildValue("d");
        jsonBuilder.endMap();
        assertEquals("{\"a\":\"b\",\"c\":\"d\"}", jsonBuilder.build());
    }

    @Test
    public void testMapBuild() throws Exception {
        JsonBuilder jsonBuilder = new JsonBuilder();
        jsonBuilder.beginMap();
        jsonBuilder.buildKey("map");
        // buildMap is wrapped with '{' and '}'
        jsonBuilder.buildMap(Map.of("a", "b"));
        jsonBuilder.endMap();
        assertEquals("{\"map\":{\"a\":\"b\"}}", jsonBuilder.build());
    }

    @Test
    public void testBeginMapBuild() throws Exception {
        JsonBuilder jsonBuilder = new JsonBuilder();
        jsonBuilder.beginMap();
        jsonBuilder.beginMap("map");
        // buildMap is not wrapped with '{' and '}'
        jsonBuilder.buildMap(Map.of("a", "b"));
        jsonBuilder.endMap();
        jsonBuilder.endMap();
        assertEquals("{\"map\":{\"a\":\"b\"}}", jsonBuilder.build());
    }

    @Test
    public void testMapOfCollections() throws Exception {
        JsonBuilder jsonBuilder = new JsonBuilder();
        jsonBuilder.beginMap();
        jsonBuilder.beginMap("map");
        jsonBuilder.collection("a", Arrays.asList("b", "c"));
        // test comma separation
        jsonBuilder.collection("d", Arrays.asList("e", "f"));
        jsonBuilder.endMap();
        jsonBuilder.endMap();
        assertEquals("{\"map\":{\"a\":[\"b\",\"c\"],\"d\":[\"e\",\"f\"]}}", jsonBuilder.build());
    }

    @Test
    public void testMapOfEmptyCollections() throws Exception {
        JsonBuilder jsonBuilder = new JsonBuilder();
        jsonBuilder.beginMap();
        jsonBuilder.beginMap("map");
        jsonBuilder.collection("a", List.of());
        // test comma separation
        jsonBuilder.collection("b", List.of());
        jsonBuilder.endMap();
        jsonBuilder.endMap();
        assertEquals("{\"map\":{\"a\":[],\"b\":[]}}", jsonBuilder.build());
    }

    @Test
    public void testCollectionOfMaps() throws Exception {
        JsonBuilder jsonBuilder = new JsonBuilder();
        jsonBuilder.beginMap();
        jsonBuilder.beginCollection("collection");
        jsonBuilder.buildMap(Map.of("a", "b"));
        // test comma separation
        jsonBuilder.buildMap(Map.of("c", "d"));
        jsonBuilder.endCollection();
        jsonBuilder.endMap();
        assertEquals("{\"collection\":[{\"a\":\"b\"},{\"c\":\"d\"}]}", jsonBuilder.build());
    }

    @Test
    public void testCollectionOfEmptyMaps() throws Exception {
        JsonBuilder jsonBuilder = new JsonBuilder();
        jsonBuilder.beginMap();
        jsonBuilder.beginCollection("collection");
        jsonBuilder.buildMap(Map.of());
        // test comma separation
        jsonBuilder.buildMap(Map.of());
        jsonBuilder.endCollection();
        jsonBuilder.endMap();
        assertEquals("{\"collection\":[{},{}]}", jsonBuilder.build());
    }

    @Test
    public void testCopy() throws Exception {
        JsonBuilder jsonBuilder1 = new JsonBuilder();
        jsonBuilder1.buildMap(Map.of("a", "b"));
        JsonBuilder jsonBuilder2 = new JsonBuilder();
        jsonBuilder2.buildMap(Map.of("c", "d"));
        JsonBuilder jsonBuilder = new JsonBuilder();
        jsonBuilder.beginCollection();
        jsonBuilder.copy(jsonBuilder1);
        // test comma separation
        jsonBuilder.copy(jsonBuilder2);
        jsonBuilder.endCollection();
        assertEquals("[{\"a\":\"b\"},{\"c\":\"d\"}]", jsonBuilder.build());
    }

}
