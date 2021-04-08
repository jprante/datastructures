package org.xbib.datastructures.charset.demo;

import org.xbib.datastructures.charset.GSMCharset;

public class PrintGSMMain {
    
    static public void main(String[] args) throws Exception {
        // utility class for converting CHAR_TABLE and EXT_CHAR_TABLEs into
        // switch statements to be used in several methods
        printTable(GSMCharset.CHAR_TABLE);
        printTable(GSMCharset.EXT_CHAR_TABLE);
    }
    
    static public void printTable(char[] t) {
        for (char c : t) {
            if (c > 0) {
                if ((c < ' ' || c > '_') && (c < 'a' || c > '~')) {
                    System.out.println("case '\\u" + Integer.toHexString(((short) c)) + "':" + "\t// " + c);
                }
            }
        }
    }
    
}
