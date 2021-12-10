package org.xbib.datastructures.json.tiny.test;

import org.junit.jupiter.api.Test;
import org.xbib.datastructures.json.tiny.StreamParser;
import org.xbib.datastructures.json.tiny.StringParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserTest {

    @Test
    public void testStringParser() throws IOException {
        try (InputStream inputStream = ParserTest.class.getResourceAsStream("/org/xbib/datastructures/json/tiny/test/test.json")) {
            if (inputStream != null) {
                byte[] b = inputStream.readAllBytes();
                String string = new String(b, StandardCharsets.UTF_8);
                StringParser stringParser = new StringParser();
                stringParser.parse(string);
                assertEquals("{a=b, c=d, e=[f, g], h={i={j=k}}, l=null, m=true, n=false, o=0, p=1, q=-1, r=0.0, s=1.0, t=2.1, u=-1.0, v=-2.1, w=, x=₫, y=Jörg, z=This is \"quoted\" text\n" +
                                "after line break}",
                        stringParser.getNode().get().toString());
            }
        }
    }

    @Test
    public void testStreamParser() throws IOException {
        InputStream inputStream = ParserTest.class.getResourceAsStream("/org/xbib/datastructures/json/tiny/test/test.json");
        if (inputStream != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                StreamParser streamParser = new StreamParser();
                assertEquals("{a=b, c=d, e=[f, g], h={i={j=k}}, l=null, m=true, n=false, o=0, p=1, q=-1, r=0.0, s=1.0, t=2.1, u=-1.0, v=-2.1, w=, x=₫, y=Jörg, z=This is \"quoted\" text\n" +
                                "after line break}",
                        streamParser.parse(reader).get().toString());
            }
        }
    }
}
