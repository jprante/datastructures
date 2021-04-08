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

package net.openhft.chronicle.bytes.util;

import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.bytes.BytesStore;
import net.openhft.chronicle.core.Maths;
import net.openhft.chronicle.core.io.IORuntimeException;
import net.openhft.chronicle.core.io.IOTools;
import org.jetbrains.annotations.NotNull;

import java.nio.BufferUnderflowException;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * This cache only gaurentees it will provide a String which matches the decoded bytes.
 * <p>
 * It doesn't guantee it will always return the same object,
 * nor that different threads will return the same object,
 * though the contents should always be the same.
 * <p>
 * While not technically thread safe, it should still behave correctly.
 *
 * @author peter.lawrey
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class AbstractInterner<T> {

    protected final InternerEntry<T>[] entries;
    protected final int mask, shift;
    protected boolean toggle = false;

    public AbstractInterner(int capacity) throws IllegalArgumentException {
        int n = Maths.nextPower2(capacity, 128);
        shift = Maths.intLog2(n);
        entries = new InternerEntry[n];
        mask = n - 1;
    }

    private static int hash32( BytesStore bs, int length) {
        return bs.fastHash(bs.readPosition(), length);
    }

    public T intern( Bytes cs)
            throws IllegalArgumentException, IORuntimeException, BufferUnderflowException {
        return intern((BytesStore) cs, (int) cs.readRemaining());
    }

    public T intern( BytesStore cs)
            throws IllegalArgumentException, IORuntimeException, BufferUnderflowException {
        return intern(cs, (int) cs.readRemaining());
    }

    public T intern( Bytes cs, int length)
            throws IllegalArgumentException, IORuntimeException, BufferUnderflowException {
        return intern((BytesStore) cs, length);
    }

    public T intern( BytesStore cs, int length)
            throws IllegalArgumentException, IORuntimeException, BufferUnderflowException {
        if (length > entries.length)
            return getValue(cs, length);
        // TODO This needs to be reviewd.
//        UnsafeMemory.UNSAFE.loadFence();
        int hash = hash32(cs, length);
        int h = hash & mask;
        InternerEntry<T> s = entries[h];
        if (s != null && s.bytes.length() == length && s.bytes.equalBytes(cs, length))
            return s.t;
        int h2 = (hash >> shift) & mask;
        InternerEntry<T> s2 = entries[h2];
        if (s2 != null && s2.bytes.length() == length && s2.bytes.equalBytes(cs, length))
            return s2.t;
         T t = getValue(cs, length);
         final byte[] bytes = new byte[length];
         BytesStore bs = BytesStore.wrap(bytes);
        IOTools.unmonitor(bs);
        cs.read(cs.readPosition(), bytes, 0, length);
        entries[s == null || (s2 != null && toggle()) ? h : h2] = new InternerEntry<>(bs, t);
//        UnsafeMemory.UNSAFE.storeFence();
        return t;
    }


    protected abstract T getValue(BytesStore bs, int length) throws IORuntimeException;

    protected boolean toggle() {
        return toggle = !toggle;
    }

    public int valueCount() {
        return (int) Stream.of(entries).filter(Objects::nonNull).count();
    }

    static class InternerEntry<T> {
        final BytesStore bytes;
        final T t;

        InternerEntry(BytesStore bytes, T t) {
            this.bytes = bytes;
            this.t = t;
        }
    }
}
