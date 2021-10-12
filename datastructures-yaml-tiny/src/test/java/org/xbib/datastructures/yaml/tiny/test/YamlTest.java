package org.xbib.datastructures.yaml.tiny.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xbib.datastructures.api.Builder;
import org.xbib.datastructures.api.DataStructure;
import org.xbib.datastructures.api.Generator;
import org.xbib.datastructures.api.Parser;
import org.xbib.datastructures.yaml.tiny.YamlGenerator;
import org.xbib.datastructures.yaml.tiny.ListNode;
import org.xbib.datastructures.yaml.tiny.MapNode;
import org.xbib.datastructures.api.Node;
import org.xbib.datastructures.yaml.tiny.ValueNode;
import org.xbib.datastructures.yaml.tiny.Yaml;
import org.xbib.datastructures.yaml.tiny.YamlParser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class YamlTest {

    private Node<?> node;

    private Yaml yaml;

    @BeforeEach
    public void setup() throws IOException {
        InputStream inputStream = YamlTest.class.getResourceAsStream("/org/xbib/datastructures/yaml/test/test.yml");
        if (inputStream != null) {
            Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            Parser parser = new YamlParser();
            node = parser.parse(reader);
            yaml = new Yaml(node);
        }
    }

    @Test
    public void testNode() {
        MapNode hsnode;
        ListNode lsnode;
        ValueNode valueNode;
        hsnode = (MapNode) node;
        valueNode = (ValueNode) hsnode.get("test");
        assertEquals("", valueNode.get());
        hsnode = (MapNode) node;
        hsnode = (MapNode) hsnode.get("types");
        valueNode = (ValueNode) hsnode.get("multiline");
        assertEquals("line 1 line 2 line 3", valueNode.get());
        valueNode = (ValueNode) hsnode.get("text");
        assertEquals("def func(x) do\n  # do something\n  print x = x * 2\nend", valueNode.get());
        hsnode = (MapNode) node;
        lsnode = (ListNode) hsnode.get("hash1");
        hsnode = (MapNode) lsnode.get(0);
        lsnode = (ListNode) hsnode.get("hash2");
        valueNode = (ValueNode) lsnode.get(0);
        assertEquals("the treasure is deep", valueNode.get());
        valueNode.set("new val");
        assertEquals("new val", valueNode.get());
    }

    @Test
    public void testRead() {
        assertEquals("", yaml.getString("test"));
        assertEquals("the value", yaml.getString("the key"));
        assertTrue(yaml.getBoolean("types.bool"));
        assertEquals((byte) -1, yaml.getByte("types.byte"));
        assertEquals((short)(3200), yaml.getShort("types.short"));
        assertEquals(-2100000000, yaml.getInteger("types.int"));
        assertEquals(1234321425321L, yaml.getLong("types.long"));
        assertEquals(3.1415926F, yaml.getFloat("types.float"));
        assertEquals(123413.4567654567654, yaml.getDouble("types.double"));
        assertEquals('c', yaml.getCharacter("types.char"));
        assertEquals("this is a string", yaml.getString("types.string"));
        assertEquals("line 1 line 2 line 3", yaml.getString("types.multiline"));
        assertEquals("def func(x) do\n  # do something\n  print x = x * 2\nend", yaml.getString("types.text"));
        assertEquals(Instant.parse("1997-01-06T23:12:10Z"), yaml.getInstant("types.datetime"));
        assertEquals("Text", yaml.getString("list.0"));
        assertEquals("val", yaml.getString("list.1.key"));
        assertEquals("ListOfList", yaml.getString("list.2.0"));
        assertEquals("Text", yaml.getString("hash.name1"));
        assertEquals("val", yaml.getString("hash.name2.key"));
        assertEquals("HashOfList", yaml.getString("hash.name3.0"));
        assertEquals("the treasure is deep", yaml.getString("hash1.0.hash2.0"));
    }

    @Test
    public void testMapListNodes() {
        Yaml yaml = new Yaml();
        yaml.set("key1", 123);
        assertEquals( (byte)123, yaml.getByte("key1"));
        yaml.set("key2.key3", 3.14);
        assertTrue(3.14F - yaml.getFloat("key2.key3") < 1e-8);
        yaml.set("list.0", "line1");
        yaml.set("list.1", "line2");
        assertEquals("line1", yaml.getString("list.0"));
        assertEquals("line2", yaml.getString("list.1"));
    }

    @Test
    public void testGenerator() throws IOException {
        Writer writer = new FileWriter("build/test.yml");
        Generator generator = new YamlGenerator(node);
        String text1 = writeToString(generator);
        generator.generate(writer);
        Reader reader = new FileReader("build/test.yml");
        Parser parser = new YamlParser();
        String text2 = writeToString(new YamlGenerator(parser.parse(reader)));
        assertEquals(text1, text2);
    }

    @Test
    public void testBuilder() throws IOException {
        List<Object> list = List.of("Text", Map.of("key", "val"), List.of("ListOfList"));
        Yaml yaml = new Yaml();
        Builder builder = yaml.createBuilder();
        builder.buildCollection(list);
        Parser parser = yaml.createParser();
        Node<?> node = parser.parse(new StringReader(builder.build()));
        String text1 = writeToString(new YamlGenerator(node));
        parser = new YamlParser();
        Node<?> node1 = parser.parse(new StringReader(text1));
        String text2 = writeToString(new YamlGenerator(node1));
        assertEquals(text1, text2);
    }

    @Test
    public void testStructure() throws IOException {
        DataStructure structure = new Yaml();
        Parser parser = structure.createParser();
        InputStream inputStream = YamlTest.class.getResourceAsStream("/org/xbib/datastructures/yaml/test/example.yml");
        if (inputStream == null) {
            fail();
        }
        structure.setRoot(parser.parse(new InputStreamReader(inputStream, StandardCharsets.UTF_8)));
        structure.set("I.hate", "Yaml");
        structure.set("I.love", "Ruby");
        structure.set("I.love", "Java");
        structure.set("You.are", "Foolish");
        structure.set("a.0", "one");
        structure.set("a.1", "two");
        structure.set("a.2", "three");
        Generator generator = structure.createGenerator(structure.getRoot());
        generator.generate(new FileWriter("build/example.yml"));
        structure.setRoot(parser.parse((new FileReader("build/example.yml"))));
        assertEquals("Yaml", structure.getString("I.hate"));
        assertEquals("Java", structure.getString("I.love"));
        assertEquals("Foolish", structure.getString("You.are"));
        assertNull(structure.getString("no.where"));
    }

    @Test
    public void testObject() throws Exception {
        DataStructure structure = new Yaml();
        Builder builder = structure.createBuilder();
        builder.beginMap()
                .buildKey("a")
                .buildValue("b")
                .endMap();
        String s = builder.build();
        assertEquals("a: b\n", s);
    }

    private String writeToString(Generator generator) throws IOException {
        StringWriter stringWriter = new StringWriter();
        generator.generate(stringWriter);
        return stringWriter.toString();
    }
}
