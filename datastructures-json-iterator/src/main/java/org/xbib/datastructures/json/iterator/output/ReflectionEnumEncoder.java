package org.xbib.datastructures.json.iterator.output;

import org.xbib.datastructures.json.iterator.any.Any;
import org.xbib.datastructures.json.iterator.spi.Encoder;

import java.io.IOException;

class ReflectionEnumEncoder implements Encoder.ReflectionEncoder {
    public ReflectionEnumEncoder(Class clazz) {
    }

    @Override
    public void encode(Object obj, JsonStream stream) throws IOException {
        stream.write('"');
        stream.writeRaw(obj.toString());
        stream.write('"');
    }

    @Override
    public Any wrap(Object obj) {
        return Any.wrap(obj.toString());
    }
}
