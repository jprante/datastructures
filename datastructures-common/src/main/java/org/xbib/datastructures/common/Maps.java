package org.xbib.datastructures.common;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Maps {

    private Maps() {
    }

    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> getListMap(Map<String, Object> map, String key) {
        Object o = get(map, key);
        if (o instanceof List) {
            return (List<Map<String, Object>>) o;
        } else if (o instanceof Map) {
            return List.of((Map<String, Object>) o);
        }
        return List.of();
    }

    @SuppressWarnings({"unchecked"})
    public static Map<String, Object> deepMerge(Map<String, Object> map, Map<String, Object> newMap) {
        for (Map.Entry<String, Object> e : newMap.entrySet()) {
            String key = e.getKey();
            Object value = e.getValue();
            if (map.containsKey(key)) {
                Object originalValue = map.get(key);
                if (originalValue instanceof Collection && value instanceof Collection) {
                    ((Collection<Object>) originalValue).addAll((Collection<Object>) value);
                } else if (originalValue instanceof Map && value instanceof Map) {
                    deepMerge((Map<String, Object>) originalValue, (Map<String, Object>) value);
                }
            } else {
                map.put(key, value);
            }
        }
        return map;
    }

    public static String getString(Map<?, ?> map, String key) {
        Object object = get(map, key);
        if (object instanceof List) {
            return ((List<?>) object).get(0).toString();
        }
        if (object instanceof Map) {
            return null;
        }
        return (String) object;
    }

    public static Long getLong(Map<?, ?> map, String key, Long defaultValue) {
        if (map.containsKey(key)) {
            try {
                Object o = get(map, key);
                return o == null ? null : o instanceof Long ? (Long) o : Long.parseLong(o.toString());
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    public static Integer getInteger(Map<?, ?> map, String key, Integer defaultValue) {
        if (map.containsKey(key)) {
            try {
                Object o = get(map, key);
                return o == null ? null : o instanceof Integer ? (Integer) o : Integer.parseInt(o.toString());
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    public static Boolean getBoolean(Map<?, ?> map, String key, Boolean defaultValue) {
        if (map.containsKey(key)) {
            Object o = get(map, key);
            return o == null ? null : o instanceof Boolean ? (Boolean) o : Boolean.parseBoolean(o.toString());
        } else {
            return defaultValue;
        }
    }

    private static <T> T get(Map<?, ?> map, String key) {
        return get(map, key.split("\\."));
    }

    @SuppressWarnings("unchecked")
    private static <T> T get(Map<?, ?> map, String[] keys) {
        if (map == null) {
            return null;
        }
        String key = keys[0];
        Object o = map.get(key);
        if (o == null) {
            return null;
        }
        if (!(o instanceof List)) {
            o = Collections.singletonList(o);
        }
        List<?> list = (List<?>) o;
        if (keys.length == 1) {
            return (T) list.get(0);
        }
        for (Object oo : list) {
            if (oo instanceof Map) {
                Map<?, ?> m = (Map<?, ?>) oo;
                if (keys.length == 2) {
                    if (m.containsKey(keys[1])) {
                        return (T) m.get(keys[1]);
                    }
                } else {
                    Object ooo = get(m, Arrays.copyOfRange(keys, 1, keys.length));
                    if (ooo != null) {
                        return (T) ooo;
                    }
                }
            } else if (oo instanceof List) {
                List<?> l = (List<?>) oo;
                return (T) l;
            } else {
                return (T) oo;
            }
        }
        return null;
    }
}
