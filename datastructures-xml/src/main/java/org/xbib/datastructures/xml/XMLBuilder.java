package org.xbib.datastructures.xml;

import org.xbib.datastructures.api.Builder;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class XMLBuilder implements Builder {

    private static final String XMLNS = "xmlns";

    private static final String PREFIX_XML = "xml";

    private static final String NS_XML = "http://www.w3.org/TR/REC-xml";

    private final Map<String, String> namespaces;

    private final Appendable appendable;

    private State state;

    private XMLBuilder(Appendable appendable) throws IOException {
        this(appendable, "1.0", "UTF-8");
    }

    public XMLBuilder(Appendable appendable, String version, String encoding) throws IOException {
        this.appendable = appendable;
        if (version != null && encoding != null) {
           appendable.append("<?xml version=\"").append(version).append("\" encoding=\"").append(encoding).append("\"?>");
        }
        this.state = new State(null, "root", 0, Structure.DOCSTART, Xml.ELEMENT_CLOSED);
        this.namespaces = new HashMap<>();
    }

    public static XMLBuilder builder() throws IOException {
        return new XMLBuilder(new StringBuilder(), null, null);
    }

    public static XMLBuilder builder(Appendable appendable) throws IOException {
        return new XMLBuilder(appendable);
    }

    public static XMLBuilder builder(Appendable appendable, String version, String encoding) throws IOException {
        return new XMLBuilder(appendable, version, encoding);
    }

    public XMLBuilder startElement(CharSequence elementName) throws IOException, XMLException {
        return startElement(null, null, elementName);
    }

    public XMLBuilder startElement(String nsURI, CharSequence elementName) throws IOException, XMLException {
        return startElement(nsURI, null, elementName);
    }

    public XMLBuilder startElement(String nsURI, String nsPrefix, CharSequence elementName) throws IOException, XMLException {
        if (state.xml == Xml.ELEMENT_OPENED) {
            appendable.append('>');
        }
        appendable.append('<');
        boolean add = nsURI != null;
        if (nsURI != null) {
            if (nsPrefix == null) {
                nsPrefix = getNamespacePrefix(nsURI);
                if (nsPrefix != null) {
                    add = false;
                }
            }
        }
        if (nsPrefix != null) {
            elementName = nsPrefix + ':' + elementName;
        }
        appendable.append(elementName);
        state = new State(state, elementName, state.level + 1, Structure.NONE, Xml.ELEMENT_OPENED);
        if (add) {
            addNamespace(nsURI, nsPrefix);
        }
        return this;
    }

    public XMLBuilder endElement() throws IOException, XMLException {
        if (state.xml != Xml.ELEMENT_OPENED && state.xml != Xml.ELEMENT_CLOSED && state.xml != Xml.TEXT_ADDED) {
            throw new XMLException("close tag without open");
        }
        switch (state.xml) {
            case ELEMENT_OPENED:
                appendable.append("/>");
                break;
            case ELEMENT_CLOSED:
            case TEXT_ADDED:
                appendable.append("</").append(state.name).append('>');
            default:
                break;
        }
        state = state.parent;
        state.xml = Xml.ELEMENT_CLOSED;
        return this;
    }

    public XMLBuilder addNamespace(String nsURI) throws IOException, XMLException {
        return addNamespace(nsURI, null);
    }

    public XMLBuilder addNamespace(String nsURI, String nsPrefix) throws IOException, XMLException {
        if (state.xml != Xml.ELEMENT_OPENED) {
            throw new XMLException("namespace outside of element");
        }
        String attrName = XMLNS;
        if (nsPrefix != null) {
            attrName = attrName + ':' + nsPrefix;
            namespaces.put(nsURI, nsPrefix);
        }
        addAttribute(null, attrName, nsURI);
        return this;
    }

    public XMLBuilder addAttribute(String attributeName, String attributeValue) throws IOException, XMLException {
        return addAttribute(null, attributeName, attributeValue);
    }

    public XMLBuilder addAttribute(String nsURI, String attributeName, String attributeValue) throws IOException, XMLException {
        switch (state.xml) {
            case ELEMENT_OPENED: {
                if (nsURI != null) {
                    String nsPrefix = getNamespacePrefix(nsURI);
                    if (nsPrefix == null) {
                        throw new XMLException("unknown attribute '" + attributeName + "' namespace URI '" + nsURI + "' in element '" + state.name + "'");
                    }
                    attributeName = nsPrefix + ':' + attributeName;
                }
               appendable.append(' ').append(attributeName).append("=\"").append(XMLUtils.escapeXml(attributeValue)).append('"');
                break;
            }
            case TEXT_ADDED:
                throw new XMLException("attribute outside of element");
            default:
                break;
        }
        return this;
    }

    public XMLBuilder addText(CharSequence textValue) throws IOException {
        return addText(textValue, true);
    }

    public XMLBuilder addText(CharSequence textValue, boolean escape) throws IOException {
        if (state.xml == Xml.ELEMENT_OPENED) {
            appendable.append('>');
        }
        if (textValue != null) {
            appendable.append(escape ? XMLUtils.escapeXml(textValue) : textValue.toString());
        }
        state.xml = Xml.TEXT_ADDED;
        return this;
    }

    public XMLBuilder addTextData(CharSequence text) throws IOException {
        if (state.xml == Xml.ELEMENT_OPENED) {
            appendable.append('>');
        }
        appendable.append("<![CDATA[").append(text).append("]]>");
        state.xml = Xml.TEXT_ADDED;
        return this;
    }

    public XMLBuilder addComment(CharSequence commentValue) throws IOException {
        if (state.xml == Xml.ELEMENT_OPENED) {
            appendable.append('>');
        }
        appendable.append("<!--").append(commentValue).append("-->");
        state.xml = Xml.TEXT_ADDED;
        return this;
    }

    public String getNamespacePrefix(String nsURI) {
        if (nsURI.equals(NS_XML)) {
            return PREFIX_XML;
        }
        return namespaces == null ? null : namespaces.get(nsURI);
    }

    @Override
    public Builder beginCollection() {
        state.structure = Structure.COLLECTION;
        return this;
    }

    @Override
    public Builder endCollection() {
        return this;
    }

    @Override
    public Builder beginMap() throws IOException {
        try {
            startElement(state.name);
            state.structure = Structure.MAP;
        } catch (XMLException e) {
            throw new IOException(e);
        }
        return this;
    }

    @Override
    public Builder endMap() throws IOException {
        if (state.structure == Structure.MAP ||
                state.structure == Structure.KEY ||
                state.structure == Structure.DOCSTART) {
            try {
                endElement();
            } catch (XMLException e) {
                throw new IOException(e);
            }
        }
        return this;
    }

    @Override
    public Builder buildMap(Map<String, Object> map) throws IOException {
        Objects.requireNonNull(map);
        boolean wrap = state.structure != Structure.MAP;
        if (wrap) {
            beginMap();
        }
        map.forEach((k, v) -> {
            try {
                buildKey(k);
                buildValue(v);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
        if (wrap) {
            endMap();
        }
        return this;
    }

    @Override
    public Builder buildCollection(Collection<?> collection) throws IOException {
        Objects.requireNonNull(collection);
        beginCollection();
        collection.forEach(v -> {
            try {
                startElement(state.name);
                buildValue(v);
                endElement();
            } catch (XMLException e) {
                throw new UncheckedIOException(new IOException(e));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
        endCollection();
        return this;
    }

    @Override
    public Builder buildKey(CharSequence key) throws IOException {
        if (state.structure == Structure.MAP || state.structure == Structure.KEY) {
            try {
                startElement(key);
            } catch (XMLException e) {
                throw new IOException(e);
            }
        }
        state.structure = Structure.KEY;
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Builder buildValue(Object value) throws IOException {
        if (value instanceof Map) {
            buildMap((Map<String, Object>) value);
        } else if (value instanceof Collection) {
            buildCollection((Collection<?>) value);
        } else if (value == null) {
            buildNull();
        } else {
            addText(value.toString(), true);
            if (state.structure == Structure.KEY) {
                try {
                    endElement();
                } catch (XMLException e) {
                    throw new IOException(e);
                }
            }
        }
        return this;
    }

    @Override
    public Builder buildNull() throws IOException {
        addText("null", false);
        return this;
    }

    @Override
    public Builder copy(Builder builder) throws IOException {
        appendable.append(builder.build());
        return this;
    }

    @Override
    public String build() {
        return appendable.toString();
    }

    private enum Structure { DOCSTART, NONE, MAP, KEY, COLLECTION };

    private enum Xml { ELEMENT_OPENED, ELEMENT_CLOSED, TEXT_ADDED };

    private static class State {
        State parent;
        CharSequence name;
        int level;
        Structure structure;
        Xml xml;

        State(State parent, CharSequence name, int level, Structure structure, Xml xml) {
            this.parent = parent;
            this.name = name;
            this.level = level;
            this.structure = structure;
            this.xml = xml;
        }
    }
}
