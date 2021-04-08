package org.xbib.datastructures.json.model;

public class ValueNode extends Node {

    private String value;

    private TextType type;

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

    public enum TextType {
        LINE,
        MULTILINE,
        TEXT,
        TEXT_ANGLE
    }
}
