package org.xbib.datastructures.interpolation;

public interface SubstitutionHandler<T> {

    void handleWith(Substitutor<T> substitutor);
}
