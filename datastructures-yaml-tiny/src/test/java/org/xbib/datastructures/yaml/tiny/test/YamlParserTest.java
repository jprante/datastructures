package org.xbib.datastructures.yaml.tiny.test;

import org.junit.jupiter.api.Test;
import org.xbib.datastructures.api.Node;
import org.xbib.datastructures.api.Parser;
import org.xbib.datastructures.yaml.tiny.Yaml;
import org.xbib.datastructures.yaml.tiny.YamlParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class YamlParserTest {

    @Test
    public void parseList() throws Exception {
        Reader reader = new StringReader("test:\n- a\n- b\n");
        Parser parser = new YamlParser();
        Node<?> node = parser.parse(reader);
        Yaml yaml = new Yaml(node);
        StringWriter writer = new StringWriter();
        yaml.createGenerator(node).generate(writer);
        String s1 = writer.toString();
    }

    @Test
    public void example() throws Exception {
        InputStream inputStream = YamlTest.class.getResourceAsStream("/org/xbib/datastructures/yaml/test/example.yml");
        if (inputStream != null) {
            roundTrip(inputStream);
        }
    }

    @Test
    public void test() throws Exception {
        InputStream inputStream = YamlTest.class.getResourceAsStream("/org/xbib/datastructures/yaml/test/test.yml");
        if (inputStream != null) {
            roundTrip(inputStream);
        }
    }

    @Test
    public void hippie() throws Exception {
        InputStream inputStream = YamlTest.class.getResourceAsStream("/org/xbib/datastructures/yaml/test/hippie.yml");
        if (inputStream != null) {
           roundTrip(inputStream);
        }
    }

    @Test
    public void interlibrary() throws Exception {
        InputStream inputStream = Files.newInputStream(Paths.get(System.getProperty("user.home") + "/.config/interlibrary/test.yaml"));
        roundTrip(inputStream);
    }

    private static void roundTrip(InputStream inputStream) throws Exception {
        Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        Parser parser = new YamlParser();
        Node<?> node = parser.parse(reader);
        Yaml yaml = new Yaml(node);
        StringWriter writer = new StringWriter();
        yaml.createGenerator(node).generate(writer);
        String s1 = writer.toString();
        Node<?> node2 = parser.parse(new StringReader(writer.toString()));
        writer = new StringWriter();
        yaml.createGenerator(node2).generate(writer);
        String s2 = writer.toString();
        assertEquals(s1, s2);
    }
}
