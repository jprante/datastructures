package org.xbib.datastructures.yaml.tiny;

import org.xbib.datastructures.api.Node;
import org.xbib.datastructures.tiny.TinyMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MapNode implements org.xbib.datastructures.api.MapNode {

    private final int depth;

    private final TinyMap.Builder<CharSequence, Node<?>> map;

    private final TinyMap.Builder<String, List<String>> comments;

    public MapNode(Node<?> parent) {
        this.depth = parent != null ? parent.getDepth() + 1 : 0;
        this.map = TinyMap.builder();
        this.comments = TinyMap.builder();
    }

    public int getDepth() {
        return depth;
    }

    public Map<CharSequence, Node<?>> get() {
        return map.build();
    }

    public Map<String, List<String>> getComments() {
        return comments.build();
    }

    public List<Map.Entry<CharSequence, Map.Entry<Node<?>, List<String>>>> getChildCommentPairs() {
        List<Map.Entry<CharSequence, Map.Entry<Node<?>, List<String>>>> pairs = new ArrayList<>();
        for (Map.Entry<CharSequence, Node<?>> kv : map.entrySet()) {
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

    public boolean has(String name) {
        return map.containsKey(name);
    }

    public Node<?> get(String name) {
        return map.get(name);
    }

    public void put(String name, Node<?> node) {
        map.put(name, node);
    }
}
