package org.xbib.datastructures.charset.demo;

import org.xbib.datastructures.charset.util.Hex;
import org.junit.jupiter.api.Test;
import org.xbib.datastructures.charset.Charset;
import org.xbib.datastructures.charset.CharsetUtil;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Charset4Main {

    private static final Logger logger = Logger.getLogger(Charset4Main.class.getName());

    @Test
    public void test() throws UnsupportedEncodingException {
        byte[] bytes = Hex.hexToByteArray("E0A495E0A49AE0A4BE");
        Charset charset = CharsetUtil.map(CharsetUtil.NAME_UTF_8);
        String decoded = CharsetUtil.decode(bytes, charset);
        byte[] hexDecoded = decoded.getBytes("ISO-10646-UCS-2");
        logger.log(Level.INFO, "decoded: " + decoded);
        logger.log(Level.INFO, "decodedAsHex: " + Hex.byteArraryToHex(hexDecoded));
    }
}
