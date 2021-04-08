package org.xbib.datastructures.yaml;

import org.xbib.datastructures.yaml.model.HashNode;
import org.xbib.datastructures.yaml.model.ListNode;
import org.xbib.datastructures.yaml.model.Node;
import org.xbib.datastructures.yaml.model.ValueNode;
import java.util.List;
import java.util.Map;

public class Builder {

    public Object object;

    public Builder(Object object) {
        this.object = object;
    }

    public Node build() {
        return object == null ? null : internalBuild(null, object);
    }

    @SuppressWarnings("unchecked")
    private Node internalBuild(Node node, Object object) {
        if (object instanceof List) {
            ListNode listNode = new ListNode(node);
            for (Object item : (List<Object>) object) {
                listNode.addItem(internalBuild(listNode, item));
            }
            return listNode;
        } else if (object instanceof Map) {
            HashNode hashNode = new HashNode(node);
            for (Map.Entry<Object, Object> kv : ((Map<Object, Object>) object).entrySet()) {
                hashNode.putChild(deliteral(kv.getKey().toString()), internalBuild(hashNode, kv.getValue()));
            }
            return hashNode;
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
