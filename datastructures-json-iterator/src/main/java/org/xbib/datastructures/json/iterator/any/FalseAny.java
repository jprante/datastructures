package org.xbib.datastructures.json.iterator.any;

import org.xbib.datastructures.json.iterator.ValueType;
import org.xbib.datastructures.json.iterator.output.JsonStream;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

class FalseAny extends Any {

    public final static FalseAny INSTANCE = new FalseAny();

    @Override
    public ValueType valueType() {
        return ValueType.BOOLEAN;
    }

    @Override
    public Object object() {
        return Boolean.FALSE;
    }

    @Override
    public boolean toBoolean() {
        return false;
    }

    @Override
    public int toInt() {
        return 0;
    }

    @Override
    public long toLong() {
        return 0;
    }

    @Override
    public float toFloat() {
        return 0;
    }

    @Override
    public double toDouble() {
        return 0;
    }

    @Override
    public BigInteger toBigInteger() {
        return BigInteger.ZERO;
    }

    @Override
    public BigDecimal toBigDecimal() {
        return BigDecimal.ZERO;
    }

    @Override
    public String toString() {
        return "false";
    }

    @Override
    public void writeTo(JsonStream stream) throws IOException {
        stream.writeFalse();
    }
}
