package org.xbib.datastructures.csv.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.xbib.datastructures.csv.Generator;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;

public class GeneratorTest {

    @Test
    public void testOneColumn() throws IOException {
        StringWriter writer = new StringWriter();
        Generator gen = new Generator(writer);
        gen.keys(Arrays.asList("a"));
        for (int i = 0; i < 2; i++) {
            gen.write("val" + i);
        }
        gen.close();
        assertEquals("val0\nval1", writer.toString());
    }

    @Test
    public void testTwoColumns() throws IOException {
        StringWriter writer = new StringWriter();
        Generator gen = new Generator(writer);
        gen.keys(Arrays.asList("a", "b"));
        for (int i = 0; i < 2; i++) {
            gen.write("val" + i);
            gen.write("val" + i);
        }
        gen.close();
        assertEquals("val0,val0\nval1,val1", writer.toString());
    }

    @Test
    public void testThreeColumns() throws IOException {
        StringWriter writer = new StringWriter();
        Generator gen = new Generator(writer);
        gen.keys(Arrays.asList("a", "b", "c"));
        for (int i = 0; i < 1; i++) {
            gen.write("val" + i);
            gen.write("\"Hello, World\"");
            gen.write("hey look a line seperator \n");
        }
        gen.close();
        assertEquals("val0,\"\"\"Hello, World\"\"\",\"hey look a line seperator \n\"", writer.toString());
    }
}
