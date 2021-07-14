package org.xbib.datastructures.json.iterator.static_codegen;

import org.xbib.datastructures.json.iterator.spi.TypeLiteral;

public interface StaticCodegenConfig {
    /**
     * register decoder/encoder before codegen
     * register extension before codegen
     */
    void setup();

    /**
     * what to codegen
     * @return generate encoder/decoder for the types
     */
    TypeLiteral[] whatToCodegen();
}
