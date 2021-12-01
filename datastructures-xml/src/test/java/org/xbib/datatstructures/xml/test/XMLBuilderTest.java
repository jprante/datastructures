package org.xbib.datatstructures.xml.test;

import org.junit.jupiter.api.Test;
import org.xbib.datastructures.xml.XMLBuilder;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class XMLBuilderTest {

    @Test
    public void simpleTest() throws Exception {
        XMLBuilder builder = XMLBuilder.builder();
        builder.startElement("root").endElement();
        assertEquals("<root/>", builder.build());
    }

    @Test
    public void nestedTest() throws Exception {
        XMLBuilder builder = XMLBuilder.builder();
        builder.startElement("a").startElement("b").endElement().endElement();
        assertEquals("<a><b/></a>", builder.build());
    }

    @Test
    public void nestedTextTest() throws Exception {
        XMLBuilder builder = XMLBuilder.builder();
        builder.startElement("a")
                .startElement("b").addText("Hello world")
                .endElement()
                .endElement();
        assertEquals("<a><b>Hello world</b></a>", builder.build());
    }

    @Test
    public void sequenceTest() throws Exception {
        XMLBuilder builder = XMLBuilder.builder();
        builder.startElement("a").endElement().startElement("b").endElement().startElement("c").endElement();
        assertEquals("<a/><b/><c/>", builder.build());
    }

    @Test
    public void attributeTest() throws Exception {
        XMLBuilder builder = XMLBuilder.builder();
        builder.startElement("a").addAttribute("b", "c").endElement();
        assertEquals("<a b=\"c\"/>", builder.build());
    }

    @Test
    public void emptyMapTest() throws Exception {
        XMLBuilder builder = XMLBuilder.builder();
        builder.beginMap().endMap();
        assertEquals("<root/>", builder.build());
    }

    @Test
    public void simpleMapTest() throws Exception {
        XMLBuilder builder = XMLBuilder.builder();
        builder.beginMap()
                .buildKey("a")
                .buildValue("b")
                .endMap();
        assertEquals("<root><a>b</a></root>", builder.build());
    }

    @Test
    public void doubleMapTest() throws Exception {
        XMLBuilder builder = XMLBuilder.builder();
        builder.beginMap()
                .buildKey("a")
                .buildValue("b")
                .buildKey("c")
                .buildValue("d")
                .endMap();
        assertEquals("<root><a>b</a><c>d</c></root>", builder.build());
    }

    @Test
    public void mapTest() throws Exception {
        XMLBuilder builder = XMLBuilder.builder();
        Map<String, Object> map = Map.of("a", "b");
        builder.buildMap(map);
        assertEquals("<root><a>b</a></root>", builder.build());
    }

    @Test
    public void collectionTest() throws Exception {
        XMLBuilder builder = XMLBuilder.builder();
        List<Object> list = List.of("a", "b", "c");
        builder.buildCollection(list);
        assertEquals("<root>a</root><root>b</root><root>c</root>", builder.build());
    }

    @Test
    public void copyTest() throws Exception {
        XMLBuilder builder1 = XMLBuilder.builder();
        builder1.buildMap(Map.of("a", "b"));
        XMLBuilder builder2 = XMLBuilder.builder();
        builder2.buildMap(Map.of("c", "d"));
        XMLBuilder builder = XMLBuilder.builder();
        builder.beginCollection();
        builder.copy(builder1);
        builder.copy(builder2);
        builder.endCollection();
        assertEquals("<root><a>b</a></root><root><c>d</c></root>", builder.build());
    }

}
