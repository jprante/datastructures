package org.xbib.datastructures.charset.demo;

import org.xbib.datastructures.charset.util.Hex;
import org.junit.jupiter.api.Test;
import org.xbib.datastructures.charset.CharsetUtil;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class Charset6Main {

    private static final Logger logger = Logger.getLogger(Charset6Main.class.getName());

    @Test
    public void test() {
        byte[] vmpbytes = Hex.hexToByteArray("c3a2c282c2ac");
        String decoded1 = CharsetUtil.decode(vmpbytes, CharsetUtil.CHARSET_UTF_8);
        logger.info("decode #1 length: " + decoded1.length());
        byte[] nextbytes = decoded1.getBytes(StandardCharsets.ISO_8859_1);
        logger.info("decode #1 bytes: " + Hex.byteArraryToHex(nextbytes));
    }

}
