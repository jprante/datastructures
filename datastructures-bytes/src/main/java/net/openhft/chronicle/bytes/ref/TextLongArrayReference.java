/*
 * Copyright 2016-2020 chronicle.software
 *
 * https://chronicle.software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.openhft.chronicle.bytes.ref;

import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.bytes.BytesStore;
import net.openhft.chronicle.core.OS;
import net.openhft.chronicle.core.values.LongValue;
import org.jetbrains.annotations.NotNull;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

/*
The format for a long array in text is
{ capacity: 12345678901234567890, values: [ 12345678901234567890, ... ] }
 */
@SuppressWarnings("rawtypes")
public class TextLongArrayReference extends AbstractReference implements ByteableLongArrayValues {
    private static final byte[] SECTION1 = "{ locked: false, capacity: ".getBytes(ISO_8859_1);
    private static final byte[] SECTION2 = ", used: ".getBytes(ISO_8859_1);
    private static final byte[] SECTION3 = ", values: [ ".getBytes(ISO_8859_1);
    private static final byte[] SECTION4 = " ] }\n".getBytes(ISO_8859_1);
    private static final byte[] ZERO = "00000000000000000000".getBytes(ISO_8859_1);
    private static final byte[] SEP = ", ".getBytes(ISO_8859_1);

    private static final int DIGITS = ZERO.length;
    private static final int CAPACITY = SECTION1.length;
    private static final int USED = CAPACITY + DIGITS + SECTION2.length;
    private static final int VALUES = USED + DIGITS + SECTION3.length;
    private static final int VALUE_SIZE = DIGITS + SEP.length;
    private static final int LOCK_OFFSET = 10;
    private static final int FALS = 'f' | ('a' << 8) | ('l' << 16) | ('s' << 24);
    private static final int TRU = ' ' | ('t' << 8) | ('r' << 16) | ('u' << 24);

    private long length = VALUES;

    public static void write( Bytes bytes, long capacity) {
        long start = bytes.writePosition();
        bytes.write(SECTION1);
        bytes.append(capacity);
        while (bytes.writePosition() - start < CAPACITY + DIGITS) {
            bytes.writeUnsignedByte(' ');
        }
        bytes.write(SECTION2);
        bytes.write(ZERO);
        bytes.write(SECTION3);
        for (long i = 0; i < capacity; i++) {
            if (i > 0)
                bytes.appendUtf8(", ");
            bytes.write(ZERO);
        }
        bytes.write(SECTION4);
    }

    public static long peakLength( BytesStore bytes, long offset) {
        //todo check this, I think there could be a bug here
        return (bytes.parseLong(offset + CAPACITY) * VALUE_SIZE) - SEP.length
                + VALUES + SECTION4.length;
    }

    @Override
    public long getUsed() {
        try {
            return bytes.parseLong(USED + offset);
        } catch (NullPointerException e) {
            throwExceptionIfClosed();
            throw e;
        }
    }

    private void setUsed(long used) {
        try {
            bytes.append(VALUES + offset, used, DIGITS);
        } catch (NullPointerException e) {
            throwExceptionIfClosed();
            throw e;
        }
    }

    @Override
    public void setMaxUsed(long usedAtLeast) {
        try {
            while (true) {
                if (!bytes.compareAndSwapInt(LOCK_OFFSET + offset, FALS, TRU))
                    continue;
                try {
                    if (getUsed() < usedAtLeast) {
                        setUsed(usedAtLeast);
                    }
                    return;
                } finally {
                    bytes.writeInt(LOCK_OFFSET + offset, FALS);
                }
            }
        } catch (NullPointerException e) {
            throwExceptionIfClosed();
            throw e;
        }
    }

    @Override
    public long getCapacity() {
        return (length - VALUES) / VALUE_SIZE;
    }

    @Override
    public ByteableLongArrayValues capacity(long arrayLength) {
        BytesStore bytesStore = bytesStore();
        long length = sizeInBytes(arrayLength);
        if (bytesStore == null) {
            this.length = length;
        } else {
            assert this.length == length;
        }
        return this;
    }

    @Override
    public long getValueAt(long index) {
        try {
            return bytes.parseLong(VALUES + offset + index * VALUE_SIZE);
        } catch (NullPointerException e) {
            throwExceptionIfClosed();
            throw e;
        }

    }

    @Override
    public void setValueAt(long index, long value) {
        try {
            bytes.append(VALUES + offset + index * VALUE_SIZE, value, DIGITS);
        } catch (NullPointerException e) {
            throwExceptionIfClosed();
            throw e;
        }
    }

    @Override
    public void bindValueAt(long index, LongValue value) {
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public long getVolatileValueAt(long index) {
        OS.memory().loadFence();
        return getValueAt(index);
    }

    @Override
    public void setOrderedValueAt(long index, long value) {
        setValueAt(index, value);
        OS.memory().storeFence();
    }

    @Override
    public boolean compareAndSet(long index, long expected, long value) {
        try {
            if (!bytes.compareAndSwapInt(LOCK_OFFSET + offset, FALS, TRU))
                return false;
            boolean ret = false;
            try {
                if (getVolatileValueAt(index) == expected) {
                    setOrderedValueAt(index, value);
                    ret = true;
                }
                return ret;
            } finally {
                bytes.writeInt(LOCK_OFFSET + offset, FALS);
            }
        } catch (NullPointerException e) {
            throwExceptionIfClosed();
            throw e;
        }

    }

    @Override
    public void bytesStore( final BytesStore bytes, long offset, long length) {
        throwExceptionIfClosedInSetter();

        if (length != peakLength(bytes, offset))
            throw new IllegalArgumentException(length + " != " + peakLength(bytes, offset));
        super.bytesStore(bytes, offset, length);
        this.length = length;
    }

    @Override
    public boolean isNull() {
        return bytes == null;
    }

    @Override
    public void reset() {
        throwExceptionIfClosedInSetter();

        bytes = null;
        offset = 0;
        length = 0;
    }

    @Override
    public long maxSize() {
        return length;
    }


    public String toString() {
        if (bytes == null) {
            return "LongArrayTextReference{" +
                    "bytes=null" +
                    ", offset=" + offset +
                    ", length=" + length +
                    '}';
        }

        return "value: " + getValueAt(0) + " ...";
    }

    @Override
    public long sizeInBytes(long capacity) {
        return (capacity * VALUE_SIZE) + VALUES + SECTION3.length - SEP.length;
    }
}
