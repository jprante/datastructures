package org.xbib.datastructures.json.flat;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public interface Node {

    boolean isNull();

    boolean isBoolean();

    boolean isNumber();

    boolean isString();

    boolean isList();

    boolean isMap();

    boolean asBoolean();

    int asInt();

    long asLong();

    float asFloat();
    
    double asDouble();

    BigInteger asBigInteger();

    BigDecimal asBigDecimal();

    String asString();

    List<Node> asList();

    Map<String, Node> asMap();

    void accept(Visitor visitor);
}
