package org.xbib.datastructures.json.tiny;

import org.xbib.datastructures.api.Generator;
import org.xbib.datastructures.api.Node;
import org.xbib.datastructures.tiny.TinyList;
import org.xbib.datastructures.tiny.TinyMap;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;

public class JsonGenerator implements Generator {

    private final Node<?> root;

    private JsonBuilder builder;

    public JsonGenerator(Node<?> root) {
        this.root = root;
    }

    @Override
    public void generate(Writer writer) throws IOException {
        this.builder = JsonBuilder.builder(writer);
        try (writer) {
            if (root != null) {
                internalWrite(root);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(Node<?> root) {
        return (Map<String, Object>) internalObject(root);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> toModifiableMap(Node<?> root) {
        return (Map<String, Object>) internalModifiableObject(root);
    }

    @SuppressWarnings("unchecked")
    public static Collection<Object> toCollection(Node<?> root) {
        return (Collection<Object>) internalObject(root);
    }

    @SuppressWarnings("unchecked")
    public static Collection<Object> toModifiableCollection(Node<?> root) {
        return (Collection<Object>) internalModifiableObject(root);
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

    private static Object internalObject(Node<?> curnode) {
        if (curnode instanceof ValueNode) {
            ValueNode valueNode = (ValueNode) curnode;
            return valueNode.get();
        } else if (curnode instanceof MapNode) {
            MapNode mapNode = (MapNode) curnode;
            TinyMap.Builder<String, Object> map = TinyMap.builder();
            for (Map.Entry<CharSequence, Node<?>> e : mapNode.get().entrySet()) {
                map.put(e.getKey().toString(), internalObject(e.getValue()));
            }
            return map.build();
        } else if (curnode instanceof ListNode) {
            ListNode listNode = (ListNode) curnode;
            TinyList.Builder<Object> list = TinyList.builder();
            for (Node<?> node : listNode.get()) {
                list.add(internalObject(node));
            }
            return list.build();
        } else {
            return null;
        }
    }

    private static Object internalModifiableObject(Node<?> curnode) {
        if (curnode instanceof ValueNode) {
            ValueNode valueNode = (ValueNode) curnode;
            return valueNode.get();
        } else if (curnode instanceof MapNode) {
            MapNode mapNode = (MapNode) curnode;
            TinyMap.Builder<String, Object> map = TinyMap.builder();
            for (Map.Entry<CharSequence, Node<?>> e : mapNode.get().entrySet()) {
                map.put(e.getKey().toString(), internalModifiableObject(e.getValue()));
            }
            return map;
        } else if (curnode instanceof ListNode) {
            ListNode listNode = (ListNode) curnode;
            TinyList.Builder<Object> list = TinyList.builder();
            for (Node<?> node : listNode.get()) {
                list.add(internalModifiableObject(node));
            }
            return list;
        } else {
            return null;
        }
    }
}
