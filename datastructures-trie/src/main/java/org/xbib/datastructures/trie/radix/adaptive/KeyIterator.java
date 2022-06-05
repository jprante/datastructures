package org.xbib.datastructures.trie.radix.adaptive;

final class KeyIterator<K, V> extends PrivateEntryIterator<K, V, K> {
    KeyIterator(AdaptiveRadixTree<K, V> m, LeafNode<K, V> first) {
        super(m, first);
    }

    @Override
    public K next() {
        return nextEntry().getKey();
    }
}
