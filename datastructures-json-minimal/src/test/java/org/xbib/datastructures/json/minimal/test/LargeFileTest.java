package org.xbib.datastructures.json.minimal.test;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.xbib.datastructures.json.minimal.DefaultHandler;
import org.xbib.datastructures.json.minimal.JsonReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.GZIPInputStream;

public class LargeFileTest {

    @Disabled
    @Test
    public void largeFileTest() throws Exception {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new GZIPInputStream(Files.newInputStream(Paths.get("/Users/joerg/jam.jsonlines.gz")))))) {
           bufferedReader.lines().forEach(line -> {
               try {
                   new JsonReader(new StringReader(line), new DefaultHandler()).parse();
               } catch (Exception e) {
                   throw new RuntimeException(line, e);
               }
           });
        }
    }
}
