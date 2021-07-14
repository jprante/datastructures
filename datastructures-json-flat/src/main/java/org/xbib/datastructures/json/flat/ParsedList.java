package org.xbib.datastructures.json.flat;

import java.util.List;

class ParsedList extends Parsed {

    private List<Node> array;

    ParsedList(Parser parser, int element) {
        super(parser, element);
    }

    @Override
    public boolean isList() {
        return true;
    }

    @Override
    public List<Node> asList() {
        if (array == null) {
            array = createArray();
        }
        return array;
    }

    @Override
    public String toString() {
        return (array == null) ? super.toString() : array.toString();
    }

    private List<Node> createArray() {
        List<Node> result = new JsonList<>();
        int e = element + 1;
        while (e <= element + parser.getNested(element)) {
            result.add(Json.create(parser, e));
            e += parser.getNested(e) + 1;
        }
        return result;
    }
}
