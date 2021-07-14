package org.xbib.datastructures.json.micro;

import java.util.Collection;
import java.util.Map;

public class DefaultFactory implements Factory {

    private static final NullJson topnull = new NullJson();

    public Json nil() {
        return topnull;
    }

    public Json bool(boolean x) {
        return new BooleanJson(x ? Boolean.TRUE : Boolean.FALSE, null);
    }

    public Json string(String x) {
        return new StringJson(x, null);
    }

    public Json number(Number x) {
        return new NumberJson(x, null);
    }

    public Json array() {
        return new ArrayJson();
    }

    public Json object() {
        return new ObjectJson();
    }

    public Json make(Object anything) {
        if (anything == null) {
            return topnull;
        } else if (anything instanceof Json) {
            return (Json) anything;
        } else if (anything instanceof String) {
            return Json.factory().string((String) anything);
        } else if (anything instanceof Collection<?>) {
            Json L = array();
            for (Object x : (Collection<?>) anything) {
                L.add(Json.factory().make(x));
            }
            return L;
        } else if (anything instanceof Map<?, ?>) {
            Json O = object();
            for (Map.Entry<?, ?> x : ((Map<?, ?>) anything).entrySet()) {
                O.set(x.getKey().toString(), Json.factory().make(x.getValue()));
            }
            return O;
        } else if (anything instanceof Boolean) {
            return Json.factory().bool((Boolean) anything);
        } else if (anything instanceof Number) {
            return Json.factory().number((Number) anything);
        } else if (anything.getClass().isArray()) {
            Class<?> comp = anything.getClass().getComponentType();
            if (!comp.isPrimitive()) {
                return Json.array((Object[]) anything);
            }
            Json A = array();
            if (boolean.class == comp) {
                for (boolean b : (boolean[]) anything) {
                    A.add(b);
                }
            } else if (byte.class == comp) {
                for (byte b : (byte[]) anything) {
                    A.add(b);
                }
            } else if (char.class == comp) {
                for (char b : (char[]) anything) {
                    A.add(b);
                }
            } else if (short.class == comp) {
                for (short b : (short[]) anything) {
                    A.add(b);
                }
            } else if (int.class == comp) {
                for (int b : (int[]) anything) {
                    A.add(b);
                }
            } else if (long.class == comp) {
                for (long b : (long[]) anything) {
                    A.add(b);
                }
            } else if (float.class == comp) {
                for (float b : (float[]) anything) {
                    A.add(b);
                }
            } else if (double.class == comp) {
                for (double b : (double[]) anything) {
                    A.add(b);
                }
            }
            return A;
        } else {
            throw new IllegalArgumentException("Don't know how to convert to Json : " + anything);
        }
    }
}
