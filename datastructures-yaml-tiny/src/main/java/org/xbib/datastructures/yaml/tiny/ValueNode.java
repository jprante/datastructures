package org.xbib.datastructures.yaml.tiny;

import org.xbib.datastructures.api.Node;

import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class ValueNode implements org.xbib.datastructures.api.ValueNode {

    private final int depth;

    private Object value;

    private TextType type;

    private List<String> comments;

    public ValueNode(Node<?> parent) {
        this(parent, "");
    }

    public ValueNode(Node<?> parent, String value) {
        this(parent, value, TextType.LINE);
    }

    public ValueNode(Node<?> parent, String value, TextType type) {
        this.depth = parent != null ? parent.getDepth() + 1 : 0;
        this.value = value;
        this.type = type;
    }

    @Override
    public int getDepth() {
        return depth;
    }

    @Override
    public void set(Object value) {
        this.value = value;
    }

    @Override
    public Object get() {
        return value;
    }

    public void setType(TextType type) {
        this.type = type;
    }

    public TextType getType() {
        return type;
    }

    public List<String> getComments() {
        return comments != null ? comments : Collections.emptyList();
    }

    public void addComments(Collection<String> comments) {
        if (this.comments == null) {
            this.comments = new ArrayList<>();
        }
        this.comments.addAll(comments);
    }

    public enum TextType {
        LINE,
        MULTILINE,
        TEXT,
        TEXT_ANGLE
    }
}
