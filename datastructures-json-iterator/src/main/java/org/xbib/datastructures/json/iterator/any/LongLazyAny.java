package org.xbib.datastructures.json.iterator.any;

import org.xbib.datastructures.json.iterator.JsonIterator;
import org.xbib.datastructures.json.iterator.JsonIteratorPool;
import org.xbib.datastructures.json.iterator.spi.JsonException;
import org.xbib.datastructures.json.iterator.ValueType;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

class LongLazyAny extends LazyAny {

    private boolean isCached;
    private long cache;

    public LongLazyAny(byte[] data, int head, int tail) {
        super(data, head, tail);
    }

    @Override
    public ValueType valueType() {
        return ValueType.NUMBER;
    }

    @Override
    public Object object() {
        fillCache();
        return cache;
    }

    @Override
    public boolean toBoolean() {
        fillCache();
        return cache != 0;
    }

    @Override
    public int toInt() {
        fillCache();
        return (int) cache;
    }

    @Override
    public long toLong() {
        fillCache();
        return cache;
    }

    @Override
    public float toFloat() {
        fillCache();
        return cache;
    }

    @Override
    public double toDouble() {
        fillCache();
        return cache;
    }

    @Override
    public BigInteger toBigInteger() {
        return new BigInteger(toString());
    }

    @Override
    public BigDecimal toBigDecimal() {
        return new BigDecimal(toString());
    }

    private void fillCache() {
        if (!isCached) {
            JsonIterator iter = parse();
            try {
                cache = iter.readLong();
            } catch (IOException e) {
                throw new JsonException(e);
            } finally {
                JsonIteratorPool.returnJsonIterator(iter);
            }
            isCached = true;
        }
    }
}
