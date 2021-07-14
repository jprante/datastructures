package org.xbib.datastructures.json.micro;

public interface Function<T, R> {
    R apply(T t);
}
