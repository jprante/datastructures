package org.xbib.datastructures.trie.ahocorasick;

public interface Token<T> {

    String getFragment();

    boolean isMatch();

    EntryOutput<T> getOutput();
}
