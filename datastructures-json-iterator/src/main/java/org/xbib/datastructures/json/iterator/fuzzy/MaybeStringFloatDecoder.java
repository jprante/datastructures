package org.xbib.datastructures.json.iterator.fuzzy;

import org.xbib.datastructures.json.iterator.CodegenAccess;
import org.xbib.datastructures.json.iterator.JsonIterator;
import org.xbib.datastructures.json.iterator.spi.Decoder;

import java.io.IOException;

public class MaybeStringFloatDecoder extends Decoder.FloatDecoder {

    @Override
    public float decodeFloat(JsonIterator iter) throws IOException {
        byte c = CodegenAccess.nextToken(iter);
        if (c != '"') {
            CodegenAccess.unreadByte(iter);
            return iter.readFloat();
        }
        float val = iter.readFloat();
        c = CodegenAccess.nextToken(iter);
        if (c != '"') {
            throw iter.reportError("StringFloatDecoder", "expect \", but found: " + (char) c);
        }
        return val;
    }
}