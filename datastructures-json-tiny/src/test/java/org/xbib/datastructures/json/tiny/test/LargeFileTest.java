package org.xbib.datastructures.json.tiny.test;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.xbib.datastructures.json.tiny.StreamParser;
import org.xbib.datastructures.json.tiny.StringParser;
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
        InputStream inputStream = ParserTest.class.getResourceAsStream("/org/xbib/datastructures/json/tiny/test/large-file.json");
        if (inputStream != null) {
            try (inputStream) {
                byte[] b = inputStream.readAllBytes();
                String string = new String(b, StandardCharsets.UTF_8);
                StringParser stringParser = new StringParser();
                stringParser.parse(string);
                stringParser.getNode().get();
                stringParser.parse(string);
                stringParser.getNode().get();
            }
        }
    }

    @Test
    public void testStreamParser() throws IOException {
        InputStream inputStream = ParserTest.class.getResourceAsStream("/org/xbib/datastructures/json/tiny/test/test.json");
        if (inputStream != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                StreamParser streamParser = new StreamParser();
                Logger.getLogger("").log(Level.INFO, streamParser.parse(reader).get().toString());
            }
        }
    }

    @Disabled
    @Test
    public void largeFileTest() throws IOException {
        InputStream inputStream = new GZIPInputStream(Files.newInputStream(Paths.get("/Users/joerg/jam.jsonlines.gz")));
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            for (String line : bufferedReader.lines().collect(Collectors.toList())) {
                StreamParser streamParser = new StreamParser();
                streamParser.parse(new StringReader(line));
            }
        }
    }
}
