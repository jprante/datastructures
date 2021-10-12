package org.xbib.datastructures.api;

import java.io.IOException;
import java.io.Reader;

public interface Parser {

    Node<?> parse(Reader reader) throws IOException;
}
