package org.xbib.datastructures.json.tiny;

import org.xbib.datastructures.api.Generator;
import org.xbib.datastructures.api.Node;
import org.xbib.datastructures.tiny.TinyList;
import org.xbib.datastructures.tiny.TinyMap;

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

    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(Node<?> root) {
        return (Map<String, Object>) internalMap(root);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> toModifiableMap(Node<?> root) {
        return (Map<String, Object>) internalModifiableMap(root);
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

    private static Object internalMap(Node<?> curnode) {
        if (curnode instanceof ValueNode) {
            ValueNode valueNode = (ValueNode) curnode;
            return valueNode.get();
        } else if (curnode instanceof MapNode) {
            MapNode mapNode = (MapNode) curnode;
            TinyMap.Builder<String, Object> map = TinyMap.builder();
            for (Map.Entry<CharSequence, Node<?>> e : mapNode.get().entrySet()) {
                map.put(e.getKey().toString(), internalMap(e.getValue()));
            }
            return map.build();
        } else if (curnode instanceof ListNode) {
            ListNode listNode = (ListNode) curnode;
            TinyList.Builder<Object> list = TinyList.builder();
            for (Node<?> node : listNode.get()) {
                list.add(internalMap(node));
            }
            return list.build();
        } else {
            return null;
        }
    }

    private static Object internalModifiableMap(Node<?> curnode) {
        if (curnode instanceof ValueNode) {
            ValueNode valueNode = (ValueNode) curnode;
            return valueNode.get();
        } else if (curnode instanceof MapNode) {
            MapNode mapNode = (MapNode) curnode;
            TinyMap.Builder<String, Object> map = TinyMap.builder();
            for (Map.Entry<CharSequence, Node<?>> e : mapNode.get().entrySet()) {
                map.put(e.getKey().toString(), internalMap(e.getValue()));
            }
            return map;
        } else if (curnode instanceof ListNode) {
            ListNode listNode = (ListNode) curnode;
            TinyList.Builder<Object> list = TinyList.builder();
            for (Node<?> node : listNode.get()) {
                list.add(internalMap(node));
            }
            return list;
        } else {
            return null;
        }
    }
}
