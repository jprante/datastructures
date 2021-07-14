package org.xbib.datastructures.json.simple;

import java.util.List;
import java.util.Map;

@SuppressWarnings({"rawtypes"})
public interface ContainerFactory {

    Map createObjectContainer();

    List createArrayContainer();
}