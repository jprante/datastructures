package org.xbib.datastructures.json.tiny;

import org.xbib.datastructures.api.Node;
import org.xbib.datastructures.tiny.TinyList;
import java.util.List;

public class ListNode extends TinyList.Builder<Node<?>> implements org.xbib.datastructures.api.ListNode {

    public ListNode() {
    }

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
    public List<Node<?>> get() {
        return this;
    }
}
