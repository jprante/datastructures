package org.xbib.datastructures.tiny;

import java.util.List;

public interface IndexedList<T> extends List<T>, IndexedCollection<T> {

    T removeLast();

    @Override
    IndexedList<T> subList(int fromIndex, int toIndex);
}
