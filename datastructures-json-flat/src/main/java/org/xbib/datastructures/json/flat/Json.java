package org.xbib.datastructures.json.flat;

public class Json {

    public static final String DEFAULT_INDENT = "  ";

    public Json() {
    }

    public static Node parse(String raw) throws ParseException {
        return create(new Parser(raw), 0);
    }

    public String prettyPrint(Node node) {
        return prettyPrint(node, DEFAULT_INDENT);
    }

    public String prettyPrint(Node node, String indent) {
        PrettyPrinter prettyPrinter = new PrettyPrinter(indent);
        node.accept(prettyPrinter);
        return prettyPrinter.toString();
    }

    public static Node create(Parser parser, int element) {
        Type type = parser.getType(element);
        return switch (type) {
            case NULL -> new NullLiteral();
            case TRUE, FALSE -> new BoolLiteral(Boolean.parseBoolean(parser.getJson(element)));
            case NUMBER -> new NumberLiteral(parser.getJson(element));
            case STRING_ESCAPED, STRING -> new ParsedString(parser, element);
            case ARRAY -> new ParsedList(parser, element);
            case OBJECT -> new ParsedMap(parser, element);
        };
    }
}
