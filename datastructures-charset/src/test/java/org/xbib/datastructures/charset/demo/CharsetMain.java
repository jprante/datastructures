package org.xbib.datastructures.charset.demo;

import org.junit.jupiter.api.Test;
import org.xbib.datastructures.charset.Charset;
import org.xbib.datastructures.charset.CharsetUtil;
import java.util.logging.Logger;

public class CharsetMain {

    private static final Logger logger = Logger.getLogger(CharsetMain.class.getName());

    @Test
    public void test() {
        StringBuilder str0 = new StringBuilder("Hello @ World");

        //Charset charset = new GSMCharset();
        //Charset charset = new PackedGSMCharset();
        //Charset charset = new ISO88591Charset();
        //Charset charset = new UCS2Charset();
        //Charset charset = new UTF8Charset();

        //Charset charset = CharsetUtil.map(CharsetUtil.NAME_PACKED_GSM);
        Charset charset = CharsetUtil.map(CharsetUtil.NAME_GSM);
        int count = 100000;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            byte[] encoded = CharsetUtil.encode(str0, charset);
            //byte[] encoded = charset.encode(str0);
            //byte[] encoded = str0.getBytes("ISO-8859-1");
        }
        long stopTime = System.currentTimeMillis();
        logger.info("To convert to bytes " + count + " times, took " + (stopTime-startTime) + " ms");
        //logger.info("gsm: " + HexUtil.toHexString(gsmEncoded));
    }

}
