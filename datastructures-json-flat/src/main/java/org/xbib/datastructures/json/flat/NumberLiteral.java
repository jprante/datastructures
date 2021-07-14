package org.xbib.datastructures.json.flat;

import java.math.BigDecimal;
import java.math.BigInteger;

class NumberLiteral extends Literal {

    private final String value;

    NumberLiteral(String value) {
        this.value = value;
    }

    @Override
    public boolean isNumber() {
        return true;
    }

    @Override
    public int asInt() {
        return Integer.parseInt(value);
    }

    @Override
    public long asLong() {
        return Long.parseLong(value);
    }

    @Override
    public float asFloat() {
        return Float.parseFloat(value);
    }

    @Override
    public double asDouble() {
        return Double.parseDouble(value);
    }

    @Override
    public BigInteger asBigInteger() {
        return new BigInteger(value);
    }

    @Override
    public BigDecimal asBigDecimal() {
        return new BigDecimal(value);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.onNumber(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
