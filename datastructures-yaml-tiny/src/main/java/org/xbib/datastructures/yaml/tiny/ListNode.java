package org.xbib.datastructures.yaml.tiny;

import org.xbib.datastructures.api.Node;
import org.xbib.datastructures.tiny.TinyList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListNode implements org.xbib.datastructures.api.ListNode {

    private final int depth;

    private final TinyList.Builder<Node<?>> list;

    private final List<List<String>> comments;

    public ListNode(Node<?> parent) {
        this.depth = parent != null ? parent.getDepth() + 1 : 0;
        this.list = TinyList.builder();
        this.comments = TinyList.builder();
    }

    @Override
    public int getDepth() {
        return depth;
    }

    public List<Node<?>> get() {
        return list.build();
    }

    public List<List<String>> getComments() {
        return comments;
    }

    public List<Map.Entry<Node<?>, List<String>>> getItemCommentPairs() {
        List<Map.Entry<Node<?>, List<String>>> pairs = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            pairs.add(Map.entry(list.get(i), comments.get(i)));
        }
        return pairs;
    }

    public void addComments(int index, List<String> comments) {
        if (index >= this.comments.size()) {
            this.comments.add(index, TinyList.builder());
        }
        TinyList.Builder<String> list = (TinyList.Builder<String>) this.comments.get(index);
        if (list == null) {
            list = TinyList.builder();
        }
        list.addAll(comments);
        this.comments.set(index, list.build());
    }

    public boolean has(int index) {
        return index >= 0 && index < list.size();
    }

    public Node<?> get(int index) {
        return list.get(index);
    }

    public void add(Node<?> node) {
        list.add(node);
        comments.add(TinyList.builder());
    }

    public void add(int index, Node<?> node) {
        list.add(index, node);
        comments.add(index, TinyList.builder());
    }
}
