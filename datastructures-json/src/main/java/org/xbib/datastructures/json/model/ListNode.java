package org.xbib.datastructures.json.model;

import org.xbib.datastructures.tiny.TinyList;
import java.util.List;

public class ListNode extends Node {

    private final TinyList.Builder<Node> items;

    public ListNode(Node parent) {
        super(parent);
        this.items = TinyList.builder();
    }

    public List<Node> getItems() {
        return items.build();
    }

    public boolean hasItem(int index) {
        return index >= 0 && index < items.size();
    }

    public Node getItem(int index) {
        return items.get(index);
    }

    public void addItem(Node node) {
        items.add(node);
    }

    public void insertItem(int index, Node node) {
        items.add(index, node);
    }
}
