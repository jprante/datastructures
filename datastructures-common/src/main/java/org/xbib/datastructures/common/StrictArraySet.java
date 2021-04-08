package org.xbib.datastructures.common;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;

public class StrictArraySet<E> extends AbstractSet<E> {

    private final E[] values;

    public StrictArraySet(E[] values) {
        this.values = values;
    }

    @SuppressWarnings("unchecked")
    public StrictArraySet(Collection<E> values) {
        this.values = (E[]) values.toArray();
    }

    public E[] theEntries() {
        return values;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int i = 0;
            @Override
            public boolean hasNext() {
                return i < values.length;
            }

            @Override
            public E next() {
                return values[i++];
            }
        };
    }

    @Override
    public int size() {
        return values.length;
    }
}
