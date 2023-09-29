package org.xbib.datastructures.interpolation;

public interface InterpolationHandler<T> {

    PrefixHandler<T> prefixedBy(String prefix);

    EnclosureOpeningHandler<T> enclosedBy(String opening);
}
