package org.xbib.datastructures.json.flat;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
class JsonList<E> extends ArrayList<E> {

    public JsonList() {
        super();
    }

    public JsonList(List<E> values) {
        super(values);
    }
}
