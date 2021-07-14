package org.xbib.datastructures.json.test;

import org.junit.jupiter.api.Test;
import org.xbib.datastructures.json.TinyJsonListener;
import org.xbib.datastructures.json.StreamParser;
import org.xbib.datastructures.json.StringParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ParserTest {

    @Test
    public void testStringParser() throws IOException {
        try (InputStream inputStream = ParserTest.class.getResourceAsStream("/org/xbib/datastructures/json/test/test.json")) {
            byte [] b = inputStream.readAllBytes();
            String string = new String(b, StandardCharsets.UTF_8);
            StringParser stringParser = new StringParser(new TinyJsonListener());
            stringParser.parse(string);
            Logger.getLogger("").log(Level.INFO, stringParser.getNode().get().toString());
            stringParser.parse(string);
            Logger.getLogger("").log(Level.INFO, stringParser.getNode().get().toString());
        }
    }

    @Test
    public void testStreamParser() throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(ParserTest.class.getResourceAsStream("/org/xbib/datastructures/json/test/test.json")))) {
            StreamParser streamParser = new StreamParser(new TinyJsonListener());
            streamParser.parse(reader);
            Logger.getLogger("").log(Level.INFO, streamParser.getNode().get().toString());
        }
    }
}
