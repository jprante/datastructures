package org.xbib.datastructures.json.micro;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ArrayJson extends Json {

    List<Json> L = new ArrayList<>();

    ArrayJson() {
    }

    ArrayJson(Json e) {
        super(e);
    }

    public Json dup() {
        org.xbib.datastructures.json.micro.ArrayJson j = new org.xbib.datastructures.json.micro.ArrayJson();
        for (Json e : L) {
            Json v = e.dup();
            v.enclosing = j;
            j.L.add(v);
        }
        return j;
    }

    public Json set(int index, Object value) {
        Json jvalue = make(value);
        L.set(index, jvalue);
        setParent(jvalue, this);
        return this;
    }

    public List<Json> asJsonList() {
        return L;
    }

    public List<Object> asList() {
        ArrayList<Object> A = new ArrayList<Object>();
        for (Json x : L) {
            A.add(x.getValue());
        }
        return A;
    }

    public boolean is(int index, Object value) {
        if (index < 0 || index >= L.size()) {
            return false;
        } else {
            return L.get(index).equals(make(value));
        }
    }

    public Object getValue() {
        return asList();
    }

    public boolean isArray() {
        return true;
    }

    public Json at(int index) {
        return L.get(index);
    }

    public Json add(Json el) {
        L.add(el);
        setParent(el, this);
        return this;
    }

    public Json remove(Json el) {
        L.remove(el);
        el.enclosing = null;
        return this;
    }

    boolean isEqualJson(Json left, Json right) {
        if (left == null) {
            return right == null;
        } else {
            return left.equals(right);
        }
    }

    boolean isEqualJson(Json left, Json right, Json fields) {
        if (fields.isNull()) {
            return left.equals(right);
        } else if (fields.isString()) {
            return isEqualJson(resolvePointer(fields.asString(), left),
                    resolvePointer(fields.asString(), right));
        } else if (fields.isArray()) {
            for (Json field : fields.asJsonList()) {
                if (!isEqualJson(resolvePointer(field.asString(), left),
                        resolvePointer(field.asString(), right))) {
                    return false;
                }
            }
            return true;
        } else {
            throw new IllegalArgumentException("Compare by options should be either a property name or an array of property names: " + fields);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    int compareJson(Json left, Json right, Json fields) {
        if (fields.isNull()) {
            return ((Comparable) left.getValue()).compareTo(right.getValue());
        } else if (fields.isString()) {
            Json leftProperty = resolvePointer(fields.asString(), left);
            Json rightProperty = resolvePointer(fields.asString(), right);
            return ((Comparable) leftProperty).compareTo(rightProperty);
        } else if (fields.isArray()) {
            for (Json field : fields.asJsonList()) {
                Json leftProperty = resolvePointer(field.asString(), left);
                Json rightProperty = resolvePointer(field.asString(), right);
                int result = ((Comparable) leftProperty).compareTo(rightProperty);
                if (result != 0) {
                    return result;
                }
            }
            return 0;
        } else {
            throw new IllegalArgumentException("Compare by options should be either a property name or an array of property names: " + fields);
        }
    }

    Json withOptions(Json array, Json allOptions, String path) {
        Json opts = allOptions.at(path, object());
        boolean dup = opts.is("dup", true);
        Json compareBy = opts.at("compareBy", nil());
        if (opts.is("sort", true)) {
            int thisIndex = 0, thatIndex = 0;
            while (thatIndex < array.asJsonList().size()) {
                Json thatElement = array.at(thatIndex);
                if (thisIndex == L.size()) {
                    L.add(dup ? thatElement.dup() : thatElement);
                    thisIndex++;
                    thatIndex++;
                    continue;
                }
                int compared = compareJson(at(thisIndex), thatElement, compareBy);
                if (compared < 0) // this < that
                {
                    thisIndex++;
                } else if (compared > 0) // this > that
                {
                    L.add(thisIndex, dup ? thatElement.dup() : thatElement);
                    thatIndex++;
                } else { // equal, ignore
                    thatIndex++;
                }
            }
        } else {
            for (Json thatElement : array.asJsonList()) {
                boolean present = false;
                for (Json thisElement : L) {
                    if (isEqualJson(thisElement, thatElement, compareBy)) {
                        present = true;
                        break;
                    }
                }
                if (!present) {
                    L.add(dup ? thatElement.dup() : thatElement);
                }
            }
        }
        return this;
    }

    public Json with(Json object, Json... options) {
        if (object == null) {
            return this;
        }
        if (!object.isArray()) {
            add(object);
        } else if (options.length > 0) {
            Json O = collectWithOptions(options);
            return withOptions(object, O, "");
        } else
        // what about "enclosing" here? we don't have a provision where a Json
        // element belongs to more than one enclosing elements...
        {
            L.addAll(((org.xbib.datastructures.json.micro.ArrayJson) object).L);
        }
        return this;
    }

    public Json atDel(int index) {
        Json el = L.remove(index);
        if (el != null) {
            el.enclosing = null;
        }
        return el;
    }

    public Json delAt(int index) {
        Json el = L.remove(index);
        if (el != null) {
            el.enclosing = null;
        }
        return this;
    }

    public String toString() {
        return toString(Integer.MAX_VALUE);
    }

    public String toString(int maxCharacters) {
        return toStringImpl(maxCharacters, new IdentityHashMap<Json, Json>());
    }

    String toStringImpl(int maxCharacters, Map<Json, Json> done) {
        StringBuilder sb = new StringBuilder("[");
        for (Iterator<Json> i = L.iterator(); i.hasNext(); ) {
            Json value = i.next();
            String s = value.isObject() ? ((ObjectJson) value).toStringImpl(maxCharacters, done)
                    : value.isArray() ? ((org.xbib.datastructures.json.micro.ArrayJson) value).toStringImpl(maxCharacters, done)
                    : value.toString(maxCharacters);
            if (sb.length() + s.length() > maxCharacters) {
                s = s.substring(0, Math.max(0, maxCharacters - sb.length()));
            } else {
                sb.append(s);
            }
            if (i.hasNext()) {
                sb.append(",");
            }
            if (sb.length() >= maxCharacters) {
                sb.append("...");
                break;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public int hashCode() {
        return L.hashCode();
    }

    public boolean equals(Object x) {
        return x instanceof org.xbib.datastructures.json.micro.ArrayJson && ((org.xbib.datastructures.json.micro.ArrayJson) x).L.equals(L);
    }
}
