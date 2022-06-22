package org.xbib.datastructures.trie.ahocorasick;

import java.util.List;

public interface CollectingOutputHandler<T> extends OutputHandler<T> {
    List<EntryOutput<T>> getOutputs();
}
