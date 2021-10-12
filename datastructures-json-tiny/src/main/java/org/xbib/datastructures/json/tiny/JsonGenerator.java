package org.xbib.datastructures.json.tiny;

import org.xbib.datastructures.api.Generator;
import org.xbib.datastructures.api.Node;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class JsonGenerator implements Generator {

    private final Node<?> root;

    private JsonBuilder builder;

    public JsonGenerator(Node<?> root) {
        this.root = root;
    }

    @Override
    public void generate(Writer writer) throws IOException {
        this.builder = new JsonBuilder(writer);
        try (writer) {
            if (root != null) {
                internalWrite(root);
            }
        }
    }

    private void internalWrite(Node<?> curnode) throws IOException {
        if (curnode instanceof ValueNode) {
            ValueNode valueNode = (ValueNode) curnode;
            Object object = valueNode.get();
            builder.buildValue(object);
        } else if (curnode instanceof MapNode) {
            MapNode mapNode = (MapNode) curnode;
            builder.beginMap();
            for (Map.Entry<CharSequence, Node<?>> e : mapNode.get().entrySet()) {
                builder.buildKey(e.getKey());
                internalWrite(e.getValue());
            }
            builder.endMap();
        } else if (curnode instanceof ListNode) {
            ListNode listNode = (ListNode) curnode;
            builder.beginCollection();
            for (Node<?> node : listNode.get()) {
                internalWrite(node);
            }
            builder.endCollection();
        }
    }
}
