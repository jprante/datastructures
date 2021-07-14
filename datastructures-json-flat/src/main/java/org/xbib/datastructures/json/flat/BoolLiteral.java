package org.xbib.datastructures.json.flat;

class BoolLiteral extends Literal {

    private final boolean value;

    BoolLiteral(boolean value) {
        this.value = value;
    }

    @Override
    public boolean isBoolean() {
        return true;
    }

    @Override
    public boolean asBoolean() {
        return value;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.onBoolean(value);
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }
}
