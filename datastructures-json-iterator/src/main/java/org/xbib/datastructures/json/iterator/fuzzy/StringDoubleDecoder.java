package org.xbib.datastructures.json.iterator.fuzzy;

import org.xbib.datastructures.json.iterator.CodegenAccess;
import org.xbib.datastructures.json.iterator.JsonIterator;
import org.xbib.datastructures.json.iterator.spi.Decoder;

import java.io.IOException;

public class StringDoubleDecoder extends Decoder.DoubleDecoder {

    @Override
    public double decodeDouble(JsonIterator iter) throws IOException {
        byte c = CodegenAccess.nextToken(iter);
        if (c != '"') {
            throw iter.reportError("StringDoubleDecoder", "expect \", but found: " + (char) c);
        }
        double val = iter.readDouble();
        c = CodegenAccess.nextToken(iter);
        if (c != '"') {
            throw iter.reportError("StringDoubleDecoder", "expect \", but found: " + (char) c);
        }
        return val;
    }
}
