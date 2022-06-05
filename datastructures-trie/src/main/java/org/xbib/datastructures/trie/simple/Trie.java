package org.xbib.datastructures.trie.simple;

import java.util.List;
import java.util.Set;

/**
 * https://stevedaskam.wordpress.com/2009/05/28/trie-structures/
 *
 * @param <T>
 */
public interface Trie<T> {

    void add(String key, T value);

    T search(String key);

    List<T> startsWith(String prefix);

    boolean contains(String key);

    Set<String> getAllKeys();

    int size();
}
