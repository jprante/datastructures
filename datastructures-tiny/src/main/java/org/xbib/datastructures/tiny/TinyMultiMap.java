package org.xbib.datastructures.tiny;

import org.xbib.datastructures.common.AbstractMultiMap;
import org.xbib.datastructures.common.MultiMap;
import java.util.Collection;
import java.util.Map;

/**
 * Tiny multi map.
 *
 * @param <K> the key type parameter
 * @param <V> the value type parameter
 */
public class TinyMultiMap<K, V> extends AbstractMultiMap<K, V> {

    public TinyMultiMap() {
        super();
    }

    public TinyMultiMap(MultiMap<K, V> map) {
        super(map);
    }

    @Override
    protected Collection<V> newValues() {
        return TinySet.builder();
    }

    @Override
    protected Map<K, Collection<V>> newMap() {
        return TinyMap.builder();
    }
}
