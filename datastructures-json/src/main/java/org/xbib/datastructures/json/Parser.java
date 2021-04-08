package org.xbib.datastructures.json;

import org.xbib.datastructures.json.model.HashNode;
import org.xbib.datastructures.json.model.ListNode;
import org.xbib.datastructures.json.model.Node;
import org.xbib.datastructures.json.model.ValueNode;
import org.xbib.datastructures.json.token.Token;
import org.xbib.datastructures.json.token.TokenType;
import java.io.IOException;
import java.io.Reader;

public class Parser {

    private final Lexer lexer;

    private Node root;

    private Token token;

    public Parser(Reader reader) {
        this.lexer = new Lexer(reader);
    }

    public void parse() throws IOException {
        root = parseNode(null);
    }

    public Node getNode() {
        return root;
    }

    private Node parseNode(Node parent) throws IOException {
        if (token == null) {
            token = lexer.next();
            if (token == null) {
                return null;
            }
        }
        switch (token.getType()) {
            case BEGIN_ARRAY:
                return parseListNode(parent);
            case BEGIN_OBJECT:
                return parseHashNode(parent);
            case VALUE:
                return parseValueNode(parent);
            default:
                return null;
        }
    }

    private ListNode parseListNode(Node parent) throws IOException {
        ListNode node = new ListNode(parent);
        while (token != null && token.getType() == TokenType.BEGIN_ARRAY) {
            token = lexer.next();
            if (token != null) {
                node.addItem(parseNode(node));
            }
        }
        return node;
    }

    private HashNode parseHashNode(Node parent) throws IOException {
        HashNode node = new HashNode(parent);
        while (token != null && token.getType() == TokenType.BEGIN_OBJECT) {
            String key = token.getValue();
            token = lexer.next();
            if (token != null) {
                node.putChild(key, parseNode(node));
            }
        }
        return node;
    }

    private ValueNode parseValueNode(Node parent) throws IOException {
        ValueNode node = new ValueNode(parent);
        if (token.getType() == TokenType.VALUE) {
            node.setType(ValueNode.TextType.LINE);
            node.setValue(token.getValue());
            token = lexer.next();
        } else {
            node.setValue("");
        }
        return node;
    }
}
