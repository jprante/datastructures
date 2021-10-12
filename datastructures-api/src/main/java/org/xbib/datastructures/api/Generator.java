package org.xbib.datastructures.api;

import java.io.IOException;
import java.io.Writer;

public interface Generator {

    void generate(Writer writer) throws IOException;
}
