package org.xbib.datastructures.json.iterator.extra;

import org.xbib.datastructures.json.iterator.spi.JsonException;
import org.xbib.datastructures.json.iterator.JsonIterator;
import org.xbib.datastructures.json.iterator.any.Any;
import org.xbib.datastructures.json.iterator.output.JsonStream;
import org.xbib.datastructures.json.iterator.spi.Decoder;
import org.xbib.datastructures.json.iterator.spi.Encoder;
import org.xbib.datastructures.json.iterator.spi.JsoniterSpi;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * there is no official way to encode/decode datetime, this is just an option for you
 */
public class JdkDatetimeSupport {

    private static String pattern;
    private final static ThreadLocal<SimpleDateFormat> sdf = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(pattern);
        }
    };

    public static synchronized void enable(String pattern) {
        if (JdkDatetimeSupport.pattern != null) {
            throw new JsonException("JdkDatetimeSupport.enable can only be called once");
        }
        JdkDatetimeSupport.pattern = pattern;
        JsoniterSpi.registerTypeEncoder(Date.class, new Encoder.ReflectionEncoder() {
            @Override
            public void encode(Object obj, JsonStream stream) throws IOException {
                stream.writeVal(sdf.get().format(obj));
            }

            @Override
            public Any wrap(Object obj) {
                return Any.wrap(sdf.get().format(obj));
            }
        });
        JsoniterSpi.registerTypeDecoder(Date.class, new Decoder() {
            @Override
            public Object decode(JsonIterator iter) throws IOException {
                try {
                    return sdf.get().parse(iter.readString());
                } catch (ParseException e) {
                    throw new JsonException(e);
                }
            }
        });
    }
}
