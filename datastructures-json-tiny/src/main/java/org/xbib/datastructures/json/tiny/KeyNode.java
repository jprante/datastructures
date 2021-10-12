package org.xbib.datastructures.json.tiny;

import org.xbib.datastructures.api.Node;

public class KeyNode implements Node<CharSequence> {

    private CharSequence value;

    public KeyNode(CharSequence value) {
        this.value = value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int getDepth() {
        return 0;
    }

    @Override
    public CharSequence get() {
        return value;
    }

    @Override
    public String toString() {
        return value != null ? value.toString() : null;
    }
}
