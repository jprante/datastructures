package org.xbib.datastructures.yaml;

import java.util.List;
import java.util.Map;

public class Builder {

    public Object object;

    public Builder(Object object) {
        this.object = object;
    }

    public Node<?> build() {
        return object == null ? null : internalBuild(null, object);
    }

    @SuppressWarnings("unchecked")
    private Node<?> internalBuild(Node<?> node, Object object) {
        if (object instanceof List) {
            ListNode listNode = new ListNode(node);
            for (Object item : (List<Object>) object) {
                listNode.add(internalBuild(listNode, item));
            }
            return listNode;
        } else if (object instanceof Map) {
            MapNode mapNode = new MapNode(node);
            for (Map.Entry<Object, Object> kv : ((Map<Object, Object>) object).entrySet()) {
                mapNode.put(deliteral(kv.getKey().toString()), internalBuild(mapNode, kv.getValue()));
            }
            return mapNode;
        } else {
            return new ValueNode(node, object.toString());
        }
    }

    private static String deliteral(String string) {
        if (!string.contains(" ")) {
            return string;
        }
        if (string.contains("'")) {
            return "\"" + string.replace("\"", "\\\"") + "\"";
        }
        return "'" + string.replace("'", "\\'") + "'";
    }
}
