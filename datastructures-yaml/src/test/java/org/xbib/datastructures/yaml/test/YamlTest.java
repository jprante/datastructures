package org.xbib.datastructures.yaml.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xbib.datastructures.yaml.Builder;
import org.xbib.datastructures.yaml.Generator;
import org.xbib.datastructures.yaml.model.HashNode;
import org.xbib.datastructures.yaml.model.ListNode;
import org.xbib.datastructures.yaml.model.Node;
import org.xbib.datastructures.yaml.Parser;
import org.xbib.datastructures.yaml.model.ValueNode;
import org.xbib.datastructures.yaml.Yaml;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class YamlTest {

    private static final Logger logger = Logger.getLogger(YamlTest.class.getName());

    private Node root;

    private Yaml yaml;

    @BeforeEach
    public void setup() throws IOException {
        Reader reader = new InputStreamReader(YamlTest.class.getResourceAsStream("/org/xbib/datastructures/yaml/test/test.yml"));
        Parser parser = new Parser(reader);
        parser.parse();
        root = parser.getNode();
        yaml = new Yaml(root);
    }

    @Test
    public void testParser() { logger.log(Level.INFO, yaml.toString()); }

    @Test
    public void testNode() {
        HashNode hsnode; ListNode lsnode; ValueNode txnode;
        hsnode = (HashNode) root;
        txnode = (ValueNode) hsnode.getChild("test");
        assertEquals("", txnode.getValue());
        hsnode = (HashNode) root;
        hsnode = (HashNode) hsnode.getChild("types");
        txnode = (ValueNode) hsnode.getChild("multiline");
        assertEquals("line 1 line 2 line 3", txnode.getValue());
        txnode = (ValueNode) hsnode.getChild("text");
        assertEquals("def func(x) do\n  # do something\n  print x = x * 2\nend", txnode.getValue());
        hsnode = (HashNode) root;
        lsnode = (ListNode) hsnode.getChild("hash1");
        hsnode = (HashNode) lsnode.getItem(0);
        lsnode = (ListNode) hsnode.getChild("hash2");
        txnode = (ValueNode) lsnode.getItem(0);
        assertEquals("the treasure is deep", txnode.getValue());
        txnode.setValue("new val");
        assertEquals("new val", txnode.getValue());
    }

    @Test
    public void testReadTree() {
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
    public void testWriteTree() {
        Yaml yaml = new Yaml();
        yaml.set("key1", 123);
        assertEquals( (byte)123, yaml.getByte("key1"));
        yaml.set("key2.key3", 3.14);
        assertTrue(3.14F - yaml.getFloat("key2.key3") < 1e-8);
        yaml.set("list.0", "line1");
        yaml.set("list.1", "line2");
        assertEquals("line1", yaml.getString("list.0"));
        assertEquals("line2", yaml.getString("list.1"));
        logger.log(Level.INFO, yaml.toString());
    }

    @Test
    public void testGenerator() throws IOException {
        Writer writer = new FileWriter("build/test.yml");
        Generator generator = new Generator(root);
        String text1 = writeToString(generator);
        logger.log(Level.INFO, "text1 = " + text1);
        generator.generate(writer);
        Reader reader = new FileReader("build/test.yml");
        Parser parser = new Parser(reader);
        parser.parse();
        String text2 = writeToString(new Generator(parser.getNode()));
        logger.log(Level.INFO, "text2 = " + text2);
        assertEquals(text1, text2);
    }

    @Test
    public void testBuilder() throws IOException {
        List<Object> struct = List.of("Text", Map.of("key", "val"), List.of("ListOfList"));
        Builder builder = new Builder(struct);
        Yaml yaml = new Yaml(builder.build());
        Formatter formatter = new Formatter();
        logger.log(Level.INFO, "node1 = " + formatter.format(yaml.getRoot()));
        String text1 = writeToString(new Generator(yaml.getRoot()));
        logger.log(Level.INFO, "text1 = " + text1);
        Parser parser = new Parser(new StringReader(text1));
        parser.parse();
        Node node = parser.getNode();
        logger.log(Level.INFO, "node2 = " + formatter.format(node));
        String text2 = writeToString(new Generator(node));
        logger.log(Level.INFO, "text2 = " + text2);
        assertEquals(text1, text2);
    }

    @Test
    public void testYamlSetter() throws IOException {
        Yaml yaml = new Yaml();
        yaml.read(new InputStreamReader(YamlTest.class.getResourceAsStream("/org/xbib/datastructures/yaml/test/example.yml")));
        yaml.set("I.hate", "Yaml");
        yaml.set("I.love", "Ruby");
        yaml.set("I.love", "Java");
        yaml.set("You.are", "Foolish");
        yaml.set("a.0", "one");
        yaml.set("a.1", "two");
        yaml.set("a.2", "three");
        yaml.save(new FileWriter("build/example.yml"));
        yaml.read(new FileReader("build/example.yml"));
        assertEquals("Yaml", yaml.getString("I.hate"));
        assertEquals("Java", yaml.getString("I.love"));
        assertEquals("Foolish", yaml.getString("You.are"));
        assertNull(yaml.getString("no.where"));
        assertEquals("novalue", yaml.getString("no.where", "novalue"));
    }

    private String writeToString(Generator generator) {
        StringWriter stringWriter = new StringWriter();
        try {
            generator.generate(stringWriter);
        } catch (IOException e) {
            // ignore
        }
        return stringWriter.toString();
    }

}
