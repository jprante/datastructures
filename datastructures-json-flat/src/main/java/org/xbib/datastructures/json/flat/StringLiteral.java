package org.xbib.datastructures.json.flat;

class StringLiteral extends Literal {

    private final String string;

    StringLiteral(String string) {
        this.string = string;
    }

    @Override
    public boolean isString() {
        return true;
    }

    @Override
    public String asString() {
        return string;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.onString(string);
    }

    @Override
    public String toString() {
        //return String.format("\"%s\"", StringUtil.escape(string));
        return string;
    }
}
