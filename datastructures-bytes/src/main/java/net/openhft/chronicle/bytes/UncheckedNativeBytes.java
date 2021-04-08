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

package net.openhft.chronicle.bytes;

import net.openhft.chronicle.bytes.algo.BytesStoreHash;
import net.openhft.chronicle.core.Jvm;
import net.openhft.chronicle.core.Memory;
import net.openhft.chronicle.core.OS;
import net.openhft.chronicle.core.annotation.ForceInline;
import net.openhft.chronicle.core.io.AbstractReferenceCounted;
import net.openhft.chronicle.core.io.BackgroundResourceReleaser;
import net.openhft.chronicle.core.io.IORuntimeException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

/**
 * Fast unchecked version of AbstractBytes
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class UncheckedNativeBytes<Underlying>
        extends AbstractReferenceCounted
        implements Bytes<Underlying> {
    protected final long capacity;

    private final Bytes<Underlying> underlyingBytes;

    protected NativeBytesStore<Underlying> bytesStore;
    protected long readPosition;
    protected long writePosition;
    protected long writeLimit;
    private int lastDecimalPlaces = 0;

    public UncheckedNativeBytes( Bytes<Underlying> underlyingBytes)
            throws IllegalStateException {
        this.underlyingBytes = underlyingBytes;
        underlyingBytes.reserve(this);
        this.bytesStore = (NativeBytesStore<Underlying>) underlyingBytes.bytesStore();
        assert bytesStore.start() == 0;
        writePosition = underlyingBytes.writePosition();
        writeLimit = underlyingBytes.writeLimit();
        readPosition = underlyingBytes.readPosition();
        capacity = bytesStore.capacity();
    }

    @Override
    public void ensureCapacity(long size) throws IllegalArgumentException {
        if (size > realCapacity()) {
            underlyingBytes.ensureCapacity(size);
            bytesStore = (NativeBytesStore<Underlying>) underlyingBytes.bytesStore();
        }
    }

    @Override
    public boolean unchecked() {
        return true;
    }

    @Override
    public boolean isDirectMemory() {
        return true;
    }

    @Override

    public Bytes<Underlying> unchecked(boolean unchecked) {
        return this;
    }

    @Override
    public void move(long from, long to, long length) {
        bytesStore.move(from - start(), to - start(), length);
    }


    @Override
    public Bytes<Underlying> compact() {
        long start = start();
        long readRemaining = readRemaining();
        if (readRemaining > 0 && start < readPosition) {
            bytesStore.move(readPosition, start, readRemaining);
            readPosition = start;
            writePosition = readPosition + readRemaining;
        }
        return this;
    }


    @Override
    public Bytes<Underlying> readPosition(long position) {
        readPosition = position;
        return this;
    }


    @Override
    public Bytes<Underlying> readLimit(long limit) {
        writePosition = limit;
        return this;
    }


    @Override
    public Bytes<Underlying> writePosition(long position) {
        writePosition = position;
        return this;
    }


    @Override
    public Bytes<Underlying> readSkip(long bytesToSkip) {
        readPosition += bytesToSkip;
        return this;
    }

    @Override
    public byte readVolatileByte(long offset) throws BufferUnderflowException {
        return bytesStore.readVolatileByte(offset);
    }

    @Override
    public short readVolatileShort(long offset) throws BufferUnderflowException {
        return bytesStore.readVolatileShort(offset);
    }

    @Override
    public int readVolatileInt(long offset) throws BufferUnderflowException {
        return bytesStore.readVolatileInt(offset);
    }

    @Override
    public long readVolatileLong(long offset) throws BufferUnderflowException {
        return bytesStore.readVolatileLong(offset);
    }

    @Override
    public void uncheckedReadSkipOne() {
        readPosition++;
    }

    @Override
    public void uncheckedReadSkipBackOne() {
        readPosition--;
    }


    @Override
    public Bytes<Underlying> writeSkip(long bytesToSkip) {
        writePosition += bytesToSkip;
        return this;
    }


    @Override
    public Bytes<Underlying> writeLimit(long limit) {
        writeLimit = limit;
        return this;
    }


    @Override
    public BytesStore<Bytes<Underlying>, Underlying> copy() {
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public boolean isElastic() {
        return false;
    }

    protected long readOffsetPositionMoved(long adding) {
        long offset = readPosition;
        readPosition += adding;
        return offset;
    }

    protected long writeOffsetPositionMoved(long adding) {
        return writeOffsetPositionMoved(adding, adding);
    }

    protected long writeOffsetPositionMoved(long adding, long advance) {
        long oldPosition = writePosition;
        long writeEnd = oldPosition + adding;
        assert writeEnd <= bytesStore.safeLimit();
        writePosition += advance;
        return oldPosition;
    }

    protected long prewriteOffsetPositionMoved(long substracting) {
        return readPosition -= substracting;
    }


    @Override
    public Bytes<Underlying> write( RandomDataInput bytes, long offset, long length)
            throws BufferUnderflowException, BufferOverflowException {
        if (length == 8) {
            writeLong(bytes.readLong(offset));

        } else if (length >= 16 && bytes.isDirectMemory()) {
            rawCopy(bytes, offset, length);

        } else {
            BytesInternal.writeFully(bytes, offset, length, this);
        }
        return this;
    }

    public long rawCopy( RandomDataInput bytes, long offset, long length)
            throws BufferOverflowException, BufferUnderflowException {
        long len = Math.min(writeRemaining(), Math.min(bytes.capacity() - offset, length));
        if (len > 0) {
            writeCheckOffset(writePosition(), len);
            this.throwExceptionIfReleased();
            OS.memory().copyMemory(bytes.addressForRead(offset), addressForWritePosition(), len);
            writeSkip(len);
        }
        return len;
    }

    @Override

    public Bytes<Underlying> clear() {
        readPosition = writePosition = start();
        writeLimit = capacity();
        return this;
    }


    @Override
    public Bytes<Underlying> clearAndPad(long length) throws BufferOverflowException {
        if (start() + length > capacity())
            throw new BufferOverflowException();
        readPosition = writePosition = start() + length;
        writeLimit = capacity();
        return this;
    }

    @Override
    @ForceInline
    public long readLimit() {
        return writePosition;
    }

    @Override
    @ForceInline
    public long writeLimit() {
        return writeLimit;
    }

    @Override
    @ForceInline
    public long realCapacity() {
        return bytesStore.realCapacity();
    }

    @Override
    public long realWriteRemaining() {
        return writeRemaining();
    }

    @Override
    @ForceInline
    public long capacity() {
        return capacity;
    }


    @Override
    public Underlying underlyingObject() {
        return bytesStore.underlyingObject();
    }

    @Override
    @ForceInline
    public long readPosition() {
        return readPosition;
    }

    @Override
    @ForceInline
    public long writePosition() {
        return writePosition;
    }

    @Override
    @ForceInline
    public boolean compareAndSwapInt(long offset, int expected, int value)
            throws BufferOverflowException {
        writeCheckOffset(offset, 4);
        return bytesStore.compareAndSwapInt(offset, expected, value);
    }

    @Override
    public void testAndSetInt(long offset, int expected, int value) {
        writeCheckOffset(offset, 4);
        bytesStore.testAndSetInt(offset, expected, value);
    }

    @Override
    @ForceInline
    public boolean compareAndSwapLong(long offset, long expected, long value)
            throws BufferOverflowException {
        writeCheckOffset(offset, 8);
        return bytesStore.compareAndSwapLong(offset, expected, value);
    }

    @Override
    protected void performRelease() {
        this.underlyingBytes.release(this);
        // need to wait as checks rely on this completing.
        BackgroundResourceReleaser.releasePendingResources();
    }

    @Override
    public int readUnsignedByte() {
        long offset = readOffsetPositionMoved(1);
        return bytesStore.memory.readByte(bytesStore.address + offset) & 0xFF;
    }

    @Override
    public int uncheckedReadUnsignedByte() {
        return readUnsignedByte();
    }

    @Override
    @ForceInline
    public byte readByte() {
        long offset = readOffsetPositionMoved(1);
        return bytesStore.memory.readByte(bytesStore.address + offset);
    }

    @Override
    @ForceInline
    public int peekUnsignedByte() {
        try {
            return readRemaining() > 0 ? bytesStore.readUnsignedByte(readPosition) : -1;

        } catch (BufferUnderflowException e) {
            return -1;
        }
    }

    @Override
    @ForceInline
    public short readShort() {
        long offset = readOffsetPositionMoved(2);
        return bytesStore.readShort(offset);
    }

    @Override
    @ForceInline
    public int readInt() {
        long offset = readOffsetPositionMoved(4);
        return bytesStore.readInt(offset);
    }

    @Override
    @ForceInline
    public long readLong() {
        long offset = readOffsetPositionMoved(8);
        return bytesStore.readLong(offset);
    }

    @Override
    @ForceInline
    public float readFloat() {
        long offset = readOffsetPositionMoved(4);
        return bytesStore.readFloat(offset);
    }

    @Override
    @ForceInline
    public double readDouble() {
        long offset = readOffsetPositionMoved(8);
        return bytesStore.readDouble(offset);
    }

    @Override
    @ForceInline
    public int readVolatileInt() {
        long offset = readOffsetPositionMoved(4);
        return bytesStore.readVolatileInt(offset);
    }

    @Override
    @ForceInline
    public long readVolatileLong() {
        long offset = readOffsetPositionMoved(8);
        return bytesStore.readVolatileLong(offset);
    }


    @Override
    @ForceInline
    public Bytes<Underlying> writeByte(long offset, byte i) throws BufferOverflowException {
        writeCheckOffset(offset, 1);
        bytesStore.writeByte(offset, i);
        return this;
    }


    @Override
    @ForceInline
    public Bytes<Underlying> writeShort(long offset, short i) throws BufferOverflowException {
        writeCheckOffset(offset, 2);
        bytesStore.writeShort(offset, i);
        return this;
    }


    @Override
    @ForceInline
    public Bytes<Underlying> writeInt(long offset, int i) throws BufferOverflowException {
        writeCheckOffset(offset, 4);
        bytesStore.writeInt(offset, i);
        return this;
    }


    @Override
    @ForceInline
    public Bytes<Underlying> writeOrderedInt(long offset, int i) throws BufferOverflowException {
        writeCheckOffset(offset, 4);
        bytesStore.writeOrderedInt(offset, i);
        return this;
    }


    @Override
    @ForceInline
    public Bytes<Underlying> writeLong(long offset, long i) throws BufferOverflowException {
        writeCheckOffset(offset, 8);
        bytesStore.writeLong(offset, i);
        return this;
    }


    @Override
    @ForceInline
    public Bytes<Underlying> writeOrderedLong(long offset, long i) throws BufferOverflowException {
        writeCheckOffset(offset, 8);
        bytesStore.writeOrderedLong(offset, i);
        return this;
    }


    @Override
    @ForceInline
    public Bytes<Underlying> writeFloat(long offset, float d) throws BufferOverflowException {
        writeCheckOffset(offset, 4);
        bytesStore.writeFloat(offset, d);
        return this;
    }


    @Override
    @ForceInline
    public Bytes<Underlying> writeDouble(long offset, double d) throws BufferOverflowException {
        writeCheckOffset(offset, 8);
        bytesStore.writeDouble(offset, d);
        return this;
    }


    @Override
    @ForceInline
    public Bytes<Underlying> writeVolatileByte(long offset, byte i8)
            throws BufferOverflowException {
        writeCheckOffset(offset, 1);
        bytesStore.writeVolatileByte(offset, i8);
        return this;
    }


    @Override
    @ForceInline
    public Bytes<Underlying> writeVolatileShort(long offset, short i16)
            throws BufferOverflowException {
        writeCheckOffset(offset, 2);
        bytesStore.writeVolatileShort(offset, i16);
        return this;
    }


    @Override
    @ForceInline
    public Bytes<Underlying> writeVolatileInt(long offset, int i32)
            throws BufferOverflowException {
        writeCheckOffset(offset, 4);
        bytesStore.writeVolatileInt(offset, i32);
        return this;
    }


    @Override
    @ForceInline
    public Bytes<Underlying> writeVolatileLong(long offset, long i64)
            throws BufferOverflowException {
        writeCheckOffset(offset, 8);
        bytesStore.writeVolatileLong(offset, i64);
        return this;
    }

    @Override

    @ForceInline
    public Bytes<Underlying> write(long offsetInRDO, byte[] bytes, int offset, int length)
            throws BufferOverflowException {
        writeCheckOffset(offsetInRDO, length);
        bytesStore.write(offsetInRDO, bytes, offset, length);
        return this;
    }

    @Override
    @ForceInline
    public void write(long offsetInRDO,  ByteBuffer bytes, int offset, int length) throws BufferOverflowException {
        writeCheckOffset(offsetInRDO, length);
        bytesStore.write(offsetInRDO, bytes, offset, length);
    }

    @Override

    @ForceInline
    public Bytes<Underlying> write(long writeOffset,
                                    RandomDataInput bytes, long readOffset, long length)
            throws BufferUnderflowException, BufferOverflowException {
        writeCheckOffset(writeOffset, length);
        bytesStore.write(writeOffset, bytes, readOffset, length);
        return this;
    }

    @ForceInline
    void writeCheckOffset(long offset, long adding) throws BufferOverflowException {
//        assert writeCheckOffset0(offset, adding);
    }

    @Override
    @ForceInline
    public byte readByte(long offset) {
        return bytesStore.readByte(offset);
    }

    @Override
    public int readUnsignedByte(long offset) {
        return bytesStore.memory.readByte(bytesStore.address + offset) & 0xFF;
    }

    @Override
    public int peekUnsignedByte(long offset) {
        return offset >= writePosition ? -1 : readByte(offset);
    }

    @Override
    @ForceInline
    public short readShort(long offset) {
        return bytesStore.readShort(offset);
    }

    @Override
    @ForceInline
    public int readInt(long offset) {
        return bytesStore.readInt(offset);
    }

    @Override
    @ForceInline
    public long readLong(long offset) {
        return bytesStore.readLong(offset);
    }

    @Override
    @ForceInline
    public float readFloat(long offset) {
        return bytesStore.readFloat(offset);
    }

    @Override
    @ForceInline
    public double readDouble(long offset) {
        return bytesStore.readDouble(offset);
    }


    @Override
    @ForceInline
    public Bytes<Underlying> writeByte(byte i8) {
        long offset = writeOffsetPositionMoved(1);
        bytesStore.memory.writeByte(bytesStore.address + offset, i8);
        return this;
    }


    @Override
    @ForceInline
    public Bytes<Underlying> prewriteByte(byte i8) {
        long offset = prewriteOffsetPositionMoved(1);
        bytesStore.memory.writeByte(bytesStore.address + offset, i8);
        return this;
    }


    @Override
    @ForceInline
    public Bytes<Underlying> writeShort(short i16) {
        long offset = writeOffsetPositionMoved(2);
        bytesStore.writeShort(offset, i16);
        return this;
    }


    @Override
    @ForceInline
    public Bytes<Underlying> prewriteShort(short i16) {
        long offset = prewriteOffsetPositionMoved(2);
        bytesStore.writeShort(offset, i16);
        return this;
    }


    @Override
    @ForceInline
    public Bytes<Underlying> writeInt(int i) {
        long offset = writeOffsetPositionMoved(4);
        bytesStore.writeInt(offset, i);
        return this;
    }


    @Override
    @ForceInline
    public Bytes<Underlying> writeIntAdv(int i, int advance) {
        long offset = writeOffsetPositionMoved(4, advance);
        bytesStore.writeInt(offset, i);
        return this;
    }


    @Override
    @ForceInline
    public Bytes<Underlying> prewriteInt(int i) {
        long offset = prewriteOffsetPositionMoved(4);
        bytesStore.writeInt(offset, i);
        return this;
    }


    @Override
    @ForceInline
    public Bytes<Underlying> writeLong(long i64) {
        long offset = writeOffsetPositionMoved(8);
        bytesStore.writeLong(offset, i64);
        return this;
    }


    @Override
    @ForceInline
    public Bytes<Underlying> writeLongAdv(long i64, int advance) {
        long offset = writeOffsetPositionMoved(8, advance);
        bytesStore.writeLong(offset, i64);
        return this;
    }


    @Override
    @ForceInline
    public Bytes<Underlying> prewriteLong(long i64) {
        long offset = prewriteOffsetPositionMoved(8);
        bytesStore.writeLong(offset, i64);
        return this;
    }


    @Override
    @ForceInline
    public Bytes<Underlying> writeFloat(float f) {
        long offset = writeOffsetPositionMoved(4);
        bytesStore.writeFloat(offset, f);
        return this;
    }


    @Override
    @ForceInline
    public Bytes<Underlying> writeDouble(double d) {
        long offset = writeOffsetPositionMoved(8);
        bytesStore.writeDouble(offset, d);
        return this;
    }


    @Override
    @ForceInline
    public Bytes<Underlying> writeDoubleAndInt(double d, int i) {
        long offset = writeOffsetPositionMoved(12);
        bytesStore.writeDouble(offset, d);
        bytesStore.writeInt(offset + 8, i);
        return this;
    }


    @Override
    @ForceInline
    public Bytes<Underlying> write( byte[] bytes, int offset, int length) {
        if (length + offset > bytes.length)
            throw new ArrayIndexOutOfBoundsException("bytes.length=" + bytes.length + ", " +
                    "length=" + length + ", offset=" + offset);
        if (length > writeRemaining())
            throw new BufferOverflowException();
        long offsetInRDO = writeOffsetPositionMoved(length);
        bytesStore.write(offsetInRDO, bytes, offset, length);
        return this;
    }


    @Override
    @ForceInline
    public Bytes<Underlying> prewrite( byte[] bytes) {
        long offsetInRDO = prewriteOffsetPositionMoved(bytes.length);
        bytesStore.write(offsetInRDO, bytes);
        return this;
    }


    @Override
    @ForceInline
    public Bytes<Underlying> prewrite( BytesStore bytes) {
        long offsetInRDO = prewriteOffsetPositionMoved(bytes.length());
        bytesStore.write(offsetInRDO, bytes);
        return this;
    }


    @Override
    @ForceInline
    public Bytes<Underlying> writeSome( ByteBuffer buffer) {
        bytesStore.write(writePosition, buffer, buffer.position(), buffer.limit());
        writePosition += buffer.remaining();
        assert writePosition <= writeLimit();
        return this;
    }


    @Override
    @ForceInline
    public Bytes<Underlying> writeOrderedInt(int i) {
        long offset = writeOffsetPositionMoved(4);
        bytesStore.writeOrderedInt(offset, i);
        return this;
    }


    @Override
    @ForceInline
    public Bytes<Underlying> writeOrderedLong(long i) {
        long offset = writeOffsetPositionMoved(8);
        bytesStore.writeOrderedLong(offset, i);
        return this;
    }

    @Override
    public long addressForRead(long offset) throws BufferUnderflowException {
        return bytesStore.addressForRead(offset);
    }

    @Override
    public long addressForWrite(long offset) throws BufferOverflowException {
        return bytesStore.addressForWrite(offset);
    }

    @Override
    public long addressForWritePosition() throws UnsupportedOperationException, BufferOverflowException {
        return bytesStore.addressForWrite(0);
    }

    @Override
    public int hashCode() {
        return BytesStoreHash.hash32(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Bytes)) return false;
         Bytes b2 = (Bytes) obj;
        long remaining = readRemaining();
        return b2.readRemaining() == remaining && equalsBytes(b2, remaining);
    }

    public boolean equalsBytes( Bytes b2, long remaining) {
        long i = 0;
        try {
            for (; i < remaining - 7; i += 8)
                if (readLong(readPosition() + i) != b2.readLong(b2.readPosition() + i))
                    return false;
            for (; i < remaining; i++)
                if (readByte(readPosition() + i) != b2.readByte(b2.readPosition() + i))
                    return false;

        } catch (BufferUnderflowException e) {
            throw Jvm.rethrow(e);
        }
        return true;
    }


    @Override
    public String toString() {
        return BytesInternal.toString(this);
    }


    @Override
    public void lenient(boolean lenient) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean lenient() {
        return false;
    }



    @Override
    @ForceInline
    public void nativeRead(long position, long address, long size) {
        bytesStore.nativeRead(position, address, size);
    }

    @Override
    @ForceInline
    public void nativeWrite(long address, long position, long size) {
        bytesStore.nativeWrite(address, position, size);
    }


    @Override
    public BytesStore bytesStore() {
        return bytesStore;
    }

    @Override
    public int byteCheckSum() throws IORuntimeException {
         NativeBytesStore bytesStore = (NativeBytesStore) bytesStore();
        return bytesStore.byteCheckSum(readPosition(), readLimit());
    }

    @Override

    public Bytes<Underlying> append8bit( CharSequence cs)
            throws BufferOverflowException, BufferUnderflowException {
        if (cs instanceof BytesStore) {
            return write((BytesStore) cs);
        }
        int length = cs.length();
        long offset = writeOffsetPositionMoved(length);
        long address = bytesStore.addressForWrite(offset);
         Memory memory = bytesStore.memory;
        assert memory != null;
        try {
            int i = 0;
            for (; i < length - 1; i += 2) {
                char c = cs.charAt(i);
                char c2 = cs.charAt(i + 1);
                memory.writeByte(address + i, (byte) c);
                memory.writeByte(address + i + 1, (byte) c2);
            }
            for (; i < length; i++) {
                char c = cs.charAt(i);
                memory.writeByte(address + i, (byte) c);
            }

            return this;
        } catch (IndexOutOfBoundsException e) {
            throw new AssertionError(e);
        }
    }


    @Override
    public Bytes<Underlying> appendUtf8(char[] chars, int offset, int length) throws BufferOverflowException, IllegalArgumentException {
        ensureCapacity(length);
         NativeBytesStore nbs = this.bytesStore;
        long position = nbs.appendUtf8(writePosition(), chars, offset, length);
        writePosition(position);
        return this;
    }

    @Override
    public int lastDecimalPlaces() {
        return lastDecimalPlaces;
    }

    @Override
    public void lastDecimalPlaces(int lastDecimalPlaces) {
        this.lastDecimalPlaces = Math.max(0, lastDecimalPlaces);
    }


    @Override
    public Bytes<Underlying> write( RandomDataInput bytes) {
        assert bytes != this : "you should not write to yourself !";

        try {
            return write(bytes, bytes.readPosition(), Math.min(writeRemaining(), bytes.readRemaining()));
        } catch (BufferOverflowException | BufferUnderflowException e) {
            throw new AssertionError(e);
        }
    }
}
