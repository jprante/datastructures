package org.xbib.datastructures.json.minimal;

import java.io.Writer;

/**
 * Controls the formatting of the JSON output. Use one of the available constants.
 */
@FunctionalInterface
public interface JsonWriterConfig {

    JsonWriter createWriter(Writer writer);

    /**
     * Write JSON in its minimal form, without any additional whitespace. This is the default.
     */
    static JsonWriterConfig minimal() {
        return JsonWriter::new;
    }

    /**
     * Write JSON in pretty-print, with each value on a separate line and an indentation of two
     * spaces.
     */
    static JsonWriterConfig prettyPrint(int n) {
        return new PrettyPrint(n);
    }

}
