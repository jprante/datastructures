package org.xbib.datastructures.io;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class InfostreamGenerator implements Flushable, Closeable {

    private static final String TRUE_STRING = "1";

    private static final String FALSE_STRING = "0";

    private final OutputStream outputStream;

    private final ByteOutput byteOutput;

    private final int limit;

    private boolean inArray;

    public InfostreamGenerator(OutputStream outputStream) {
        this(outputStream, 512);
    }

    public InfostreamGenerator(OutputStream outputStream, int limit) {
        this.outputStream = outputStream;
        this.byteOutput = new BytesStreamOutput(limit * 2);
        this.limit = limit;
    }

    @Override
    public void close() throws IOException {
        byteOutput.writeByte(InformationSeparator.FS);
        outputStream.write(byteOutput.bytes(), 0, byteOutput.count());
        byteOutput.reset();
        outputStream.close();
    }

    @Override
    public void flush() throws IOException {
        outputStream.write(byteOutput.bytes(), 0, byteOutput.count());
        byteOutput.reset();
        outputStream.flush();
    }

    public void writeNull() throws IOException {
        byteOutput.writeByte(InformationSeparator.US);
        byteOutput.writeByte(Types.NULL);
        flushIfNeeded();
    }

    public void writeString(String string) throws IOException {
        internalWrite(inArray ? null : Types.STRING, string);
    }

    public void writeChar(char ch) throws IOException {
        internalWrite(inArray ? null : Types.CHAR, Character.toString(ch));
    }

    public void writeBoolean(boolean bool) throws IOException {
        internalWrite(inArray ? null : Types.BOOLEAN, bool ? TRUE_STRING : FALSE_STRING);
    }

    public void writeByte(byte number) throws IOException {
        internalWrite(inArray ? null : Types.BYTE, Byte.toString(number));
    }

    public void writeShort(short number) throws IOException {
        internalWrite(inArray ? null : Types.SHORT, Short.toString(number));
    }

    public void writeInt(int number) throws IOException {
        internalWrite(inArray ? null : Types.INT, Integer.toString(number));
    }

    public void writeLong(long number) throws IOException {
        internalWrite(inArray ? null : Types.LONG, Long.toString(number));
    }

    public void writeFloat(float number) throws IOException {
        internalWrite(inArray ? null : Types.FLOAT, Float.toString(number));
    }

    public void writeDouble(double number) throws IOException {
        internalWrite(inArray ? null : Types.DOUBLE, Double.toString(number));
    }

    public void writeObject(Object object) throws IOException {
        if (object == null) {
            writeNull();
            return;
        }
        if (object instanceof String) {
            writeString((String) object);
            return;
        }
        if (object instanceof Boolean) {
            writeBoolean((Boolean) object);
            return;
        }
        if (object instanceof Integer) {
            writeInt((int) object);
            return;
        }
        if (object instanceof Long) {
            writeLong((long) object);
            return;
        }
        if (object instanceof Byte) {
            writeByte((byte) object);
            return;
        }
        if (object instanceof Character) {
            writeChar((char) object);
            return;
        }
        if (object instanceof Short) {
            writeShort((short) object);
            return;
        }
        if (object instanceof Float) {
            writeFloat((float) object);
            return;
        }
        if (object instanceof Double) {
            writeDouble((double) object);
            return;
        }
        // fall back
        internalWrite(Types.OBJECT, object.getClass().getName() + " " + object.toString());
    }

    public void writeCollection(Collection<?> collection) throws IOException {
        writeCollection(collection, true);
    }

    private void writeCollection(Collection<?> collection, boolean typeControl) throws IOException {
        Objects.requireNonNull(collection);
        if (typeControl) {
            byteOutput.writeByte(Types.ARRAY);
            inArray = false;
        }
        for (Object object : collection) {
            writeObject(object);
            if (typeControl) {
                inArray = true;
            }
        }
        if (typeControl) {
            inArray = false;
        }
        byteOutput.writeByte(InformationSeparator.RS);
    }

    public void writeMap(Map<String, Object> map) throws IOException {
        Objects.requireNonNull(map);
        writeCollection(map.keySet(), true);
        writeCollection(map.values(), false);
        byteOutput.writeByte(InformationSeparator.GS);
    }

    private void flushIfNeeded() throws IOException {
        if (byteOutput.count() > limit) {
            outputStream.write(byteOutput.bytes(), 0, byteOutput.count());
            byteOutput.reset();
        }
    }

    private void internalWrite(Byte type, CharSequence charSequence) throws IOException {
        if (type != null) {
            byteOutput.writeByte(type);
            if (type == Types.STRING) {
                encodeUtf8(charSequence, byteOutput);
            } else {
                encodeAscii(charSequence, byteOutput);
            }
        } else {
            encodeUtf8(charSequence, byteOutput);
        }
        byteOutput.writeByte(InformationSeparator.US);
        flushIfNeeded();
    }

    private static void encodeAscii(CharSequence charSequence, ByteOutput byteOutput) {
        int len = charSequence.length();
        for (int i = 0; i < len; i++) {
             byteOutput.writeByte((byte) charSequence.charAt(i));
        }
    }

    private static void encodeUtf8(CharSequence charSequence, ByteOutput byteOutput) {
        int c;
        int bytePos = 0;
        int charPos = 0;
        byte[] buffer = new byte[4];
        int charAbsLength = charSequence.length();
        for (; charPos < charAbsLength; charPos++) {
            c = charSequence.charAt(charPos);
            if (!(c <= 0x007F))
                break;
            buffer[bytePos++] = (byte) c;
        }
        for (; charPos < charAbsLength; charPos++) {
            c = charSequence.charAt(charPos);
            if (c <= 0x007F) {
                buffer[bytePos++] = (byte) c;
            } else if (c > 0x07FF) {
                buffer[bytePos++] = (byte) (0xE0 | ((c >> 12) & 0x0F));
                buffer[bytePos++] = (byte) (0x80 | ((c >> 6) & 0x3F));
                buffer[bytePos++] = (byte) (0x80 | (c & 0x3F));
            } else {
                buffer[bytePos++] = (byte) (0xC0 | ((c >> 6) & 0x1F));
                buffer[bytePos++] = (byte) (0x80 | (c & 0x3F));
            }
        }
        byteOutput.write(buffer, 0, bytePos);
    }
}
