package org.xbib.datastructures.json;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class StandardMapNode extends LinkedHashMap<CharSequence, Object> implements Node<Map<CharSequence, Object>> {

    public boolean has(String name) {
        return containsKey(name);
    }

    @Override
    public Map<CharSequence, Object> get() {
        return this;
    }
}
