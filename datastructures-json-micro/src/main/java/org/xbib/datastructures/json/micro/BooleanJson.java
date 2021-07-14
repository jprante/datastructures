package org.xbib.datastructures.json.micro;

import java.util.Collections;
import java.util.List;

public class BooleanJson extends Json {

    boolean val;

    BooleanJson() {
    }

    BooleanJson(Json e) {
        super(e);
    }

    BooleanJson(Boolean val, Json e) {
        super(e);
        this.val = val;
    }

    public Object getValue() {
        return val;
    }

    public Json dup() {
        return new org.xbib.datastructures.json.micro.BooleanJson(val, null);
    }

    public boolean asBoolean() {
        return val;
    }

    public boolean isBoolean() {
        return true;
    }

    public String toString() {
        return val ? "true" : "false";
    }

    public List<Object> asList() {
        return Collections.singletonList(val);
    }

    public int hashCode() {
        return val ? 1 : 0;
    }

    public boolean equals(Object x) {
        return x instanceof org.xbib.datastructures.json.micro.BooleanJson && ((org.xbib.datastructures.json.micro.BooleanJson) x).val == val;
    }

}
