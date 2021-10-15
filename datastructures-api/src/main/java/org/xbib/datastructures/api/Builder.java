package org.xbib.datastructures.api;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public interface Builder {

    Builder beginCollection() throws IOException;

    Builder endCollection() throws IOException;

    Builder beginMap() throws IOException;

    Builder endMap() throws IOException;

    Builder buildMap(Map<String, Object> map) throws IOException;

    Builder buildCollection(Collection<Object> collection) throws IOException;

    Builder buildKey(CharSequence key) throws IOException;

    Builder buildValue(Object value) throws IOException;

    Builder buildNull() throws IOException;

    default Builder buildIfNotNull(CharSequence key, Object value) throws IOException {
        if (value != null){
            buildKey(key);
            buildValue(value);
        }
        return this;
    }

    String build();
}
