package org.xbib.datastructures.json.test;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.xbib.datastructures.json.TinyJsonListener;
import org.xbib.datastructures.json.StreamParser;
import org.xbib.datastructures.json.StringParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

public class LargeFileTest {

    @Test
    public void testStringParser() throws IOException {
        try (InputStream inputStream = ParserTest.class.getResourceAsStream("/org/xbib/datastructures/json/test/large-file.json")) {
            byte [] b = inputStream.readAllBytes();
            String string = new String(b, StandardCharsets.UTF_8);
            StringParser stringParser = new StringParser(new TinyJsonListener());
            stringParser.parse(string);
            stringParser.getNode().get().toString();
            stringParser.parse(string);
            stringParser.getNode().get().toString();
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

    @Disabled
    @Test
    public void largeFileTest() throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new GZIPInputStream(Files.newInputStream(Paths.get("/Users/joerg/jam.jsonlines.gz")))))) {
           for (String line : bufferedReader.lines().collect(Collectors.toList())) {
               StreamParser streamParser = new StreamParser();
               streamParser.parse(new StringReader(line));
           }
        }
    }
}
