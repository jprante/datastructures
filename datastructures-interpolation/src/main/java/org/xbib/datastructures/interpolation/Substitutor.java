package org.xbib.datastructures.interpolation;

public interface Substitutor<T> {

    String substitute(String captured, T arg);
}
