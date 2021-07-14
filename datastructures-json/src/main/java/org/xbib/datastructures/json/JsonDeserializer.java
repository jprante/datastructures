package org.xbib.datastructures.json;

import java.util.Deque;

public interface JsonDeserializer extends JsonListener {

    Deque<Node<?>> getStack();

    Node<?> getResult();

}
