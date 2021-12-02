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

    Builder buildCollection(Collection<?> collection) throws IOException;

    Builder buildKey(CharSequence key) throws IOException;

    Builder buildValue(Object value) throws IOException;

    Builder buildNull() throws IOException;

    default Builder field(CharSequence key, Object value) throws IOException {
        buildKey(key);
        buildValue(value);
        return this;
    }

    default Builder fieldIfNotNull(CharSequence key, Object value) throws IOException {
        if (value != null){
            buildKey(key);
            buildValue(value);
        }
        return this;
    }

    default Builder collection(CharSequence key, Collection<?> value) throws IOException {
        buildKey(key);
        buildValue(value);
        return this;
    }

    default Builder beginMap(CharSequence key) throws IOException {
        buildKey(key);
        beginMap();
        return this;
    }

    default Builder beginCollection(CharSequence key) throws IOException {
        buildKey(key);
        beginCollection();
        return this;
    }

    Builder copy(Builder builder) throws IOException;

    Builder copy(String string) throws IOException;

    String build();
}
