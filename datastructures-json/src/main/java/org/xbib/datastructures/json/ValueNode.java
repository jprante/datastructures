package org.xbib.datastructures.json;

public class ValueNode implements Node<Object> {

    private Object value;

    public ValueNode(Object value) {
        this.value = value;
    }

    public void setValue(Object value) {
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
