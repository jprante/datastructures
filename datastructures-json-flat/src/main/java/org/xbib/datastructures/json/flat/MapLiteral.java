package org.xbib.datastructures.json.flat;

import java.util.Map;

class MapLiteral extends Literal {

    private final Map<String, Node> map;

    MapLiteral() {
        this.map = new JsonMap<>();
    }

    @Override
    public boolean isMap() {
        return true;
    }

    @Override
    public Map<String, Node> asMap() {
        return map;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.beginObject();
        for (Map.Entry<String, Node> entry : map.entrySet()) {
            visitor.onString(entry.getKey());
            entry.getValue().accept(visitor);
        }
        visitor.endObject();
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
