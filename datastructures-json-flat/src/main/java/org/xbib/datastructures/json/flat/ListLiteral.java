package org.xbib.datastructures.json.flat;

import java.util.List;

class ListLiteral extends Literal {

    private final List<Node> list;

    ListLiteral(List<Node> values) {
        this.list = new JsonList<>(values);
    }

    @Override
    public boolean isList() {
        return true;
    }

    @Override
    public List<Node> asList() {
        return list;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.beginArray();
        for (Node value : list) {
            value.accept(visitor);
        }
        visitor.endArray();
    }

    @Override
    public String toString() {
        return list.toString();
    }
}
