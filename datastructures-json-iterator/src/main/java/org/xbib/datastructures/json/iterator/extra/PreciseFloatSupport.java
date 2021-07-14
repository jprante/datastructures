package org.xbib.datastructures.json.iterator.extra;

import org.xbib.datastructures.json.iterator.spi.JsonException;
import org.xbib.datastructures.json.iterator.any.Any;
import org.xbib.datastructures.json.iterator.output.JsonStream;
import org.xbib.datastructures.json.iterator.spi.Encoder;
import org.xbib.datastructures.json.iterator.spi.JsoniterSpi;

import java.io.IOException;

/**
 * default float/double encoding will keep 6 decimal places
 * enable precise encoding will use JDK toString to be precise
 */
public class PreciseFloatSupport {
    private static boolean enabled;

    public static synchronized void enable() {
        if (enabled) {
            throw new JsonException("PreciseFloatSupport.enable can only be called once");
        }
        enabled = true;
        JsoniterSpi.registerTypeEncoder(Double.class, new Encoder.ReflectionEncoder() {
            @Override
            public void encode(Object obj, JsonStream stream) throws IOException {
                stream.writeRaw(obj.toString());
            }

            @Override
            public Any wrap(Object obj) {
                Double number = (Double) obj;
                return Any.wrap(number.doubleValue());
            }
        });
        JsoniterSpi.registerTypeEncoder(double.class, new Encoder.DoubleEncoder() {
            @Override
            public void encodeDouble(double obj, JsonStream stream) throws IOException {
                stream.writeRaw(Double.toString(obj));
            }
        });
        JsoniterSpi.registerTypeEncoder(Float.class, new Encoder.ReflectionEncoder() {
            @Override
            public void encode(Object obj, JsonStream stream) throws IOException {
                stream.writeRaw(obj.toString());
            }

            @Override
            public Any wrap(Object obj) {
                Float number = (Float) obj;
                return Any.wrap(number.floatValue());
            }
        });
        JsoniterSpi.registerTypeEncoder(float.class, new Encoder.FloatEncoder() {
            @Override
            public void encodeFloat(float obj, JsonStream stream) throws IOException {
                stream.writeRaw(Float.toString(obj));
            }
        });
    }
}
