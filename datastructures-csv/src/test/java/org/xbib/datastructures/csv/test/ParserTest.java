package org.xbib.datastructures.csv.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.xbib.datastructures.csv.Parser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ParserTest {

    @Test
    public void testCommaSeparated() throws IOException {
        InputStream in = getClass().getResourceAsStream("test.csv");
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
             Parser csvParser = new Parser(reader)) {
            for (List<String> row : csvParser) {
                count++;
            }
        }
        assertEquals(2, count);
    }

    @Test
    public void testLargeFile() throws IOException {
        InputStream in = getClass().getResourceAsStream("titleFile.csv");
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            Parser csvParser = new Parser(reader)) {
            for (List<String> row : csvParser) {
                count++;
            }
        }
        assertEquals(44447, count);
    }

    @Test
    public void testTabSeparated() throws IOException {
        InputStream in = getClass().getResourceAsStream("2076831-X-web.txt");
        InputStreamReader r = new InputStreamReader(in, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(r);
        // skip 3 lines
        reader.readLine();
        reader.readLine();
        reader.readLine();
        String line;
        while ((line = reader.readLine()) != null) {
            String[] s = line.split("\\t");
            int i = 0;
            String sigel = i < s.length ? s[i++] : "";
            String isil = i < s.length ? s[i++] : "";
            String name = i < s.length ? s[i++] : ""; // unused
            String code1 = i < s.length ? s[i++] : "";
            String code2 = i < s.length ? s[i++] : "";
            String code3 = i < s.length ? s[i++] : "";
            String comment = i < s.length ? s[i++] : "";
            String firstDate = i < s.length ? s[i++] : "";
            String firstVolume = i < s.length ? s[i++] : "";
            String firstIssue = i < s.length ? s[i++] : "";
            String lastDate = i < s.length ? s[i++] : "";
            String lastVolume = i < s.length ? s[i++] : "";
            String lastIssue = i < s.length ? s[i++] : "";
            String movingWall = i < s.length ? s[i] : "";
            assertNotNull(sigel);
            assertNotNull(isil);
            assertNotNull(name);
            assertNotNull(code1);
            assertNotNull(code2);
            assertNotNull(code3);
            assertNotNull(comment);
            assertNotNull(firstDate);
            assertNotNull(firstVolume);
            assertNotNull(firstIssue);
            assertNotNull(lastDate);
            assertNotNull(lastVolume);
            assertNotNull(lastIssue);
            assertNotNull(movingWall);
        }
    }
}
