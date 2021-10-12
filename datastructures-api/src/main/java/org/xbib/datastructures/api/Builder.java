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

    Builder buildKey(CharSequence charSequence) throws IOException;

    Builder buildValue(Object object) throws IOException;

    Builder buildNull() throws IOException;

    String build();
}
