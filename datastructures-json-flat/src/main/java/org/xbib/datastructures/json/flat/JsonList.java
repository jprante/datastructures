package org.xbib.datastructures.json.flat;

import java.util.ArrayList;
import java.util.List;

class JsonList<E> extends ArrayList<E> {

    public JsonList() {
        super();
    }

    public JsonList(List<E> values) {
        super(values);
    }

    /*@Override
    public String toString() {
        boolean append = false;
        StringBuilder result = new StringBuilder("[");
        for (E e : this) {
            if (append) {
                result.append(",");
            }
            result.append(e);
            append = true;
        }
        result.append("]");
        return result.toString();
    }*/

}
