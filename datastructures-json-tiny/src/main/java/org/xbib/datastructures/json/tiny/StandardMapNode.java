package org.xbib.datastructures.json.tiny;

import org.xbib.datastructures.api.Node;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class StandardMapNode extends LinkedHashMap<CharSequence, Object> implements Node<Map<CharSequence, Object>> {

    public StandardMapNode() {
    }

    public boolean has(String name) {
        return containsKey(name);
    }

    @Override
    public int getDepth() {
        return 0;
    }

    @Override
    public Map<CharSequence, Object> get() {
        return this;
    }
}
