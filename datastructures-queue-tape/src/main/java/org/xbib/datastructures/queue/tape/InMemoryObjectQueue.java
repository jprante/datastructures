package org.xbib.datastructures.queue.tape;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public final class InMemoryObjectQueue<T> extends ObjectQueue<T> {

    private final Deque<T> entries;
    int modCount = 0;
    boolean closed;

    public InMemoryObjectQueue() {
        entries = new ArrayDeque<>();
    }

    @Override
    public QueueFile file() {
        return null;
    }

    @Override
    public void add(T entry) {
        if (closed) throw new IllegalStateException("closed");
        modCount++;
        entries.addLast(entry);
    }

    @Override
    public T peek() {
        if (closed) throw new IllegalStateException("closed");
        return entries.peekFirst();
    }

    @Override
    public List<T> asList() {
        return Collections.unmodifiableList(new ArrayList<>(entries));
    }

    @Override
    public int size() {
        return entries.size();
    }

    @Override
    public void remove() {
        remove(1);
    }

    @Override
    public void remove(int n) {
        if (closed) throw new IllegalStateException("closed");
        modCount++;
        for (int i = 0; i < n; i++) {
            entries.removeFirst();
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new EntryIterator(entries.iterator());
    }

    @Override
    public void close() {
        closed = true;
    }

    @Override
    public String toString() {
        return "InMemoryObjectQueue{"
                + "size=" + entries.size()
                + '}';
    }

    private final class EntryIterator implements Iterator<T> {
        private final Iterator<T> delegate;
        private int index = 0;

        private int expectedModCount = modCount;

        EntryIterator(Iterator<T> delegate) {
            this.delegate = delegate;
        }

        @Override
        public boolean hasNext() {
            checkForComodification();
            return delegate.hasNext();
        }

        @Override
        public T next() {
            if (closed) throw new IllegalStateException("closed");
            checkForComodification();

            T next = delegate.next();
            index += 1;
            return next;
        }

        @Override
        public void remove() {
            if (closed) throw new IllegalStateException("closed");
            checkForComodification();

            if (size() == 0) throw new NoSuchElementException();
            if (index != 1) {
                throw new UnsupportedOperationException("Removal is only permitted from the head.");
            }

            InMemoryObjectQueue.this.remove();

            expectedModCount = modCount;
            index -= 1;
        }

        private void checkForComodification() {
            if (modCount != expectedModCount) throw new ConcurrentModificationException();
        }
    }
}
