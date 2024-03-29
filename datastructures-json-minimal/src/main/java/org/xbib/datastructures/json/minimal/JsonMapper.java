package org.xbib.datastructures.json.minimal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonMapper {

    private JsonMapper() {
        // do not instantiate this class
    }

    public static Object asObject(JsonValue value) {
        if (value.isBoolean()) {
            return value.asBoolean();
        } else if (value.isInt()) {
            return value.asInt();
        } else if (value.isLong()) {
            return value.asLong();
        } else if (value.isFloat()) {
            return value.asFloat();
        } else if (value.isDouble()) {
            return value.asDouble();
        } else if (value.isString()) {
            return value.asString();
        } else if (value.isArray()) {
            return asList(value.asArray());
        } else if (value.isObject()) {
            return asMap(value.asObject());
        } else {
            return null;
        }
    }

    public static List<Object> asList(JsonArray array) {
        List<Object> list = new ArrayList<>(array.size());
        for (JsonValue element : array) {
            list.add(asObject(element));
        }
        return list;
    }

    public static Map<String, Object> asMap(JsonObject object) {
        Map<String, Object> map = new HashMap<>(object.size(), 1.f);
        for (JsonObjectMember member : object) {
            map.put(member.getName(), asObject(member.getValue()));
        }
        return map;
    }

    public static JsonValue asJsonValue(Object object) {
        if (object == null) {
            return JsonLiteral.NULL;
        } else if (object instanceof Boolean) {
            return Json.of((Boolean) object);
        } else if (object instanceof Integer) {
            return Json.of((Integer) object);
        } else if (object instanceof Long) {
            return Json.of((Long) object);
        } else if (object instanceof Float) {
            return Json.of((Float) object);
        } else if (object instanceof Double) {
            return Json.of((Double) object);
        } else if (object instanceof String) {
            return Json.of((String) object);
        } else if (object instanceof Collection) {
            return asJsonArray((Collection<?>) object);
        } else if (object instanceof Map) {
            return asJsonObject((Map<?, ?>) object);
        } else {
            return null;
        }
    }

    public static JsonArray asJsonArray(Collection<?> collection) {
        JsonArray array = new JsonArray();
        for (Object element : collection) {
            array.add(asJsonValue(element));
        }
        return array;
    }

    public static JsonObject asJsonObject(Map<?, ?> map) {
        JsonObject object = new JsonObject();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            object.add(String.valueOf(entry.getKey()),
                    asJsonValue(entry.getValue()));
        }
        return object;
    }
}
