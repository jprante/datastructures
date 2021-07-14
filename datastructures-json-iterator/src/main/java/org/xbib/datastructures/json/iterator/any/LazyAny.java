package org.xbib.datastructures.json.iterator.any;

import org.xbib.datastructures.json.iterator.JsonIteratorPool;
import org.xbib.datastructures.json.iterator.spi.JsonException;
import org.xbib.datastructures.json.iterator.JsonIterator;
import org.xbib.datastructures.json.iterator.ValueType;
import org.xbib.datastructures.json.iterator.output.JsonStream;
import org.xbib.datastructures.json.iterator.spi.TypeLiteral;

import java.io.IOException;

abstract class LazyAny extends Any {

    protected final byte[] data;
    protected final int head;
    protected final int tail;

    public LazyAny(byte[] data, int head, int tail) {
        this.data = data;
        this.head = head;
        this.tail = tail;
    }

    public abstract ValueType valueType();

    public final <T> T bindTo(T obj) {
        JsonIterator iter = parse();
        try {
            return iter.read(obj);
        } catch (IOException e) {
            throw new JsonException(e);
        } finally {
            JsonIteratorPool.returnJsonIterator(iter);
        }
    }

    public final <T> T bindTo(TypeLiteral<T> typeLiteral, T obj) {
        JsonIterator iter = parse();
        try {
            return iter.read(typeLiteral, obj);
        } catch (IOException e) {
            throw new JsonException(e);
        } finally {
            JsonIteratorPool.returnJsonIterator(iter);
        }
    }

    public final <T> T as(Class<T> clazz) {
        JsonIterator iter = parse();
        try {
            return iter.read(clazz);
        } catch (IOException e) {
            throw new JsonException(e);
        } finally {
            JsonIteratorPool.returnJsonIterator(iter);
        }
    }

    public final <T> T as(TypeLiteral<T> typeLiteral) {
        JsonIterator iter = parse();
        try {
            return iter.read(typeLiteral);
        } catch (IOException e) {
            throw new JsonException(e);
        } finally {
            JsonIteratorPool.returnJsonIterator(iter);
        }
    }

    public String toString() {
        return new String(data, head, tail - head).trim();
    }

    protected final JsonIterator parse() {
        JsonIterator iter = JsonIteratorPool.borrowJsonIterator();
        iter.reset(data, head, tail);
        return iter;
    }

    @Override
    public void writeTo(JsonStream stream) throws IOException {
        stream.write(data, head, tail - head);
    }
}
