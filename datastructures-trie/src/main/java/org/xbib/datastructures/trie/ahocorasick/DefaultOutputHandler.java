package org.xbib.datastructures.trie.ahocorasick;

import java.util.ArrayList;
import java.util.List;

public class DefaultOutputHandler<T> implements CollectingOutputHandler<T> {

    private final List<EntryOutput<T>> outputs = new ArrayList<>();

    @Override
    public boolean output(EntryOutput<T> emit) {
        outputs.add(emit);
        return true;
    }

    @Override
    public List<EntryOutput<T>> getOutputs() {
        return outputs;
    }
}
