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

import net.openhft.chronicle.bytes.BytesStore;
import org.jetbrains.annotations.NotNull;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;

public class BinaryLongReference extends AbstractReference implements LongReference {
    public static final long LONG_NOT_COMPLETE = -1;

    @SuppressWarnings("rawtypes")
    @Override
    public void bytesStore( final BytesStore bytes, final long offset, final long length) throws IllegalStateException, IllegalArgumentException, BufferOverflowException, BufferUnderflowException {
        throwExceptionIfClosed();

        if (length != maxSize())
            throw new IllegalArgumentException();

        super.bytesStore(bytes, offset, length);
    }

    @Override
    public long maxSize() {
        return 8;
    }


    public String toString() {
        return bytes == null ? "bytes is null" : "value: " + getValue();
    }

    @Override
    public long getValue() {
        return bytes == null ? 0L : bytes.readLong(offset);
    }

    @Override
    public void setValue(long value) {
        try {
            bytes.writeLong(offset, value);
        } catch (NullPointerException e) {
            throwExceptionIfClosed();

            throw e;
        }
    }

    @Override
    public long getVolatileValue() {
        try {
            return bytes.readVolatileLong(offset);
        } catch (NullPointerException e) {
            throwExceptionIfClosed();

            throw e;
        }
    }

    @Override
    public void setVolatileValue(long value) {
        try {
            bytes.writeVolatileLong(offset, value);
        } catch (NullPointerException e) {
            throwExceptionIfClosed();

            throw e;
        }
    }

    @Override
    public long getVolatileValue(long closedValue) {
        if (isClosed())
            return closedValue;
        try {
            return getVolatileValue();
        } catch (Exception e) {
            return closedValue;
        }
    }

    @Override
    public void setOrderedValue(long value) {
        try {
            bytes.writeOrderedLong(offset, value);
        } catch (NullPointerException e) {
            throwExceptionIfClosed();

            throw e;
        }
    }

    @Override
    public long addValue(long delta) {
        try {
            return bytes.addAndGetLong(offset, delta);
        } catch (NullPointerException e) {
            throwExceptionIfClosed();

            throw e;
        }
    }

    @Override
    public long addAtomicValue(long delta) {
        return addValue(delta);
    }

    @Override
    public boolean compareAndSwapValue(long expected, long value) {
        try {
            return bytes.compareAndSwapLong(offset, expected, value);
        } catch (NullPointerException e) {
            throwExceptionIfClosed();

            throw e;
        }
    }
}
