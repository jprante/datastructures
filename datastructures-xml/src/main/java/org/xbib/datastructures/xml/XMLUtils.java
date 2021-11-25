package org.xbib.datastructures.xml;

public class XMLUtils {

    public static String escapeXml(CharSequence str) {
        if (str == null) {
            return null;
        }
        StringBuilder res = null;
        int strLength = str.length();
        for (int i = 0; i < strLength; i++) {
            char c = str.charAt(i);
            String repl = encodeXMLChar(c);
            if (repl == null) {
                if (res != null) {
                    res.append(c);
                }
            } else {
                if (res == null) {
                    res = new StringBuilder(str.length() + 5);
                    for (int k = 0; k < i; k++) {
                        res.append(str.charAt(k));
                    }
                }
                res.append(repl);
            }
        }
        return res == null ? str.toString() : res.toString();
    }

    /**
     * Encodes a char to XML-valid form replacing &amp;,',",&lt;,&gt; with special XML encoding.
     *
     * @param ch char to convert
     * @return XML-encoded text
     */
    public static String encodeXMLChar(char ch) {
        switch (ch) {
            case '&':
                return "&amp;";
            case '\"':
                return "&quot;";
            case '\'':
                return "&#39;";
            case '<':
                return "&lt;";
            case '>':
                return "&gt;";
            default:
                return null;
        }
    }
}
