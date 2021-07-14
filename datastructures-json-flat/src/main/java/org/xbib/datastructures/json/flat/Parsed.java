package org.xbib.datastructures.json.flat;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

class Parsed implements Node {

    protected final Parser parser;

    protected final int element;

    Parsed(Parser parser, int element) {
        this.parser = parser;
        this.element = element;
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public boolean isBoolean() {
        return false;
    }

    @Override
    public boolean isNumber() {
        return false;
    }

    @Override
    public boolean isString() {
        return false;
    }

    @Override
    public boolean isList() {
        return false;
    }

    @Override
    public boolean isMap() {
        return false;
    }

    @Override
    public boolean asBoolean() {
        return false;
    }

    @Override
    public int asInt() {
        return 0;
    }

    @Override
    public long asLong() {
        return 0;
    }

    @Override
    public float asFloat() {
        return 0;
    }

    @Override
    public double asDouble() {
        return 0;
    }

    @Override
    public BigInteger asBigInteger() {
        return null;
    }

    @Override
    public BigDecimal asBigDecimal() {
        return null;
    }

    @Override
    public String asString() {
        return null;
    }

    @Override
    public List<Node> asList() {
        return null;
    }

    @Override
    public Map<String, Node> asMap() {
        return null;
    }

    @Override
    public void accept(Visitor visitor) {
        parser.accept(element, visitor);
    }

    @Override
    public String toString() {
        return parser.getJson(element);
    }
}
