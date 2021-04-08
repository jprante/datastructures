package org.xbib.datastructures.io;

/**
 * An interface for Information separators.
 * Also known as control characters group 0 ("C0"), ASCII-1967
 * defines units, records, groups and files as separable hierarchically
 * organized data structures. The structures are separated not by protocol,
 * but by embedded separator codes.
 * Originally, these codes were used to simulate punch card data on magnetic
 * tape. Trailing blanks on tape could be saved by using separator characters
 * instead.
 */
public interface InformationSeparator {

    /**
     * FILE SEPARATOR.
     */
    byte FS = 0x1c;
    /**
     * RECORD TERMINATOR / GROUP SEPARATOR  / Satzende (SE).
     */
    byte GS = 0x1d;
    /**
     * FIELD TERMINATOR / RECORD SEPARATOR / Feldende (FE).
     */
    byte RS = 0x1e;
    /**
     * SUBFIELD DELIMITER / UNIT SEPARATOR /  Unterfeld (UF).
     */
    byte US = 0x1f;

}
