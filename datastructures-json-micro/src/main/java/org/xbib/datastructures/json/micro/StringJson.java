package org.xbib.datastructures.json.micro;

import java.util.Collections;
import java.util.List;

public class StringJson extends Json {

    String val;

    StringJson() {
    }

    StringJson(Json e) {
        super(e);
    }

    StringJson(String val, Json e) {
        super(e);
        this.val = val;
    }

    public Json dup() {
        return new org.xbib.datastructures.json.micro.StringJson(val, null);
    }

    public boolean isString() {
        return true;
    }

    public Object getValue() {
        return val;
    }

    public String asString() {
        return val;
    }

    public int asInteger() {
        return Integer.parseInt(val);
    }

    public float asFloat() {
        return Float.parseFloat(val);
    }

    public double asDouble() {
        return Double.parseDouble(val);
    }

    public long asLong() {
        return Long.parseLong(val);
    }

    public short asShort() {
        return Short.parseShort(val);
    }

    public byte asByte() {
        return Byte.parseByte(val);
    }

    public char asChar() {
        return val.charAt(0);
    }

    public List<Object> asList() {
        return Collections.singletonList(val);
    }

    public String toString() {
        return '"' + escaper.escapeJsonString(val) + '"';
    }

    public String toString(int maxCharacters) {
        if (val.length() <= maxCharacters) {
            return toString();
        } else {
            return '"' + escaper.escapeJsonString(val.subSequence(0, maxCharacters)) + "...\"";
        }
    }

    public int hashCode() {
        return val.hashCode();
    }

    public boolean equals(Object x) {
        return x instanceof org.xbib.datastructures.json.micro.StringJson && ((org.xbib.datastructures.json.micro.StringJson) x).val.equals(val);
    }
}
