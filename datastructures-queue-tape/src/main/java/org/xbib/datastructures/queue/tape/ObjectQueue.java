package org.xbib.datastructures.queue.tape;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class ObjectQueue<T> implements Iterable<T>, Closeable {

    public static <T> ObjectQueue<T> create(QueueFile qf, Converter<T> converter) {
        return new FileObjectQueue<>(qf, converter);
    }

    public static <T> ObjectQueue<T> createInMemory() {
        return new InMemoryObjectQueue<>();
    }

    public abstract QueueFile file();

    public abstract int size();

    public boolean isEmpty() {
        return size() == 0;
    }

    public abstract void add(T entry) throws IOException;

    public abstract T peek() throws IOException;

    public List<T> peek(int max) {
        int end = Math.min(max, size());
        List<T> subList = new ArrayList<>(end);
        Iterator<T> iterator = iterator();
        for (int i = 0; i < end; i++) {
            subList.add(iterator.next());
        }
        return Collections.unmodifiableList(subList);
    }

    public List<T> asList() {
        return peek(size());
    }

    public void remove() throws IOException {
        remove(1);
    }

    public abstract void remove(int n) throws IOException;

    public void clear() throws IOException {
        remove(size());
    }

    public interface Converter<T> {

        T from(byte[] source) throws IOException;

        void toStream(T value, OutputStream sink) throws IOException;
    }
}
