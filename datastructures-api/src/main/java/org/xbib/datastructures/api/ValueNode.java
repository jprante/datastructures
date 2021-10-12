package org.xbib.datastructures.api;

public interface ValueNode extends Node<Object> {

    void set(Object value);

    @Override
    Object get();
}
