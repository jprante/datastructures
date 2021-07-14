package org.xbib.datastructures.json.micro;

import java.util.Collections;
import java.util.List;

public class NullJson extends Json {

    NullJson() {
    }

    NullJson(Json e) {
        super(e);
    }

    public Object getValue() {
        return null;
    }

    public Json dup() {
        return new NullJson();
    }

    public boolean isNull() {
        return true;
    }

    public String toString() {
        return "null";
    }

    public List<Object> asList() {
        return Collections.singletonList(null);
    }

    public int hashCode() {
        return 0;
    }

    public boolean equals(Object x) {
        return x instanceof NullJson;
    }

}
