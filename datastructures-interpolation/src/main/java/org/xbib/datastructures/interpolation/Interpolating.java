package org.xbib.datastructures.interpolation;

import java.util.List;

public interface Interpolating<T> {

    List<Substitution> interpolate(String toInterpolate, T arg);
}
