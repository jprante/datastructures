package org.xbib.datastructures.yaml.model;

import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class ValueNode extends Node {

    private String value;

    private TextType type;

    private List<String> comments;

    public ValueNode(Node parent) {
        this(parent, "");
    }

    public ValueNode(Node parent, String value) {
        this(parent, value, TextType.LINE);
    }

    public ValueNode(Node parent, String value, TextType type) {
        super(parent);
        this.value = value;
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
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
