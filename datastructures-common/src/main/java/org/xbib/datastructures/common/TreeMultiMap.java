package org.xbib.datastructures.common;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Tree-set multi map.
 *
 * @param <K> the key type parameter
 * @param <V> the value type parameter
 */
public class TreeMultiMap<K extends Comparable<K>, V extends Comparable<V>> extends AbstractMultiMap<K, V> {

    public TreeMultiMap() {
        super();
    }

    public TreeMultiMap(MultiMap<K, V> map) {
        super(map);
    }

    @Override
    protected Collection<V> newValues() {
        return new TreeSet<>();
    }

    @Override
    protected Map<K, Collection<V>> newMap() {
        return new TreeMap<>();
    }
}
