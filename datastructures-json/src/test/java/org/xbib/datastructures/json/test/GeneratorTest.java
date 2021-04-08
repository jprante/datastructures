package org.xbib.datastructures.json.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xbib.datastructures.json.Generator;
import org.xbib.datastructures.json.Lexer;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class GeneratorTest {

    Generator generator;

    @BeforeEach
    public void setup() {
        StringWriter sw = new StringWriter();
        generator = new Generator(sw);
    }

    @Test
    public void testArrayStr() throws IOException {
        generator.beginArray()
                .writeString("a")
                .writeString("b")
                .writeString("c")
                .endArray();
        String s = generator.toString();
        assertEquals("[\"a\",\"b\",\"c\"]", s);
        Lexer lexer = new Lexer(new StringReader(s));
        Collection<Object> list = lexer.nextList();
        assertEquals("[a, b, c]", list.toString());
    }

    @Test
    public void testArrayNum() throws IOException {
        generator.beginArray()
                .writeNumber(1)
                .writeNumber(2)
                .writeNumber(3)
                .endArray();
        String s = generator.toString();
        assertEquals("[1,2,3]", s);
        Lexer lexer = new Lexer(new StringReader(s));
        Collection<Object> list = lexer.nextList();
        assertEquals("[1, 2, 3]", list.toString());
    }

    @Test
    public void testArrayFloat() throws IOException {
        generator.beginArray()
                .writeNumber(1.0)
                .writeNumber(2.0)
                .writeNumber(3.0)
                .endArray();
        String s = generator.toString();
        assertEquals("[1.0,2.0,3.0]", s);
        Lexer lexer = new Lexer(new StringReader(s));
        Collection<Object> list = lexer.nextList();
        assertEquals("[1.0, 2.0, 3.0]", list.toString());
    }

    @Test
    public void testArrayFromCollection() throws IOException {
        generator.write(List.of("a", "b", "c"));
        assertEquals("[\"a\",\"b\",\"c\"]", generator.toString());
    }

    @Test
    public void testObjectStr() throws IOException {
        generator.beginObject()
                .writeString("a")
                .writeString("b")
                .endObject();
        String s = generator.toString();
        assertEquals("{\"a\":\"b\"}", s);
        Lexer lexer = new Lexer(new StringReader(s));
        Map<String, Object> map = lexer.nextMap();
        assertEquals("{a=b}", map.toString());
    }

    @Test
    public void testObjectNum() throws IOException {
        generator.beginObject()
                .writeString("a")
                .writeNumber(1)
                .endObject();
        String s = generator.toString();
        assertEquals("{\"a\":1}", s);
        Lexer lexer = new Lexer(new StringReader(s));
        Map<String, Object> map = lexer.nextMap();
        assertEquals("{a=1}", map.toString());
    }

    @Test
    public void testObjectStrFromMap() throws IOException {
        generator.write(Map.of("a", "b"));
        assertEquals("{\"a\":\"b\"}", generator.toString());
    }

    @Test
    public void testObjectNumFromMap() throws IOException {
        generator.write(Map.of("a", 2));
        assertEquals("{\"a\":2}", generator.toString());
    }
}
