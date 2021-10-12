package org.xbib.datastructures.json.tiny;

import org.xbib.datastructures.api.Node;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class StandardListNode extends ArrayList<Object> implements Node<List<Object>> {

    public boolean has(int i) {
        return i >= 0 && i < size() && get(i) == null;
    }

    public boolean has(Node<?> node) {
        return contains(node);
    }

    @Override
    public int getDepth() {
        return 0;
    }

    @Override
    public List<Object> get() {
        return this;
    }
}
