package org.xbib.datastructures.trie.radix.adaptive;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableMap;
import java.util.Set;
import java.util.Spliterator;

final class AscendingSubMap<K, V> extends NavigableSubMap<K, V> {

    AscendingSubMap(AdaptiveRadixTree<K, V> m,
                    boolean fromStart, K lo, boolean loInclusive,
                    boolean toEnd, K hi, boolean hiInclusive) {
        super(m, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
    }

    @Override
    public Comparator<? super K> comparator() {
        return m.comparator();
    }

    @Override
    public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive,
                                     K toKey, boolean toInclusive) {
        if (!inRange(fromKey, fromInclusive))
            throw new IllegalArgumentException("fromKey out of range");
        if (!inRange(toKey, toInclusive))
            throw new IllegalArgumentException("toKey out of range");
        return new AscendingSubMap<>(m,
                false, fromKey, fromInclusive,
                false, toKey, toInclusive);
    }

    // TODO: offer another ctor to take in loBytes
    @Override
    public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
        if (!inRange(toKey, inclusive))
            throw new IllegalArgumentException("toKey out of range");
        return new AscendingSubMap<>(m,
                fromStart, lo, loInclusive,
                false, toKey, inclusive);
    }

    // TODO: offer another ctor to take in hiBytes
    @Override
    public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
        if (!inRange(fromKey, inclusive))
            throw new IllegalArgumentException("fromKey out of range");
        return new AscendingSubMap<>(m,
                false, fromKey, inclusive,
                toEnd, hi, hiInclusive);
    }

    @Override
    public NavigableMap<K, V> descendingMap() {
        NavigableMap<K, V> mv = descendingMapView;
        return (mv != null) ? mv :
                (descendingMapView =
                        new DescendingSubMap<>(m,
                                fromStart, lo, loInclusive,
                                toEnd, hi, hiInclusive));
    }

    @Override
    Iterator<K> keyIterator() {
        return new SubMapKeyIterator(absLowest(), absHighFence());
    }

    @Override
    Spliterator<K> keySpliterator() {
        return new SubMapKeyIterator(absLowest(), absHighFence());
    }

    @Override
    Iterator<K> descendingKeyIterator() {
        return new DescendingSubMapKeyIterator(absHighest(), absLowFence());
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        EntrySetView es = entrySetView;
        return (es != null) ? es : (entrySetView = new AscendingEntrySetView());
    }

    @Override
    LeafNode<K, V> subLowest() {
        return absLowest();
    }

    @Override
    LeafNode<K, V> subHighest() {
        return absHighest();
    }

    @Override
    LeafNode<K, V> subCeiling(K key) {
        return absCeiling(key);
    }

    @Override
    LeafNode<K, V> subHigher(K key) {
        return absHigher(key);
    }

    @Override
    LeafNode<K, V> subFloor(K key) {
        return absFloor(key);
    }

    @Override
    LeafNode<K, V> subLower(K key) {
        return absLower(key);
    }

    final class AscendingEntrySetView extends EntrySetView {
        @Override
        public Iterator<Entry<K, V>> iterator() {
            return new SubMapEntryIterator(absLowest(), absHighFence());
        }
    }
}
