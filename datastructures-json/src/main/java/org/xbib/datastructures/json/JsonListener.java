package org.xbib.datastructures.json;

public interface JsonListener {

    void begin();

    void end();

    void onNull();

    void onTrue();

    void onFalse();

    void onKey(CharSequence key);

    void onValue(CharSequence value);

    void onLong(Long value);

    void onDouble(Double value);

    void beginList();

    void endList();

    void beginMap();

    void endMap();
}
