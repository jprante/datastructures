package org.xbib.datastructures.charset.demo;

import org.junit.jupiter.api.Test;
import org.xbib.datastructures.charset.GSMCharset;
import java.util.logging.Logger;

public class GSMBenchmarkMain {

    private static final Logger logger = Logger.getLogger(GSMBenchmarkMain.class.getName());

    @Test
    public void test() {
        // strings to decode/encode to/from UTF-8
        // build a string of every GSM char
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < GSMCharset.CHAR_TABLE.length; i++) {
            char c = GSMCharset.CHAR_TABLE[i];
            if (c > 0) {
                s.append(c);
            }
        }
        for (int i = 0; i < GSMCharset.EXT_CHAR_TABLE.length; i++) {
            char c = GSMCharset.EXT_CHAR_TABLE[i];
            if (c > 0) {
                s.append(c);
            }
        }
        String gsmString = s.toString();
        System.out.println("gsm string: " + gsmString);
        int count = 1000000;
        long encodeStart = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            GSMCharset.canRepresent(gsmString);
        }
        long encodeStop = System.currentTimeMillis();
        System.out.println("took " + (encodeStop-encodeStart) + " ms to run " + count + " times");
    }
    
}
