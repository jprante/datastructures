package org.xbib.datastructures.json.flat;

import java.util.Map;

class ParsedMap extends Parsed {

    private Map<String, Node> map;

    ParsedMap(Parser parser, int element) {
        super(parser, element);
    }

    @Override
    public boolean isMap() {
        return true;
    }

    @Override
    public Map<String, Node> asMap() {
        if (map == null) {
            map = createMap();
        }
        return map;
    }

    @Override
    public String toString() {
        return (map == null) ? super.toString() : map.toString();
    }

    private Map<String, Node> createMap() {
        Map<String, Node> result = new JsonMap<>();
        int e = element + 1;
        while (e <= element + parser.getNested(element)) {
            String key = parser.getUnescapedString(e);
            result.put(key, Json.create(parser, e + 1));
            e += parser.getNested(e + 1) + 2;
        }
        return result;
    }
}
