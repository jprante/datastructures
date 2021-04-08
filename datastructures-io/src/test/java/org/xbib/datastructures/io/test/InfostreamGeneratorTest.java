package org.xbib.datastructures.io.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.xbib.datastructures.io.BytesStreamOutput;
import org.xbib.datastructures.io.Information;
import org.xbib.datastructures.io.InfostreamGenerator;
import org.xbib.datastructures.io.InfostreamParser;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InfostreamGeneratorTest {

    private static final Logger logger = Logger.getLogger(InfostreamGenerator.class.getName());

    @Test
    public void writeString() throws IOException {
        BytesStreamOutput output = new BytesStreamOutput();
        InfostreamGenerator generator = new InfostreamGenerator(output);
        generator.writeString("a");
        generator.close();
        byte[] b = output.bytes();
        assertEquals(4, output.count());
        for (int i = 0; i < output.count(); i++) {
            logger.log(Level.INFO, Integer.toHexString(b[i]));
        }
    }

    @Test
    public void writeBoolean() throws IOException {
        BytesStreamOutput output = new BytesStreamOutput();
        InfostreamGenerator generator = new InfostreamGenerator(output);
        generator.writeBoolean(true);
        generator.close();
        byte[] b = output.bytes();
        assertEquals(4, output.count());
        for (int i = 0; i < output.count(); i++) {
            logger.log(Level.INFO, Integer.toHexString(b[i]));
        }
    }

    @Test
    public void writeLong() throws IOException {
        BytesStreamOutput output = new BytesStreamOutput();
        InfostreamGenerator generator = new InfostreamGenerator(output);
        generator.writeLong(1L);
        generator.close();
        byte[] b = output.bytes();
        assertEquals(4, output.count());
        for (int i = 0; i < output.count(); i++) {
            logger.log(Level.INFO, Integer.toHexString(b[i]));
        }
    }

    @Test
    public void writeSet() throws IOException {
        BytesStreamOutput output = new BytesStreamOutput();
        InfostreamGenerator generator = new InfostreamGenerator(output);
        generator.writeCollection(Arrays.asList("a","b","c"));
        generator.close();
        byte[] b = output.bytes();
        logger.log(Level.INFO, "a,b,c: count = " + output.count());
        for (int i = 0; i < output.count(); i++) {
            logger.log(Level.INFO, Integer.toHexString(b[i]));
        }
    }

    @Test
    public void writeSimpleMap() throws IOException {
        BytesStreamOutput output = new BytesStreamOutput(1024);
        InfostreamGenerator generator = new InfostreamGenerator(output);
        generator.writeMap(Map.of("a", "b"));
        generator.close();
        byte[] b = output.bytes();
        logger.log(Level.INFO, "{a:b} count = " + output.count());
        for (int i = 0; i < output.count(); i++) {
            logger.log(Level.INFO, Integer.toHexString(b[i]));
        }
        int l = "{\"a\":\"b\"}".getBytes(StandardCharsets.UTF_8).length;
        logger.log(Level.INFO, "{a:b} json length = " + l);
    }

    @Test
    public void writeDoubleMap() throws IOException {
        BytesStreamOutput output = new BytesStreamOutput(1024);
        InfostreamGenerator generator = new InfostreamGenerator(output);
        generator.writeMap(Map.of("a", "b", "c", "d"));
        generator.close();
        byte[] b = output.bytes();
        logger.log(Level.INFO, "{a:b,c:d} count = " + output.count());
        for (int i = 0; i < output.count(); i++) {
            logger.log(Level.INFO, Integer.toHexString(b[i]));
        }
        int l = "{\"a\":\"b\",\"c\":\"d\"}".getBytes(StandardCharsets.UTF_8).length;
        logger.log(Level.INFO, "{a:b,c:d} json length = " + l);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(b, 0, output.count());
        InfostreamParser parser = new InfostreamParser(inputStream);
        logger.log(Level.INFO, "string = " + parser.nextString());
        logger.log(Level.INFO, "string = " + parser.nextString());
        logger.log(Level.INFO, "string = " + parser.nextString());
        logger.log(Level.INFO, "string = " + parser.nextString());
        logger.log(Level.INFO, "string = " + parser.nextString());
    }
}
