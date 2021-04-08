package org.xbib.datastructures.json.test;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.xbib.datastructures.json.Lexer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.GZIPInputStream;

public class LargeFileTest {

    @Disabled
    @Test
    public void largeFileTest() throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new GZIPInputStream(Files.newInputStream(Paths.get("/Users/joerg/jam.jsonlines.gz")))))) {
           bufferedReader.lines().forEach(line -> {
               try {
                   Lexer lexer = new Lexer(new StringReader(line));
                   lexer.nextObject();
               } catch (IOException e) {
                   throw new RuntimeException(line, e);
               }
           });
        }
    }
}
