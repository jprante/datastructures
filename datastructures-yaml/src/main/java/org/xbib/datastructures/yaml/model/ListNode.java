package org.xbib.datastructures.yaml.model;

import org.xbib.datastructures.tiny.TinyList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListNode extends Node {

    private final TinyList.Builder<Node> items;

    private final List<List<String>> comments;

    public ListNode(Node parent) {
        super(parent);
        this.items = TinyList.builder();
        this.comments = TinyList.builder();
    }

    public List<Node> getItems() {
        return items.build();
    }

    public List<List<String>> getComments() {
        return comments;
    }

    public List<Map.Entry<Node, List<String>>> getItemCommentPairs() {
        List<Map.Entry<Node, List<String>>> pairs = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            pairs.add(Map.entry(items.get(i), comments.get(i)));
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

    public boolean hasItem(int index) {
        return index >= 0 && index < items.size();
    }

    public Node getItem(int index) {
        return items.get(index);
    }

    public void addItem(Node node) {
        items.add(node);
        comments.add(TinyList.builder());
    }

    public void insertItem(int index, Node node) {
        items.add(index, node);
        comments.add(index, TinyList.builder());
    }
}
