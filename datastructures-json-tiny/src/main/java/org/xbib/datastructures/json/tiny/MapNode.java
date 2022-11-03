package org.xbib.datastructures.json.tiny;

import org.xbib.datastructures.api.Node;
import org.xbib.datastructures.tiny.TinyMap;
import java.util.Map;

public class MapNode extends TinyMap.Builder<CharSequence, Node<?>> implements org.xbib.datastructures.api.MapNode {

    public MapNode() {
    }

    public boolean has(String name) {
        return containsKey(name);
    }

    @Override
    public int getDepth() {
        return 0;
    }

    @Override
    public Map<CharSequence, Node<?>> get() {
        return this;
    }
}
