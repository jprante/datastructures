package org.xbib.datastructures.json.micro;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.text.CharacterIterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Json  {

    static String fetchContent(URL url) {
		try (java.io.Reader reader = new InputStreamReader((InputStream) url.getContent())) {
			StringBuilder content = new StringBuilder();
			char[] buf = new char[1024];
			for (int n = reader.read(buf); n > -1; n = reader.read(buf)) {
				content.append(buf, 0, n);
			}
			return content.toString();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
    }

    static Json resolvePointer(String pointerRepresentation, Json top) {
        String[] parts = pointerRepresentation.split("/");
        Json result = top;
        for (String p : parts) {
            // TODO: unescaping and decoding
			if (p.length() == 0) {
				continue;
			}
            p = p.replace("~1", "/").replace("~0", "~");
			if (result.isArray()) {
				result = result.at(Integer.parseInt(p));
			} else if (result.isObject()) {
				result = result.at(p);
			} else {
				throw new RuntimeException("Can't resolve pointer " + pointerRepresentation +
						" on document " + top.toString(200));
			}
        }
        return result;
    }

    static URI makeAbsolute(URI base, String ref) throws Exception {
        URI refuri;
        if (base != null && base.getAuthority() != null && !new URI(ref).isAbsolute()) {
            StringBuilder sb = new StringBuilder();
			if (base.getScheme() != null) {
				sb.append(base.getScheme()).append("://");
			}
            sb.append(base.getAuthority());
            if (!ref.startsWith("/")) {
				if (ref.startsWith("#")) {
					sb.append(base.getPath());
				} else {
					int slashIdx = base.getPath().lastIndexOf('/');
					sb.append(slashIdx == -1 ? base.getPath() : base.getPath().substring(0, slashIdx)).append("/");
				}
            }
            refuri = new URI(sb.append(ref).toString());
        } else if (base != null) {
			refuri = base.resolve(ref);
		} else {
			refuri = new URI(ref);
		}
        return refuri;
    }

    static Json resolveRef(URI base,
                           Json refdoc,
                           URI refuri,
                           Map<String, Json> resolved,
                           Map<Json, Json> expanded,
                           Function<URI, Json> uriResolver) throws Exception {
        if (refuri.isAbsolute() &&
                (base == null || !base.isAbsolute() ||
                        !base.getScheme().equals(refuri.getScheme()) ||
                        !Objects.equals(base.getHost(), refuri.getHost()) ||
                        base.getPort() != refuri.getPort() ||
                        !base.getPath().equals(refuri.getPath()))) {
            URI docuri = null;
            refuri = refuri.normalize();
			if (refuri.getHost() == null) {
				docuri = new URI(refuri.getScheme() + ":" + refuri.getPath());
			} else {
				docuri = new URI(refuri.getScheme() + "://" + refuri.getHost() +
						((refuri.getPort() > -1) ? ":" + refuri.getPort() : "") +
						refuri.getPath());
			}
            refdoc = uriResolver.apply(docuri);
            refdoc = expandReferences(refdoc, refdoc, docuri, resolved, expanded, uriResolver);
        }
		if (refuri.getFragment() == null) {
			return refdoc;
		} else {
			return resolvePointer(refuri.getFragment(), refdoc);
		}
    }

    static Json expandReferences(Json json,
                                 Json topdoc,
                                 URI base,
                                 Map<String, Json> resolved,
                                 Map<Json, Json> expanded,
                                 Function<URI, Json> uriResolver) throws Exception {
		if (expanded.containsKey(json)) {
			return json;
		}
        if (json.isObject()) {
            if (json.has("id") && json.at("id").isString()) // change scope of nest references
            {
                base = base.resolve(json.at("id").asString());
            }

            if (json.has("$ref")) {
                URI refuri = makeAbsolute(base, json.at("$ref").asString()); // base.resolve(json.at("$ref").asString());
                Json ref = resolved.get(refuri.toString());
                if (ref == null) {
                    ref = Json.object();
                    resolved.put(refuri.toString(), ref);
                    ref.with(resolveRef(base, topdoc, refuri, resolved, expanded, uriResolver));
                }
                json = ref;
            } else {
				for (Map.Entry<String, Json> e : json.asJsonMap().entrySet()) {
					json.set(e.getKey(), expandReferences(e.getValue(), topdoc, base, resolved, expanded, uriResolver));
				}
            }
        } else if (json.isArray()) {
			for (int i = 0; i < json.asJsonList().size(); i++) {
				json.set(i,
						expandReferences(json.at(i), topdoc, base, resolved, expanded, uriResolver));
			}
        }
        expanded.put(json, json);
        return json;
    }

	public static Schema schema(Json S) {
        return new DefaultSchema(null, S, null);
    }

    public static Schema schema(URI uri) {
        return schema(uri, null);
    }

    public static Schema schema(URI uri, Function<URI, Json> relativeReferenceResolver) {
        try {
            return new DefaultSchema(uri, Json.read(Json.fetchContent(uri.toURL())), relativeReferenceResolver);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Schema schema(Json S, URI uri) {
        return new DefaultSchema(uri, S, null);
    }

	public static final Factory defaultFactory = new DefaultFactory();

    private static Factory globalFactory = defaultFactory;

    private static final ThreadLocal<Factory> threadFactory = new ThreadLocal<Factory>();

    public static Factory factory() {
        Factory f = threadFactory.get();
        return f != null ? f : globalFactory;
    }

    public static void setGlobalFactory(Factory factory) {
        globalFactory = factory;
    }

    public static void attachFactory(Factory factory) {
        threadFactory.set(factory);
    }

    public static void detachFactory() {
        threadFactory.remove();
    }

    public static Json read(String jsonAsString) {
        return (Json) new Reader().read(jsonAsString);
    }

    public static Json read(URL location) {
        return (Json) new Reader().read(fetchContent(location));
    }

    public static Json read(CharacterIterator it) {
        return (Json) new Reader().read(it);
    }

    public static Json nil() {
        return factory().nil();
    }

    public static Json object() {
        return factory().object();
    }

    public static Json object(Object... args) {
        Json j = object();
		if (args.length % 2 != 0) {
			throw new IllegalArgumentException("An even number of arguments is expected.");
		}
		for (int i = 0; i < args.length; i++) {
			j.set(args[i].toString(), factory().make(args[++i]));
		}
        return j;
    }

    public static Json array() {
        return factory().array();
    }

    public static Json array(Object... args) {
        Json A = array();
		for (Object x : args) {
			A.add(factory().make(x));
		}
        return A;
    }

    public static Json make(Object anything) {
        return factory().make(anything);
    }

    Json enclosing = null;

    protected Json() {
    }

    protected Json(Json enclosing) {
        this.enclosing = enclosing;
    }

    public String toString(int maxCharacters) {
        return toString();
    }

    public void attachTo(Json enclosing) {
        this.enclosing = enclosing;
    }

    public final Json up() {
        return enclosing;
    }

    public Json dup() {
        return this;
    }

    public Json at(int index) {
        throw new UnsupportedOperationException();
    }

    public Json at(String property) {
        throw new UnsupportedOperationException();
    }

    public final Json at(String property, Json def) {
        Json x = at(property);
		if (x == null) {
			return def;
		} else {
			return x;
		}
    }

    public final Json at(String property, Object def) {
        return at(property, make(def));
    }

    public boolean has(String property) {
        throw new UnsupportedOperationException();
    }

    public boolean is(String property, Object value) {
        throw new UnsupportedOperationException();
    }

    public boolean is(int index, Object value) {
        throw new UnsupportedOperationException();
    }

    public Json add(Json el) {
        throw new UnsupportedOperationException();
    }

    public final Json add(Object anything) {
        return add(make(anything));
    }

    public Json atDel(String property) {
        throw new UnsupportedOperationException();
    }

    public Json atDel(int index) {
        throw new UnsupportedOperationException();
    }

    public Json delAt(String property) {
        throw new UnsupportedOperationException();
    }

    public Json delAt(int index) {
        throw new UnsupportedOperationException();
    }

    public Json remove(Json el) {
        throw new UnsupportedOperationException();
    }

    public final Json remove(Object anything) {
        return remove(make(anything));
    }

    public Json set(String property, Json value) {
        throw new UnsupportedOperationException();
    }

    public final Json set(String property, Object value) {
        return set(property, make(value));
    }

    public Json set(int index, Object value) {
        throw new UnsupportedOperationException();
    }

    public Json with(Json object, Json[] options) {
        throw new UnsupportedOperationException();
    }

    public Json with(Json object, Object... options) {
        Json[] jopts = new Json[options.length];
		for (int i = 0; i < jopts.length; i++) {
			jopts[i] = make(options[i]);
		}
        return with(object, jopts);
    }

    public Object getValue() {
        throw new UnsupportedOperationException();
    }

    public boolean asBoolean() {
        throw new UnsupportedOperationException();
    }

    public String asString() {
        throw new UnsupportedOperationException();
    }

    public int asInteger() {
        throw new UnsupportedOperationException();
    }

    public float asFloat() {
        throw new UnsupportedOperationException();
    }

    public double asDouble() {
        throw new UnsupportedOperationException();
    }

    public long asLong() {
        throw new UnsupportedOperationException();
    }

    public short asShort() {
        throw new UnsupportedOperationException();
    }

    public byte asByte() {
        throw new UnsupportedOperationException();
    }

    public char asChar() {
        throw new UnsupportedOperationException();
    }

    public Map<String, Object> asMap() {
        throw new UnsupportedOperationException();
    }

    public Map<String, Json> asJsonMap() {
        throw new UnsupportedOperationException();
    }

    public List<Object> asList() {
        throw new UnsupportedOperationException();
    }

    public List<Json> asJsonList() {
        throw new UnsupportedOperationException();
    }

    public boolean isNull() {
        return false;
    }
    public boolean isString() {
        return false;
    }

    public boolean isNumber() {
        return false;
    }

    public boolean isBoolean() {
        return false;
    }

    public boolean isArray() {
        return false;
    }

    public boolean isObject() {
        return false;
    }

    public boolean isPrimitive() {
        return isString() || isNumber() || isBoolean();
    }

    public String pad(String callback) {
        return (callback != null && callback.length() > 0)
                ? callback + "(" + this + ");"
                : toString();
    }

    protected Json collectWithOptions(Json... options) {
        Json result = object();
        for (Json opt : options) {
            if (opt.isString()) {
				if (!result.has("")) {
					result.set("", object());
				}
                result.at("").set(opt.asString(), true);
            } else {
				if (!opt.has("for")) {
					opt.set("for", array(""));
				}
                Json forPaths = opt.at("for");
				if (!forPaths.isArray()) {
					forPaths = array(forPaths);
				}
                for (Json path : forPaths.asJsonList()) {
					if (!result.has(path.asString())) {
						result.set(path.asString(), object());
					}
                    Json at_path = result.at(path.asString());
                    at_path.set("merge", opt.is("merge", true));
                    at_path.set("dup", opt.is("dup", true));
                    at_path.set("sort", opt.is("sort", true));
                    at_path.set("compareBy", opt.at("compareBy", nil()));
                }
            }
        }
        return result;
    }

    static void setParent(Json el, Json parent) {
		if (el.enclosing == null) {
			el.enclosing = parent;
		} else if (el.enclosing instanceof ParentArrayJson) {
			((ParentArrayJson) el.enclosing).L.add(parent);
		} else {
			ParentArrayJson A = new ParentArrayJson();
			A.L.add(el.enclosing);
			A.L.add(parent);
			el.enclosing = A;
		}
    }

    static void removeParent(Json el, Json parent) {
		if (el.enclosing == parent) {
			el.enclosing = null;
		} else if (el.enclosing.isArray()) {
			ArrayJson A = (ArrayJson) el.enclosing;
			int idx = 0;
			while (A.L.get(idx) != parent && idx < A.L.size()) {
				idx++;
			}
			if (idx < A.L.size()) {
				A.L.remove(idx);
			}
		}
    }

	protected static final Escaper escaper = new Escaper(false);

}
