package org.xbib.datastructures.json.iterator.any;

import org.xbib.datastructures.json.iterator.ValueType;
import org.xbib.datastructures.json.iterator.output.JsonStream;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

class FloatAny extends Any {

    private float val;

    public FloatAny(float val) {
        this.val = val;
    }

    @Override
    public ValueType valueType() {
        return ValueType.NUMBER;
    }

    @Override
    public Object object() {
        return val;
    }

    @Override
    public boolean toBoolean() {
        return val != 0;
    }

    @Override
    public int toInt() {
        return (int) val;
    }

    @Override
    public long toLong() {
        return (long) val;
    }

    @Override
    public float toFloat() {
        return val;
    }

    @Override
    public double toDouble() {
        return val;
    }

    @Override
    public BigInteger toBigInteger() {
        return BigInteger.valueOf((long) val);
    }

    @Override
    public BigDecimal toBigDecimal() {
        return BigDecimal.valueOf(val);
    }

    @Override
    public String toString() {
        return String.valueOf(val);
    }

    public Any set(float newVal) {
        this.val = newVal;
        return this;
    }

    @Override
    public void writeTo(JsonStream stream) throws IOException {
        stream.writeVal(val);
    }
}
