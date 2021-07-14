package org.xbib.datastructures.json.iterator.extra;

import org.xbib.datastructures.json.iterator.JsonIterator;
import org.xbib.datastructures.json.iterator.spi.Slice;
import org.xbib.datastructures.json.iterator.output.JsonStream;
import org.xbib.datastructures.json.iterator.spi.Decoder;
import org.xbib.datastructures.json.iterator.spi.Encoder;
import org.xbib.datastructures.json.iterator.spi.JsonException;
import org.xbib.datastructures.json.iterator.spi.JsoniterSpi;

import java.io.IOException;

/**
 * byte[] &lt;=&gt; base64
 */
public class Base64Support {
    private static boolean enabled;
    public static synchronized void enable() {
        if (enabled) {
            throw new JsonException("Base64Support.enable can only be called once");
        }
        enabled = true;
        JsoniterSpi.registerTypeDecoder(byte[].class, new Decoder() {
            @Override
            public Object decode(JsonIterator iter) throws IOException {
                Slice slice = iter.readStringAsSlice();
                return Base64.decodeFast(slice.data(), slice.head(), slice.tail());
            }
        });
        JsoniterSpi.registerTypeEncoder(byte[].class, new Encoder() {
            @Override
            public void encode(Object obj, JsonStream stream) throws IOException {
                byte[] bytes = (byte[]) obj;
                stream.write('"');
                Base64.encodeToBytes(bytes, stream);
                stream.write('"');
            }
        });
    }
}
