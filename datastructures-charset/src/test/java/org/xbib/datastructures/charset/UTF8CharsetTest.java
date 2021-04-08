package org.xbib.datastructures.charset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.xbib.datastructures.charset.util.Hex;
import org.junit.jupiter.api.Test;
import java.util.logging.Logger;

public class UTF8CharsetTest {

    private static final Logger logger = Logger.getLogger(UTF8CharsetTest.class.getName());

    @Test
    public void emoticons() throws Exception {
        // great site: http://www.rishida.net/tools/conversion/
        // U+1F631 is a very high range example of an emoticon (something more people are using)
        // UTF-8 bytes look like this: F09F98B1
        // UTF-16 bytes look like this: D83DDE31
        // JavaScript escapes: \uD83D\uDE31
        byte[] bytes = Hex.hexToByteArray("F09F98B1");
        String str = CharsetUtil.CHARSET_UTF_8.decode(bytes);
        //logger.debug(str);
        //byte[] utf32 = str.getBytes("UTF-32");
        //logger.debug(HexUtil.toHexString(utf32));
        assertEquals("\uD83D\uDE31", str);   // UTF-16 used with JVM
    }
}
