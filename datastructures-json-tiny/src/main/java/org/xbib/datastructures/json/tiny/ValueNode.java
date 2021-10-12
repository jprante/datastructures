package org.xbib.datastructures.json.tiny;

public class ValueNode implements org.xbib.datastructures.api.ValueNode {

    private Object value;

    public ValueNode(Object value) {
        this.value = value;
    }

    @Override
    public int getDepth() {
        return 0;
    }

    @Override
    public void set(Object value) {
        this.value = value;
    }

    @Override
    public Object get() {
        return value;
    }

    @Override
    public String toString() {
        return value != null ? value.toString() : null;
    }
}
