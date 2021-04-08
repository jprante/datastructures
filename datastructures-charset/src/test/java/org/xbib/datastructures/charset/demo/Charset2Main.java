package org.xbib.datastructures.charset.demo;

import org.xbib.datastructures.charset.util.Hex;
import org.xbib.datastructures.charset.Charset;
import org.xbib.datastructures.charset.CharsetUtil;
import java.util.logging.Logger;

public class Charset2Main {

    private static final Logger logger = Logger.getLogger(Charset2Main.class.getName());

    public void test() {
        StringBuilder str0 = new StringBuilder("\u20AC");

        //Charset charset = new GSMCharset();
        //Charset charset = new PackedGSMCharset();
        //Charset charset = new ISO88591Charset();
        //Charset charset = new UCS2Charset();
        //Charset charset = new UTF8Charset();

        //Charset charset = CharsetUtil.map(CharsetUtil.NAME_PACKED_GSM);
        Charset charset = CharsetUtil.map(CharsetUtil.NAME_ISO_8859_15);
        byte[] encoded = CharsetUtil.encode(str0, charset);
        logger.info("str0: " + str0);
        logger.info("encoded: " + Hex.byteArraryToHex(encoded));
    }

}
