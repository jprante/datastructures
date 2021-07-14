package org.xbib.datastructures.json.iterator.output;

import org.xbib.datastructures.json.iterator.any.Any;
import org.xbib.datastructures.json.iterator.spi.Encoder;
import org.xbib.datastructures.json.iterator.spi.JsoniterSpi;
import org.xbib.datastructures.json.iterator.spi.TypeLiteral;

import java.io.IOException;

public class CodegenAccess {
    public static void writeVal(String cacheKey, Object obj, JsonStream stream) throws IOException {
        JsoniterSpi.getEncoder(cacheKey).encode(obj, stream);
    }

    public static void writeVal(String cacheKey, boolean obj, JsonStream stream) throws IOException {
        Encoder.BooleanEncoder encoder = (Encoder.BooleanEncoder) JsoniterSpi.getEncoder(cacheKey);
        encoder.encodeBoolean(obj, stream);
    }

    public static void writeVal(String cacheKey, byte obj, JsonStream stream) throws IOException {
        Encoder.ShortEncoder encoder = (Encoder.ShortEncoder) JsoniterSpi.getEncoder(cacheKey);
        encoder.encodeShort(obj, stream);
    }

    public static void writeVal(String cacheKey, short obj, JsonStream stream) throws IOException {
        Encoder.ShortEncoder encoder = (Encoder.ShortEncoder) JsoniterSpi.getEncoder(cacheKey);
        encoder.encodeShort(obj, stream);
    }

    public static void writeVal(String cacheKey, int obj, JsonStream stream) throws IOException {
        Encoder.IntEncoder encoder = (Encoder.IntEncoder) JsoniterSpi.getEncoder(cacheKey);
        encoder.encodeInt(obj, stream);
    }

    public static void writeVal(String cacheKey, char obj, JsonStream stream) throws IOException {
        Encoder.IntEncoder encoder = (Encoder.IntEncoder) JsoniterSpi.getEncoder(cacheKey);
        encoder.encodeInt(obj, stream);
    }

    public static void writeVal(String cacheKey, long obj, JsonStream stream) throws IOException {
        Encoder.LongEncoder encoder = (Encoder.LongEncoder) JsoniterSpi.getEncoder(cacheKey);
        encoder.encodeLong(obj, stream);
    }

    public static void writeVal(String cacheKey, float obj, JsonStream stream) throws IOException {
        Encoder.FloatEncoder encoder = (Encoder.FloatEncoder) JsoniterSpi.getEncoder(cacheKey);
        encoder.encodeFloat(obj, stream);
    }

    public static void writeVal(String cacheKey, double obj, JsonStream stream) throws IOException {
        Encoder.DoubleEncoder encoder = (Encoder.DoubleEncoder) JsoniterSpi.getEncoder(cacheKey);
        encoder.encodeDouble(obj, stream);
    }

    public static void writeMapKey(String cacheKey, Object mapKey, JsonStream stream) throws IOException {
        Encoder mapKeyEncoder = JsoniterSpi.getMapKeyEncoder(cacheKey);
        mapKeyEncoder.encode(mapKey, stream);
    }

    public static void writeStringWithoutQuote(String obj, JsonStream stream) throws IOException {
        StreamImplString.writeStringWithoutQuote(stream, obj);
    }

    public static void staticGenEncoders(TypeLiteral[] typeLiterals, StaticCodegenTarget staticCodegenTarget) {
        Codegen.staticGenEncoders(typeLiterals, staticCodegenTarget);
    }

    public static Any wrap(Object val) {
        if (val == null) {
            return Any.wrapNull();
        }
        Class<?> clazz = val.getClass();
        String cacheKey = TypeLiteral.create(clazz).getEncoderCacheKey();
        return Codegen.getReflectionEncoder(cacheKey, clazz).wrap(val);
    }

    public static class StaticCodegenTarget {

        public final String outputDir;

        public StaticCodegenTarget(String outputDir) {
            this.outputDir = outputDir;
        }
    }
}
