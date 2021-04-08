package org.xbib.datastructures.csv.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.xbib.datastructures.csv.Generator;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;

public class GeneratorTest {

    @Test
    public void test() throws IOException {
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
