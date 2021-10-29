package org.xbib.datastructures.xml;

import java.util.HashMap;
import java.util.Map;

public final class XMLElement {
    public static final String NS_XML = "http://www.w3.org/TR/REC-xml";
    public static final String PREFIX_XML = "xml";

    private XMLElement parent;
    private String name;
    private Map<String, String> namespaces = null;
    private int level;

    XMLElement(XMLElement parent, String name) {
        this.init(parent, name);
    }

    void init(XMLElement parent, String name) {
        this.parent = parent;
        this.name = name;
        this.namespaces = null;
        this.level = parent == null ? 0 : parent.level + 1;
    }

    public String getName() {
        return name;
    }

    public XMLElement getParent() {
        return parent;
    }

    public int getLevel() {
        return level;
    }

    public void addNamespace(String nsURI, String nsPrefix) {
        if (namespaces == null) {
            namespaces = new HashMap<>();
        }
        namespaces.put(nsURI, nsPrefix);
    }

    public String getNamespacePrefix(String nsURI) {
        if (nsURI.equals(NS_XML)) {
            return PREFIX_XML;
        }
        String prefix = namespaces == null ? null : namespaces.get(nsURI);
        return prefix != null ? prefix : (parent != null ? parent.getNamespacePrefix(nsURI) : null);
    }
}
