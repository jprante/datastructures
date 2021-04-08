package org.xbib.datastructures.charset.demo;

import org.junit.jupiter.api.Test;
import org.xbib.datastructures.charset.CharsetUtil;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Charset5Main {

    private static final Logger logger = Logger.getLogger(Charset5Main.class.getName());

    @Test
    public void test() {
        StringBuilder sourceString = new StringBuilder("h\u6025\u20ACllo");
        String targetString = CharsetUtil.normalize(sourceString, CharsetUtil.CHARSET_UTF_8);
        logger.log(Level.INFO, "source string: " + sourceString);
        logger.log(Level.INFO, "target string: " + targetString);
    }

}
