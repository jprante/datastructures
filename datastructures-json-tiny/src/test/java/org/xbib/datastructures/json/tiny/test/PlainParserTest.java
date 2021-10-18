package org.xbib.datastructures.json.tiny.test;

import org.junit.jupiter.api.Test;
import org.xbib.datastructures.json.tiny.PlainParser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlainParserTest {

    @Test
    public void testStringParser() throws IOException {
        try (InputStream inputStream = ParserTest.class.getResourceAsStream("/org/xbib/datastructures/json/tiny/test/test.json")) {
            if (inputStream != null) {
                byte[] b = inputStream.readAllBytes();
                String string = new String(b, StandardCharsets.UTF_8);
                PlainParser parser = new PlainParser();
                parser.parse(string);
                assertEquals("{a=b, c=d, e=[f, g], h={i={j=k}}, l=null, m=true, n=false, o=0, p=1, q=-1, r=0.0, s=1.0, t=2.1, u=-1.0, v=-2.1, w=, x=₫, y=Jörg}",
                        parser.getResult().toString());
            }
        }
    }

}
