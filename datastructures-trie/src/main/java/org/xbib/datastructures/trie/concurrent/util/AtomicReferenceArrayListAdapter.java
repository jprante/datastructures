package org.xbib.datastructures.trie.concurrent.util;

import java.util.AbstractList;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * Wraps an {@link AtomicReferenceArray} to implement read-only methods of the {@link java.util.List} interface.
 * <p/>
 * This enables binary search of an {@link AtomicReferenceArray}, using
 * {@link java.util.Collections#binarySearch(java.util.List, Object)}.
 */
@SuppressWarnings("serial")
public class AtomicReferenceArrayListAdapter<T> extends AbstractList<T> {

    private final AtomicReferenceArray<T> atomicReferenceArray;

    public AtomicReferenceArrayListAdapter(AtomicReferenceArray<T> atomicReferenceArray) {
        this.atomicReferenceArray = atomicReferenceArray;
    }

    @Override
    public T get(int index) {
        return atomicReferenceArray.get(index);
    }

    @Override
    public int size() {
        return atomicReferenceArray.length();
    }
}
