package org.xbib.datastructures.io;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class InfostreamParser {

    private final BufferedSeparatorInputStream bufferedSeparatorInputStream;

    private final Iterator<Information> iterator;

    public InfostreamParser(InputStream inputStream) {
        this.bufferedSeparatorInputStream = inputStream instanceof BufferedSeparatorInputStream ?
                (BufferedSeparatorInputStream) inputStream : new BufferedSeparatorInputStream(inputStream, 8192, 8192, StandardCharsets.UTF_8);
        this.iterator = bufferedSeparatorInputStream.iterator();
    }

    public Information nextInformation() {
        return iterator.next();
    }

    public String nextString() {
        Information information = nextInformation();
        if (information.getSeparator() == InformationSeparator.US) {
            return new String(bufferedSeparatorInputStream.byteOutput().bytes(), 0, information.getCount());
        } else {
            return null;
        }
    }
}
