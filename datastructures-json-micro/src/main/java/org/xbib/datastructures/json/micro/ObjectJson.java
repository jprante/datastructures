package org.xbib.datastructures.json.micro;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;

class ObjectJson extends Json {

    Map<String, Json> object = new HashMap<String, Json>();

    ObjectJson() {
    }

    ObjectJson(Json e) {
        super(e);
    }

    public Json dup() {
        org.xbib.datastructures.json.micro.ObjectJson j = new org.xbib.datastructures.json.micro.ObjectJson();
        for (Map.Entry<String, Json> e : object.entrySet()) {
            Json v = e.getValue().dup();
            v.enclosing = j;
            j.object.put(e.getKey(), v);
        }
        return j;
    }

    public boolean has(String property) {
        return object.containsKey(property);
    }

    public boolean is(String property, Object value) {
        Json p = object.get(property);
        if (p == null) {
            return false;
        } else {
            return p.equals(make(value));
        }
    }

    public Json at(String property) {
        return object.get(property);
    }

    protected Json withOptions(Json other, Json allOptions, String path) {
        if (!allOptions.has(path)) {
            allOptions.set(path, object());
        }
        Json options = allOptions.at(path, object());
        boolean duplicate = options.is("dup", true);
        if (options.is("merge", true)) {
            for (Map.Entry<String, Json> e : other.asJsonMap().entrySet()) {
                Json local = object.get(e.getKey());
                if (local instanceof org.xbib.datastructures.json.micro.ObjectJson) {
                    ((org.xbib.datastructures.json.micro.ObjectJson) local).withOptions(e.getValue(), allOptions, path + "/" + e.getKey());
                } else if (local instanceof ArrayJson) {
                    ((ArrayJson) local).withOptions(e.getValue(), allOptions, path + "/" + e.getKey());
                } else {
                    set(e.getKey(), duplicate ? e.getValue().dup() : e.getValue());
                }
            }
        } else if (duplicate) {
            for (Map.Entry<String, Json> e : other.asJsonMap().entrySet()) {
                set(e.getKey(), e.getValue().dup());
            }
        } else {
            for (Map.Entry<String, Json> e : other.asJsonMap().entrySet()) {
                set(e.getKey(), e.getValue());
            }
        }
        return this;
    }

    public Json with(Json x, Json... options) {
        if (x == null) {
            return this;
        }
        if (!x.isObject()) {
            throw new UnsupportedOperationException();
        }
        if (options.length > 0) {
            Json O = collectWithOptions(options);
            return withOptions(x, O, "");
        } else {
            for (Map.Entry<String, Json> e : x.asJsonMap().entrySet()) {
                set(e.getKey(), e.getValue());
            }
        }
        return this;
    }

    public Json set(String property, Json el) {
        if (property == null) {
            throw new IllegalArgumentException("Null property names are not allowed, value is " + el);
        }
        if (el == null) {
            el = nil();
        }
        setParent(el, this);
        object.put(property, el);
        return this;
    }

    public Json atDel(String property) {
        Json el = object.remove(property);
        removeParent(el, this);
        return el;
    }

    public Json delAt(String property) {
        Json el = object.remove(property);
        removeParent(el, this);
        return this;
    }

    public Object getValue() {
        return asMap();
    }

    public boolean isObject() {
        return true;
    }

    public Map<String, Object> asMap() {
        HashMap<String, Object> m = new HashMap<String, Object>();
        for (Map.Entry<String, Json> e : object.entrySet()) {
            m.put(e.getKey(), e.getValue().getValue());
        }
        return m;
    }

    @Override
    public Map<String, Json> asJsonMap() {
        return object;
    }

    public String toString() {
        return toString(Integer.MAX_VALUE);
    }

    public String toString(int maxCharacters) {
        return toStringImpl(maxCharacters, new IdentityHashMap<Json, Json>());
    }

    String toStringImpl(int maxCharacters, Map<Json, Json> done) {
        StringBuilder sb = new StringBuilder("{");
        if (done.containsKey(this)) {
            return sb.append("...}").toString();
        }
        done.put(this, this);
        for (Iterator<Map.Entry<String, Json>> i = object.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry<String, Json> x = i.next();
            sb.append('"');
            sb.append(escaper.escapeJsonString(x.getKey()));
            sb.append('"');
            sb.append(":");
            String s = x.getValue().isObject() ? ((org.xbib.datastructures.json.micro.ObjectJson) x.getValue()).toStringImpl(maxCharacters, done)
                    : x.getValue().isArray() ? ((ArrayJson) x.getValue()).toStringImpl(maxCharacters, done)
                    : x.getValue().toString(maxCharacters);
            if (sb.length() + s.length() > maxCharacters) {
                s = s.substring(0, Math.max(0, maxCharacters - sb.length()));
            }
            sb.append(s);
            if (i.hasNext()) {
                sb.append(",");
            }
            if (sb.length() >= maxCharacters) {
                sb.append("...");
                break;
            }
        }
        sb.append("}");
        return sb.toString();
    }

    public int hashCode() {
        return object.hashCode();
    }

    public boolean equals(Object x) {
        return x instanceof org.xbib.datastructures.json.micro.ObjectJson && ((org.xbib.datastructures.json.micro.ObjectJson) x).object.equals(object);
    }
}
