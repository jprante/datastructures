package org.xbib.datastructures.common;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * Linked multi map.
 *
 * @param <K> the key type parameter
 * @param <V> the value type parameter
 */
public class LinkedHashSetMultiMap<K, V> extends AbstractMultiMap<K, V> {

    public LinkedHashSetMultiMap() {
        super();
    }

    public LinkedHashSetMultiMap(MultiMap<K, V> map) {
        super(map);
    }

    @Override
    protected Collection<V> newValues() {
        return new LinkedHashSet<>();
    }

    @Override
    protected Map<K, Collection<V>> newMap() {
        return new LinkedHashMap<>();
    }
}
