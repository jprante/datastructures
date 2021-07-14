package org.xbib.datastructures.json.flat;

class NullLiteral extends Literal {

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.onNull();
    }

    @Override
    public String toString() {
        return "null";
    }
}
