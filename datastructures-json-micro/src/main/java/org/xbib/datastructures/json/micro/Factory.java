package org.xbib.datastructures.json.micro;

public interface Factory {

    Json nil();

    Json bool(boolean value);

    Json string(String value);

    Json number(Number value);

    Json object();

    Json array();

    Json make(Object anything);
}
