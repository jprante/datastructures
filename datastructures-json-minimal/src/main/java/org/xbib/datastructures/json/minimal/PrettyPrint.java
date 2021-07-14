package org.xbib.datastructures.json.minimal;

import java.io.Writer;
import java.util.Arrays;

/**
 * Enables human readable JSON output by inserting whitespace between values.after commas and
 * colons. Example:
 *
 * <pre>
 * jsonValue.writeTo(writer, WriterConfig.prettyPrint());
 * </pre>
 */
public class PrettyPrint implements JsonWriterConfig {

    private final char[] indentChars;

    public PrettyPrint(char[] indentChars) {
        this.indentChars = indentChars;
    }

    /**
     * Print every value on a separate line. Use the given number of spaces for indentation.
     *
     * @param number the number of spaces to use
     */
    public PrettyPrint(int number) {
        this(fillChars(number));
    }

    private static char[] fillChars(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("number is negative");
        }
        char[] chars = new char[number];
        Arrays.fill(chars, ' ');
        return chars;
    }

    @Override
    public JsonWriter createWriter(Writer writer) {
        return new PrettyPrintWriter(writer, indentChars);
    }

}
