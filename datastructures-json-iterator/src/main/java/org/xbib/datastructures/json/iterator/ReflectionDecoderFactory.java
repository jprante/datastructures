package org.xbib.datastructures.json.iterator;

import org.xbib.datastructures.json.iterator.spi.ClassInfo;
import org.xbib.datastructures.json.iterator.spi.Decoder;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

class ReflectionDecoderFactory {
    public static Decoder create(ClassInfo classAndArgs) {
        Class clazz = classAndArgs.clazz;
        Type[] typeArgs = classAndArgs.typeArgs;
        if (clazz.isArray()) {
            return new ReflectionArrayDecoder(clazz);
        }
        if (Collection.class.isAssignableFrom(clazz)) {
            return new ReflectionCollectionDecoder(clazz, typeArgs);
        }
        if (Map.class.isAssignableFrom(clazz)) {
            return new ReflectionMapDecoder(clazz, typeArgs);
        }
        if (clazz.isEnum()) {
            return new ReflectionEnumDecoder(clazz);
        }
        return new ReflectionObjectDecoder(classAndArgs).create();
    }
}
