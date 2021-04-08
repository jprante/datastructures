package org.xbib.datastructures.json.model;

import org.xbib.datastructures.tiny.TinyMap;
import java.util.Map;

public class HashNode extends Node {

    private final TinyMap.Builder<String, Node> children;

    public HashNode(Node parent) {
        super(parent);
        this.children = TinyMap.builder();
    }

    public Map<String, Node> getChildren() {
        return children.build();
    }

    public boolean hasChild(String name) {
        return children.containsKey(name);
    }

    public Node getChild(String name) {
        return children.get(name);
    }

    public void putChild(String name, Node node) {
        children.put(name, node);
    }
}
