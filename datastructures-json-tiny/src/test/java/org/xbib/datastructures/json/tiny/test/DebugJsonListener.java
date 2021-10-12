package org.xbib.datastructures.json.tiny.test;

import org.xbib.datastructures.api.Node;
import org.xbib.datastructures.json.tiny.TinyJsonListener;
import org.xbib.datastructures.json.tiny.KeyNode;
import org.xbib.datastructures.json.tiny.ListNode;
import org.xbib.datastructures.json.tiny.MapNode;
import org.xbib.datastructures.json.tiny.ValueNode;
import java.util.Stack;
import java.util.logging.Logger;

public class DebugJsonListener extends TinyJsonListener {

    private static final Logger logger = Logger.getLogger(TinyJsonListener.class.getName());

    private Node<?> node;

    private final Stack<Node<?>> stack = new Stack<>();

    private final ValueNode NULL_NODE = new ValueNode(null);

    private final ValueNode TRUE_NODE = new ValueNode(Boolean.TRUE.toString());

    private final ValueNode FALSE_NODE = new ValueNode(Boolean.FALSE.toString());

    public Node<?> getResult() {
        return node;
    }

    @Override
    public void onNull() {
        valueNode(NULL_NODE);
    }

    @Override
    public void onTrue() {
        valueNode(TRUE_NODE);
    }

    @Override
    public void onFalse() {
        valueNode(FALSE_NODE);
    }

    @Override
    public void onKey(CharSequence key) {
        stack.push(new KeyNode(key));
    }

    @Override
    public void onValue(CharSequence value) {
        valueNode(new ValueNode(value.toString()));
    }

    @Override
    public void onLong(Long value) {
        valueNode(new ValueNode(value.toString()));
    }

    @Override
    public void onDouble(Double value) {
        valueNode(new ValueNode(value.toString()));
    }

    @Override
    public void beginCollection() {
        stack.push(new ListNode());
    }

    @Override
    public void endCollection() {
        node = stack.pop();
        tryAppend(node);
    }

    @Override
    public void beginMap() {
        stack.push(new MapNode());
    }

    @Override
    public void endMap() {
        node = stack.pop();
        tryAppend(node);
    }

    private void valueNode(ValueNode valueNode) {
        if (!tryAppend(valueNode)) {
            node = stack.push(valueNode);
        }
    }

    private boolean tryAppend(Node<?> node) {
        if (!stack.isEmpty()) {
            if (stack.peek() instanceof ListNode) {
                ListNode listNode = (ListNode) stack.peek();
                listNode.add(node);
                return true;
            } else if (stack.peek() instanceof KeyNode) {
                KeyNode keyNode = (KeyNode) stack.pop();
                MapNode mapNode = (MapNode) stack.peek();
                mapNode.put(keyNode.get(), node);
                return true;
            }
        }
        return false;
    }
}
