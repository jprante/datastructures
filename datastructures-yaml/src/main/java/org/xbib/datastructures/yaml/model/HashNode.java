package org.xbib.datastructures.yaml.model;

import org.xbib.datastructures.tiny.TinyMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class HashNode extends Node {

    private final TinyMap.Builder<String, Node> children;

    private final TinyMap.Builder<String, List<String>> comments;

    public HashNode(Node parent) {
        super(parent);
        this.children = TinyMap.builder();
        this.comments = TinyMap.builder();
    }

    public Map<String, Node> getChildren() {
        return children.build();
    }

    public Map<String, List<String>> getComments() {
        return comments.build();
    }

    public List<Map.Entry<String, Map.Entry<Node, List<String>>>> getChildCommentPairs() {
        List<Map.Entry<String, Map.Entry<Node, List<String>>>> pairs = new ArrayList<>();
        for (Map.Entry<String, Node> kv : children.entrySet()) {
            pairs.add(Map.entry(kv.getKey(), Map.entry(kv.getValue(), comments.getOrDefault(kv.getKey(), Collections.emptyList()))));
        }
        return pairs;
    }

    public void addComments(String name, Collection<String> comments) {
        if (!this.comments.containsKey(name)) {
            this.comments.put(name, new ArrayList<>());
        }
        this.comments.get(name).addAll(comments);
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
