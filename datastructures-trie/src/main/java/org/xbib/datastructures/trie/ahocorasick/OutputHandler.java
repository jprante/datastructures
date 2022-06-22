package org.xbib.datastructures.trie.ahocorasick;

@FunctionalInterface
public interface OutputHandler<T> {

    boolean output(EntryOutput<T> entryOutput);
}
