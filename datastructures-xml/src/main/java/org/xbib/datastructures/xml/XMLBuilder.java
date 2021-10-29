package org.xbib.datastructures.xml;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class XMLBuilder {

    public static final String XMLNS = "xmlns";

    public static String XML_HEADER() {
        return "<?xml version=\"1.0\"?>";
    }

    public static String XML_HEADER(String encoding) {
        return "<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>";
    }

    private static final int STATE_NOTHING = 0;
    private static final int STATE_ELEM_OPENED = 1;
    private static final int STATE_TEXT_ADDED = 2;
    private static final int IO_BUFFER_SIZE = 8192;

    private Writer writer;

    private int state = STATE_NOTHING;

    private XMLElement element = null;

    private final List<XMLElement> trashElements = new ArrayList<>();

    public XMLBuilder(OutputStream stream, String documentEncoding) throws IOException {
        this(stream, documentEncoding, true);
    }

    public XMLBuilder(OutputStream stream, String documentEncoding, boolean printHeader) throws IOException {
        if (documentEncoding == null) {
            init(new OutputStreamWriter(stream), null, printHeader);
        } else {
            init(new OutputStreamWriter(stream, documentEncoding), documentEncoding, printHeader);
        }
    }

    public XMLBuilder(Writer writer, String documentEncoding) throws IOException {
        this(writer, documentEncoding, true);
    }

    public XMLBuilder(Writer writer, String documentEncoding, boolean printHeader) throws IOException {
        init(writer, documentEncoding, printHeader);
    }

    private XMLElement createElement(XMLElement parent, String name) {
        if (trashElements.isEmpty()) {
            return new XMLElement(parent, name);
        } else {
            XMLElement element = trashElements.remove(trashElements.size() - 1);
            element.init(parent, name);
            return element;
        }
    }

    private void deleteElement(XMLElement element) {
        trashElements.add(element);
    }

    private void init(Writer writer, String documentEncoding, boolean printHeader) throws IOException {
        writer = new BufferedWriter(writer, IO_BUFFER_SIZE);
        if (printHeader) {
            if (documentEncoding != null) {
                writer.write(XML_HEADER(documentEncoding));
            } else {
                writer.write(XML_HEADER());
            }
        }
    }

    public XMLElement startElement(String elementName) throws IOException {
        return startElement(null, null, elementName);
    }

    public XMLElement startElement(String nsURI, String elementName) throws IOException {
        return startElement(nsURI, null, elementName);
    }

    public XMLElement startElement(String nsURI, String nsPrefix, String elementName) throws IOException {
        switch (state) {
            case STATE_ELEM_OPENED:
                writer.write('>');
            case STATE_NOTHING:
                break;
            default:
                break;
        }
        writer.write('<');
        boolean addNamespace = (nsURI != null);
        if (nsURI != null) {
            if (nsPrefix == null && element != null) {
                nsPrefix = element.getNamespacePrefix(nsURI);
                if (nsPrefix != null) {
                    addNamespace = false;
                }
            }
        }
        if (nsPrefix != null) {
            elementName = nsPrefix + ':' + elementName;
        }
        writer.write(elementName);
        state = STATE_ELEM_OPENED;
        element = createElement(element, elementName);
        if (addNamespace) {
            addNamespace(nsURI, nsPrefix);
            element.addNamespace(nsURI, nsPrefix);
        }

        return element;
    }

    public XMLBuilder endElement() throws IOException, IllegalStateException {
        if (element == null) {
            throw new IllegalStateException("Close tag without open");
        }
        switch (state) {
            case STATE_ELEM_OPENED:
                writer.write("/>");
                break;
            case STATE_NOTHING:
            case STATE_TEXT_ADDED:
                writer.write("</");
                writer.write(element.getName());
                writer.write('>');
            default:
                break;
        }
        deleteElement(element);
        element = element.getParent();
        state = STATE_NOTHING;
        return this;
    }

    public XMLBuilder addNamespace(String nsURI) throws IOException {
        return addNamespace(nsURI, null);
    }

    public XMLBuilder addNamespace(String nsURI, String nsPrefix) throws IOException, IllegalStateException {
        if (element == null) {
            throw new IllegalStateException("Namespace outside of element");
        }
        String attrName = XMLNS;
        if (nsPrefix != null) {
            attrName = attrName + ':' + nsPrefix;
            element.addNamespace(nsURI, nsPrefix);
        }
        addAttribute(null, attrName, nsURI, true);
        return this;
    }

    public XMLBuilder addAttribute(String attributeName, String attributeValue) throws IOException {
        return addAttribute(null, attributeName, attributeValue, true);
    }

    public XMLBuilder addAttribute(String nsURI, String attributeName, String attributeValue) throws IOException {
        return addAttribute(nsURI, attributeName, attributeValue, true);
    }

    private XMLBuilder addAttribute(String nsURI, String attributeName, String attributeValue, boolean escape) throws IOException, IllegalStateException {
        switch (state) {
            case STATE_ELEM_OPENED: {
                if (nsURI != null) {
                    String nsPrefix = element.getNamespacePrefix(nsURI);
                    if (nsPrefix == null) {
                        throw new IllegalStateException("Unknown attribute '" + attributeName + "' namespace URI '" + nsURI + "' in element '" + element.getName() + "'");
                    }
                    attributeName = nsPrefix + ':' + attributeName;
                }
                writer.write(' ');
                writer.write(attributeName);
                writer.write("=\"");
                writer.write(escape ? XMLUtils.escapeXml(attributeValue) : attributeValue);
                writer.write('"');
                break;
            }
            case STATE_TEXT_ADDED:
            case STATE_NOTHING:
                throw new IllegalStateException("Attribute outside of element");
            default:
                break;
        }
        return this;
    }

    public XMLBuilder addText(CharSequence textValue) throws IOException {
        return addText(textValue, true);
    }

    public XMLBuilder addText(CharSequence textValue, boolean escape) throws IOException {
        switch (state) {
            case STATE_ELEM_OPENED:
                writer.write('>');
            case STATE_TEXT_ADDED:
            case STATE_NOTHING:
                break;
            default:
                break;
        }
        writeText(textValue, escape);

        state = STATE_TEXT_ADDED;

        return this;
    }

    public XMLBuilder addTextData(String text) throws IOException {
        switch (state) {
            case STATE_ELEM_OPENED:
                writer.write('>');
            case STATE_TEXT_ADDED:
            case STATE_NOTHING:
                break;
            default:
                break;
        }
        writer.write("<![CDATA[");
        writer.write(text);
        writer.write("]]>");
        state = STATE_TEXT_ADDED;
        return this;
    }

    public XMLBuilder addContent(CharSequence textValue) throws IOException {
        writer.write(textValue.toString());
        return this;
    }

    public XMLBuilder addComment(String commentValue) throws IOException {
        switch (state) {
            case STATE_ELEM_OPENED:
                writer.write('>');
            case STATE_NOTHING:
                break;
            default:
                break;
        }
        writer.write("<!--");
        writer.write(commentValue);
        writer.write("-->");
        state = STATE_TEXT_ADDED;
        return this;
    }

    public XMLBuilder addElement(String elementName, String elementValue) throws IOException {
        startElement(elementName);
        addText(elementValue);
        endElement();
        return this;
    }

    public XMLBuilder addElementText(String elementName, String elementValue) throws IOException {
        startElement(elementName);
        addTextData(elementValue);
        endElement();
        return this;
    }

    private XMLBuilder writeText(CharSequence textValue, boolean escape) throws IOException {
        if (textValue != null) {
            writer.write(escape ? XMLUtils.escapeXml(textValue) : textValue.toString());
        }
        return this;
    }

}
