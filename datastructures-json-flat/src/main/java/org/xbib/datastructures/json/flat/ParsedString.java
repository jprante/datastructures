package org.xbib.datastructures.json.flat;

class ParsedString extends Parsed {

    private String string;

    ParsedString(Parser parser, int element) {
        super(parser, element);
    }

    @Override
    public boolean isString() {
        return true;
    }

    @Override
    public String asString() {
        if (string == null) {
            string = parser.getUnescapedString(element);
        }
        return string;
    }
}
