package org.xbib.datastructures.json.iterator.output;

import org.xbib.datastructures.json.iterator.any.Any;
import org.xbib.datastructures.json.iterator.spi.Encoder;
import org.xbib.datastructures.json.iterator.spi.TypeLiteral;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

class ReflectionListEncoder implements Encoder.ReflectionEncoder {

    private final TypeLiteral compTypeLiteral;

    public ReflectionListEncoder(Class clazz, Type[] typeArgs) {
        if (typeArgs.length > 0) {
            compTypeLiteral = TypeLiteral.create(typeArgs[0]);
        } else {
            compTypeLiteral = TypeLiteral.create(Object.class);
        }
    }

    @Override
    public void encode(Object obj, JsonStream stream) throws IOException {
        if (null == obj) {
            stream.writeNull();
            return;
        }
        List list = (List) obj;
        if (list.isEmpty()) {
            stream.writeEmptyArray();
            return;
        }
        stream.writeArrayStart();
        stream.writeIndention();
        stream.writeVal(compTypeLiteral, list.get(0));
        for (int i = 1; i < list.size(); i++) {
            stream.writeMore();
            stream.writeVal(compTypeLiteral, list.get(i));
        }
        stream.writeArrayEnd();
    }

    @Override
    public Any wrap(Object obj) {
        List col = (List) obj;
        return Any.wrap(col);
    }
}
