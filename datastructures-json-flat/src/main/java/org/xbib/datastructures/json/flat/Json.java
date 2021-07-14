package org.xbib.datastructures.json.flat;

public class Json {

    public static final String DEFAULT_INDENT = "  ";

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
        switch (type) {
            case NULL:
                return new NullLiteral();
            case TRUE:
            case FALSE:
                return new BoolLiteral(Boolean.parseBoolean(parser.getJson(element)));
            case NUMBER:
                return new NumberLiteral(parser.getJson(element));
            case STRING_ESCAPED:
            case STRING:
                return new ParsedString(parser, element);
            case ARRAY:
                return new ParsedList(parser, element);
            case OBJECT:
                return new ParsedMap(parser, element);
            default:
                throw new IllegalStateException();
        }
    }
}
