package org.xbib.datastructures.json.micro;

import java.util.Collections;
import java.util.List;

public class NumberJson extends Json {

    Number val;

    NumberJson() {
    }

    NumberJson(Json e) {
        super(e);
    }

    NumberJson(Number val, Json e) {
        super(e);
        this.val = val;
    }

    public Json dup() {
        return new NumberJson(val, null);
    }

    public boolean isNumber() {
        return true;
    }

    public Object getValue() {
        return val;
    }

    public String asString() {
        return val.toString();
    }

    public int asInteger() {
        return val.intValue();
    }

    public float asFloat() {
        return val.floatValue();
    }

    public double asDouble() {
        return val.doubleValue();
    }

    public long asLong() {
        return val.longValue();
    }

    public short asShort() {
        return val.shortValue();
    }

    public byte asByte() {
        return val.byteValue();
    }

    public List<Object> asList() {
        return Collections.singletonList(val);
    }

    public String toString() {
        return val.toString();
    }

    public int hashCode() {
        return val.hashCode();
    }

    public boolean equals(Object x) {
        return x instanceof NumberJson && val.doubleValue() == ((NumberJson) x).val.doubleValue();
    }
}
