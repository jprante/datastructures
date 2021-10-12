package org.xbib.datastructures.yaml.tiny;

import org.xbib.datastructures.api.Node;
import org.xbib.datastructures.api.Parser;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static org.xbib.datastructures.yaml.tiny.TokenType.ITEM;

public class YamlParser implements Parser {

    private final List<Token> comments;

    private Lexer lexer;

    private Token token;

    public YamlParser() {
        comments = new ArrayList<>();
    }

    public Node<?> parse(Reader reader) throws IOException {
        lexer = new Lexer(reader);
        return parseNode(null, -1);
    }

    private Node<?> parseNode(Node<?> parent, int depth) throws IOException {
        if (token == null) {
            token = lexer.next();
            if (token == null) {
                return null;
            }
        }
        if (token.getDepth() < depth) {
            return new ValueNode(parent);
        }
        switch (token.getType()) {
            case DOCUMENT_START:
            case DOCUMENT_END:
                token = lexer.next();
                return parseNode(parent, depth);
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

    private ListNode parseListNode(Node<?> parent, int indent) throws IOException {
        ListNode node = new ListNode(parent);
        int cnt = 0;
        while (token != null && token.getDepth() == indent && token.getType() == ITEM) {
            token = lexer.next();
            if (token != null) {
                node.addComments(cnt, collectComments(indent));
                node.add(parseNode(node, indent));
            }
            stashComments();
            cnt++;
        }
        return node;
    }

    private MapNode parseHashNode(Node<?> parent, int indent) throws IOException {
        MapNode node = new MapNode(parent);
        while (token != null && token.getDepth() == indent && token.getType() == TokenType.KEY) {
            String key = token.getValue();
            token = lexer.next();
            if (token != null) {
                node.addComments(key, collectComments(indent));
                node.put(key, parseNode(node, indent));
            }
            stashComments();
        }
        return node;
    }

    private ValueNode parseValueNode(Node<?> parent, int indent) throws IOException {
        ValueNode node = new ValueNode(parent);
        node.addComments(collectComments(indent));
        switch (token.getType()) {
            case VALUE_LINE:
                node.setType(ValueNode.TextType.LINE);
                node.set(token.getValue());
                token = lexer.next();
                break;
            case VALUE_MULTILINE:
                node.setType(ValueNode.TextType.MULTILINE);
                node.set(token.getValue());
                token = lexer.next();
                break;
            case VALUE_TEXT_PIPE:
                node.setType(ValueNode.TextType.TEXT);
                node.set(token.getValue());
                token = lexer.next();
                break;
            case VALUE_TEXT_ANGLE:
                node.setType(ValueNode.TextType.TEXT_ANGLE);
                node.set(token.getValue());
                token = lexer.next();
                break;
            default:
                node.set("");
                break;
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
