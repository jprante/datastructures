package org.xbib.datastructures.charset.demo;

import org.junit.jupiter.api.Test;
import org.xbib.datastructures.charset.CharsetUtil;
import java.util.logging.Logger;

public class BenchmarkMain {

    private static final Logger logger = Logger.getLogger(BenchmarkMain.class.getName());

    @Test
    public void benchmark() {
        StringBuilder in = new StringBuilder("\u20ACABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuv\u6025");
        int count = 500000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            //String out = CharsetUtil.normalize(in, CharsetUtil.CHARSET_GSM);
            //GSMCharset.canRepresent(in);
            //int length = UTF8Charset.calculateByteLength(in);
            //int length = in.getBytes("UTF8").length;
            //byte[] a = UTF8Charset.encode(in);
            byte[] b = CharsetUtil.encode(in, CharsetUtil.CHARSET_UTF_8);
        }
        long stop = System.currentTimeMillis();
        logger.info("Took " + (stop-start) + " ms to run " + count + " times");
    }
}
