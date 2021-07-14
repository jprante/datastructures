package org.xbib.datastructures.json;

import org.xbib.datastructures.tiny.TinyList;
import java.util.List;

public class ListNode extends TinyList.Builder<Object> implements Node<List<Object>> {

    public boolean has(int i) {
        return i >= 0 && i < size() && get(i) == null;
    }

    public boolean has(Node<?> node) {
        return contains(node);
    }

    @Override
    public List<Object> get() {
        return this;
    }
}
