package org.xbib.datastructures.json.tiny;

import org.xbib.datastructures.api.Node;

import java.util.Deque;
import java.util.LinkedList;

public class StandardJsonListener implements JsonResult {

    private Node<?> node;

    private final Deque<Node<?>> stack = new LinkedList<>();

    private final ValueNode NULL_NODE = new ValueNode(null);

    private final ValueNode TRUE_NODE = new ValueNode(Boolean.TRUE);

    private final ValueNode FALSE_NODE = new ValueNode(Boolean.FALSE);

    public Deque<Node<?>> getStack() {
        return stack;
    }

    @Override
    public Node<?> getResult() {
        return node;
    }

    @Override
    public void begin() {
    }

    @Override
    public void end() {
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
        valueNode(new ValueNode(value));
    }

    @Override
    public void onLong(Long value) {
        valueNode(new ValueNode(value));
    }

    @Override
    public void onDouble(Double value) {
        valueNode(new ValueNode(value));
    }

    @Override
    public void beginCollection() {
        stack.push(new StandardListNode());
    }

    @Override
    public void endCollection() {
        node = stack.pop();
        tryAppend(node);
    }

    @Override
    public void beginMap() {
        stack.push(new StandardMapNode());
    }

    @Override
    public void endMap() {
        node = stack.pop();
        tryAppend(node);
    }

    private void valueNode(ValueNode valueNode) {
        if (!tryAppend(valueNode)) {
            stack.push(valueNode);
            node = valueNode;
        }
    }

    private boolean tryAppend(Node<?> node) {
        if (!stack.isEmpty()) {
            if (stack.peek() instanceof StandardListNode) {
                StandardListNode listNode = (StandardListNode) stack.peek();
                listNode.add(node);
                return true;
            } else if (stack.peek() instanceof KeyNode) {
                KeyNode keyNode = (KeyNode) stack.pop();
                if (stack.peek() instanceof StandardMapNode) {
                    StandardMapNode mapNode = (StandardMapNode) stack.peek();
                    mapNode.put(keyNode.get(), node);
                    return true;
                }
            }
        }
        return false;
    }
}
