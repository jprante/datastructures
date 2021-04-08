package org.xbib.datastructures.charset.demo;

import org.xbib.datastructures.charset.util.Hex;
import org.junit.jupiter.api.Test;
import org.xbib.datastructures.charset.Charset;
import org.xbib.datastructures.charset.CharsetUtil;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.logging.Logger;

public class Charset3Main {

    private static final Logger logger = Logger.getLogger(Charset3Main.class.getName());

    @Test
    public void test() throws UnsupportedEncodingException {
        Charset cs = CharsetUtil.map("GSM");
        for (int i = 0; i < 65536; i++) {
            StringBuilder str = new StringBuilder().append(i);
            byte[] sourceBytes = str.toString().getBytes("ISO-10646-UCS-2");
            byte[] encodedBytes = cs.encode(str);
            if (!Arrays.equals(sourceBytes, encodedBytes) && encodedBytes[0] != (byte)0x3F) {
                System.out.println("{ (byte)0x" + Hex.byteArraryToHex(encodedBytes) + ", (char)0x" + Hex.byteArraryToHex(sourceBytes) + " }, // " + str);
                //logger.debug(str + ": " + HexUtil.toHexString(sourceBytes) + "->" + HexUtil.toHexString(encodedBytes));
            }
        }
    }
}
