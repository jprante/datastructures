package org.xbib.datastructures.json.flat;

public interface Visitor {

    void onNull();

    void onBoolean(boolean value);

    void onNumber(String value);

    void onString(String value);

    void beginArray();

    void endArray();

    void beginObject();

    void endObject();
}
