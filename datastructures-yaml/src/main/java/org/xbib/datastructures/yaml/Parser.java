package org.xbib.datastructures.yaml;

import org.xbib.datastructures.yaml.model.HashNode;
import org.xbib.datastructures.yaml.model.ListNode;
import org.xbib.datastructures.yaml.model.Node;
import org.xbib.datastructures.yaml.model.ValueNode;
import org.xbib.datastructures.yaml.model.ValueNode.TextType;
import org.xbib.datastructures.yaml.token.Token;
import org.xbib.datastructures.yaml.token.TokenType;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Parser {

    private final Lexer lexer;

    private final List<Token> comments;

    private Node root;

    private Token token;

    public Parser(Reader reader) {
        lexer = new Lexer(reader);
        comments = new ArrayList<>();
    }

    public void parse() throws IOException {
        root = parseNode(null, -1);
    }

    public Node getNode() {
        return root;
    }

    private Node parseNode(Node parent, int depth) throws IOException {
        if (token == null) {
            token = lexer.next();
            if (token == null) {
                return null;
            }
        }
        if (token.getDepth() <= depth) {
            return new ValueNode(parent);
        }
        switch (token.getType()) {
            case COMMENT:
                stashComments();
                return parseNode(parent, depth);
            case ITEM:
                return parseListNode(parent, token.getDepth());
            case KEY:
                return parseHashNode(parent, token.getDepth());
            case VALUE:
            case VALUE_LINE:
            case VALUE_MULTILINE:
            case VALUE_TEXT_PIPE:
            case VALUE_TEXT_ANGLE:
                return parseValueNode(parent, token.getDepth());
            default:
                return null;
        }
    }

    private ListNode parseListNode(Node parent, int indent) throws IOException {
        ListNode node = new ListNode(parent);
        int cnt = 0;
        while (token != null && token.getDepth() == indent && token.getType() == TokenType.ITEM) {
            token = lexer.next();
            if (token != null) {
                node.addComments(cnt, collectComments(indent));
                node.addItem(parseNode(node, indent));
            }
            stashComments();
            cnt++;
        }
        return node;
    }

    private HashNode parseHashNode(Node parent, int indent) throws IOException {
        HashNode node = new HashNode(parent);
        while (token != null && token.getDepth() == indent && token.getType() == TokenType.KEY) {
            String key = token.getValue();
            token = lexer.next();
            if (token != null) {
                node.addComments(key, collectComments(indent));
                node.putChild(key, parseNode(node, indent));
            }
            stashComments();
        }
        return node;
    }

    private ValueNode parseValueNode(Node parent, int indent) throws IOException {
        ValueNode node = new ValueNode(parent);
        node.addComments(collectComments(indent));
        switch (token.getType()) {
            case VALUE_LINE:
                node.setType(TextType.LINE);
                node.setValue(token.getValue());
                token = lexer.next();
                break;
            case VALUE_MULTILINE:
                node.setType(TextType.MULTILINE);
                node.setValue(token.getValue());
                token = lexer.next();
                break;
            case VALUE_TEXT_PIPE:
                node.setType(TextType.TEXT);
                node.setValue(token.getValue());
                token = lexer.next();
                break;
            case VALUE_TEXT_ANGLE:
                node.setType(TextType.TEXT_ANGLE);
                node.setValue(token.getValue());
                token = lexer.next();
                break;
            default:
                node.setValue("");
        }
        return node;
    }

    private List<String> collectComments(int indent) {
        ListIterator<Token> iter = comments.listIterator(comments.size());
        while (iter.hasPrevious()) {
            Token cmttok = iter.previous();
            if (cmttok.getDepth() != indent) {
                break;
            }
        }
        List<String> res = new ArrayList<>();
        while (iter.hasNext()) {
            Token cmttok = iter.next();
            res.add(cmttok.getValue());
        }
        comments.clear();
        return res;
    }

    private void stashComments() throws IOException {
        while (token != null && token.getType() == TokenType.COMMENT) {
            comments.add(token);
            token = lexer.next();
        }
    }
}
