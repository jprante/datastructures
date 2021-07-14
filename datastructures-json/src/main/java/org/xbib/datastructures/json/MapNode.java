package org.xbib.datastructures.json;

import org.xbib.datastructures.tiny.TinyMap;
import java.util.Map;

public class MapNode extends TinyMap.Builder<CharSequence, Object> implements Node<Map<CharSequence, Object>> {

    public boolean has(String name) {
        return containsKey(name);
    }

    @Override
    public Map<CharSequence, Object> get() {
        return this;
    }
}
