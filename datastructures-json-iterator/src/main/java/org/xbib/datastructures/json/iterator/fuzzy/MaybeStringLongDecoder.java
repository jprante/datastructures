package org.xbib.datastructures.json.iterator.fuzzy;

import org.xbib.datastructures.json.iterator.CodegenAccess;
import org.xbib.datastructures.json.iterator.JsonIterator;
import org.xbib.datastructures.json.iterator.spi.Decoder;

import java.io.IOException;

public class MaybeStringLongDecoder extends Decoder.LongDecoder {

    @Override
    public long decodeLong(JsonIterator iter) throws IOException {
        byte c = CodegenAccess.nextToken(iter);
        if (c != '"') {
            CodegenAccess.unreadByte(iter);
            return iter.readLong();
        }
        long val = iter.readLong();
        c = CodegenAccess.nextToken(iter);
        if (c != '"') {
            throw iter.reportError("StringLongDecoder", "expect \", but found: " + (char) c);
        }
        return val;
    }
}
