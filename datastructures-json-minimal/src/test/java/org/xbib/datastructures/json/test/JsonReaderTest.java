package org.xbib.datastructures.json.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.xbib.datastructures.json.Json.parse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xbib.datastructures.json.DefaultHandler;
import org.xbib.datastructures.json.JsonArray;
import org.xbib.datastructures.json.JsonBool;
import org.xbib.datastructures.json.JsonException;
import org.xbib.datastructures.json.JsonHandler;
import org.xbib.datastructures.json.JsonLiteral;
import org.xbib.datastructures.json.JsonNull;
import org.xbib.datastructures.json.JsonNumber;
import org.xbib.datastructures.json.JsonObject;
import org.xbib.datastructures.json.JsonReader;
import org.xbib.datastructures.json.JsonString;
import org.xbib.datastructures.json.JsonValue;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class JsonReaderTest {

    @Test
    public void constructorrejectsNullHandler() {
        Assertions.assertThrows(NullPointerException.class, () -> new JsonReader(null, null));
    }

    @Test
    public void parsestringrejectsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            JsonReader reader = new JsonReader(new StringReader(null), new TestHandler());
            reader.parse();
        });
    }

    @Test
    public void parsereaderrejectsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            JsonReader reader = new JsonReader(null, new TestHandler());
            reader.parse();
        });
    }

    @Test
    public void parsereaderrejectsNegativeBufferSize() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            JsonReader reader = new JsonReader(new StringReader("[]"), new TestHandler());
            reader.parse(-1);
        });
    }

    @Test
    public void parsestringrejectsEmpty() {
        assertParseException(0, "Unexpected end of input", "");
    }

    @Test
    public void parsereaderrejectsEmpty() {
        JsonReader reader = new JsonReader(new StringReader(""), new TestHandler());
        Assertions.assertThrows(JsonException.class, reader::parse);
    }

    @Test
    public void parsenull() throws IOException {
        TestHandler handler = new TestHandler();
        JsonReader reader = new JsonReader(new StringReader("null"), handler);
        reader.parse();
        assertEquals("null", handler.getLog());
    }

    @Test
    public void parsetrue() throws IOException {
        TestHandler handler = new TestHandler();
        JsonReader reader = new JsonReader(new StringReader("true"), handler);
        reader.parse();
        assertEquals("bool true", handler.getLog());
    }

    @Test
    public void parsefalse() throws IOException {
        TestHandler handler = new TestHandler();
        JsonReader reader = new JsonReader(new StringReader("false"), handler);
        reader.parse();
        assertEquals("bool false", handler.getLog());
    }

    @Test
    public void parsestring() throws IOException {
        TestHandler handler = new TestHandler();
        JsonReader reader = new JsonReader(new StringReader("\"foo\""), handler);
        reader.parse();
        assertEquals("string foo", handler.getLog());
    }

    @Test
    public void parsestringempty() throws IOException {
        TestHandler handler = new TestHandler();
        JsonReader reader = new JsonReader(new StringReader("\"\""), handler);
        reader.parse();
        assertEquals("string ", handler.getLog());
    }

    @Test
    public void parsenumber() throws IOException {
        TestHandler handler = new TestHandler();
        JsonReader reader = new JsonReader(new StringReader("23"), handler);
        reader.parse();
        assertEquals("number 23", handler.getLog());
    }

    @Test
    public void parsenumbernegative() throws IOException {
        TestHandler handler = new TestHandler();
        JsonReader reader = new JsonReader(new StringReader("-23"), handler);
        reader.parse();
        assertEquals("number -23", handler.getLog());
    }

    @Test
    public void parsenumbernegativeexponent() throws IOException {
        TestHandler handler = new TestHandler();
        JsonReader reader = new JsonReader(new StringReader("-2.3e-12"), handler);
        reader.parse();
        assertEquals("number -2.3e-12", handler.getLog());
    }

    @Test
    public void parsearray() throws IOException {
        TestHandler handler = new TestHandler();
        JsonReader reader = new JsonReader(new StringReader("[23]"), handler);
        reader.parse();
        String s1 = String.join("\n", "startArray", "number 23", "endArray [23]");
        String s2 = handler.getLog();
        assertEquals(s1.length(), s2.length());
        assertEquals(s1, s2);
    }

    @Test
    public void parsearrayempty() throws IOException {
        TestHandler handler = new TestHandler();
        JsonReader reader = new JsonReader(new StringReader("[]"), handler);
        reader.parse();
        assertEquals(String.join("\n", "startArray", "endArray []"), handler.getLog());
    }

    @Test
    public void parseobject() throws IOException {
        TestHandler handler = new TestHandler();
        JsonReader reader = new JsonReader(new StringReader("{\"foo\": 23}"), handler);
        reader.parse();
        assertEquals(String.join("\n", "startObject",
                "objectName [{}, foo]",
                "number 23",
                "objectValue [{}, foo]",
                "endObject {}"),
                handler.getLog());
    }

    @Test
    public void parseobjectempty() throws IOException {
        TestHandler handler = new TestHandler();
        JsonReader reader = new JsonReader(new StringReader("{}"), handler);
        reader.parse();
        assertEquals("startObject\nendObject {}", handler.getLog());
    }

    @Test
    public void parsestripsPadding() throws IOException {
        assertEquals(new JsonArray(), parse(" [ ] "));
    }

    @Test
    public void parseignoresAllWhiteSpace() throws IOException {
        assertEquals(new JsonArray(), parse("\t\r\n [\t\r\n ]\t\r\n "));
    }

    @Test
    public void parsefailsWithUnterminatedString() {
        assertParseException(5, "Unexpected end of input", "[\"foo");
    }

    @Test
    public void parselineAndColumnonFirstLine() throws IOException {
        TestHandler handler = new TestHandler();
        JsonReader reader = new JsonReader(new StringReader("[]"), handler);
        reader.parse();
        //assertEquals("1:3", handler.lastLocation.toString());
    }

    @Test
    public void parselineAndColumnafterLF() throws IOException {
        TestHandler handler = new TestHandler();
        JsonReader reader = new JsonReader(new StringReader("[\n]"), handler);
        reader.parse();
        //assertEquals("2:2", handler.lastLocation.toString());
    }

    @Test
    public void parselineAndColumnafterCRLF() throws IOException {
        TestHandler handler = new TestHandler();
        JsonReader reader = new JsonReader(new StringReader("[\r\n]"), handler);
        reader.parse();
        //assertEquals("2:2", handler.lastLocation.toString());
    }

    @Test
    public void parselineAndColumnafterCR() throws IOException {
        TestHandler handler = new TestHandler();
        JsonReader reader = new JsonReader(new StringReader("[\r]"), handler);
        reader.parse();
        //assertEquals("1:4", handler.lastLocation.toString());
    }

    @Test
    public void parsehandlesInputsThatExceedBufferSize() throws IOException {
        String input = "[ 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47 ]";
        DefaultHandler defHandler = new DefaultHandler();
        JsonReader reader = new JsonReader(new StringReader(input), defHandler);
        reader.parse(3);
        assertEquals("[2,3,5,7,11,13,17,19,23,29,31,37,41,43,47]", defHandler.getValue().toString());
    }

    @Test
    public void parsehandlesStringsThatExceedBufferSize() throws IOException {
        String input = "[ \"lorem ipsum dolor sit amet\" ]";
        DefaultHandler defHandler = new DefaultHandler();
        JsonReader reader = new JsonReader(new StringReader(input), defHandler);
        reader.parse(3);
        assertEquals("[\"lorem ipsum dolor sit amet\"]", defHandler.getValue().toString());
    }

    @Test
    public void parsehandlesNumbersThatExceedBufferSize() throws IOException {
        String input = "[ 3.141592653589 ]";
        DefaultHandler defHandler = new DefaultHandler();
        JsonReader reader = new JsonReader(new StringReader(input), defHandler);
        reader.parse(3);
        assertEquals("[3.141592653589]", defHandler.getValue().toString());
    }

    @Test
    public void parsehandlesPositionsCorrectlyWhenInputExceedsBufferSize() {
        final String input = "{\n  \"a\": 23,\n  \"b\": 42,\n}";
        TestHandler handler = new TestHandler();
        JsonReader reader = new JsonReader(new StringReader(input), handler);
        Assertions.assertThrows(JsonException.class, () -> reader.parse(3));
    }

    @Test
    public void parsefailsOnTooDeeplyNestedArray() {
        JsonArray array = new JsonArray();
        for (int i = 0; i < 256; i++) {
            array = new JsonArray().add(array);
        }
        final String input = array.toString();
        TestHandler handler = new TestHandler();
        JsonReader reader = new JsonReader(new StringReader(input), handler);
        Assertions.assertThrows(JsonException.class, reader::parse);
    }

    @Test
    public void parsefailsOnTooDeeplyNestedObject() {
        JsonObject object = new JsonObject();
        for (int i = 0; i < 256; i++) {
            object = new JsonObject().add("foo", object);
        }
        final String input = object.toString();
        TestHandler handler = new TestHandler();
        JsonReader reader = new JsonReader(new StringReader(input), handler);
        Assertions.assertThrows(JsonException.class, reader::parse);
    }

    @Test
    public void parsefailsOnTooDeeplyNestedMixedObject() {
        JsonValue value = new JsonObject();
        for (int i = 0; i < 256; i++) {
            value = i % 2 == 0 ? new JsonArray().add(value) : new JsonObject().add("foo", value);
        }
        final String input = value.toString();
        TestHandler handler = new TestHandler();
        JsonReader reader = new JsonReader(new StringReader(input), handler);
        Assertions.assertThrows(JsonException.class, reader::parse);
    }

    @Test
    public void parsedoesNotFailWithManyArrays() throws IOException {
        JsonArray array = new JsonArray();
        for (int i = 0; i < 256; i++) {
            array.add(new JsonArray().add(7));
        }
        final String input = array.toString();
        JsonValue result = parse(input);
        assertTrue(result.isArray());
    }

    @Test
    public void parsedoesNotFailWithManyEmptyArrays() throws IOException {
        JsonArray array = new JsonArray();
        for (int i = 0; i < 1001; i++) {
            array.add(new JsonArray());
        }
        final String input = array.toString();
        JsonValue result = parse(input);
        assertTrue(result.isArray());
    }

    @Test
    public void parsedoesNotFailWithManyObjects() throws IOException {
        JsonArray array = new JsonArray();
        for (int i = 0; i < 1001; i++) {
            array.add(new JsonObject().add("a", 7));
        }
        final String input = array.toString();
        JsonValue result = parse(input);
        assertTrue(result.isArray());
    }

    @Test
    public void parsedoesNotFailWithManyEmptyObjects() throws IOException {
        JsonArray array = new JsonArray();
        for (int i = 0; i < 1001; i++) {
            array.add(new JsonObject());
        }
        final String input = array.toString();
        JsonValue result = parse(input);
        assertTrue(result.isArray());
    }

    @Test
    public void parsecanBeCalledOnce() throws IOException {
        TestHandler handler = new TestHandler();
        JsonReader reader = new JsonReader(new StringReader("[23]"), handler);
        reader.parse();
        assertEquals(String.join("\n",
                "startArray",
                "number 23",
                "endArray [23]"),
                handler.getLog());
        handler = new TestHandler();
        reader = new JsonReader(new StringReader("[42]"), handler);
        reader.parse();
        assertEquals(String.join("\n",
                "startArray",
                "number 42",
                "endArray [42]"),
                handler.getLog());
    }

    @Test
    public void arraysempty() throws IOException {
        assertEquals("[]", parse("[]").toString());
    }

    @Test
    public void arrayssingleValue() throws IOException {
        assertEquals("[23]", parse("[23]").toString());
    }

    @Test
    public void arraysmultipleValues() throws IOException {
        assertEquals("[23,42]", parse("[23,42]").toString());
    }

    @Test
    public void arrayswithWhitespaces() throws IOException {
        assertEquals("[23,42]", parse("[ 23 , 42 ]").toString());
    }

    @Test
    public void arraysnested() throws IOException {
        assertEquals("[[23]]", parse("[[23]]").toString());
        assertEquals("[[[]]]", parse("[[[]]]").toString());
        assertEquals("[[23],42]", parse("[[23],42]").toString());
        assertEquals("[[23],[42]]", parse("[[23],[42]]").toString());
        assertEquals("[[23],[42]]", parse("[[23],[42]]").toString());
        assertEquals("[{\"foo\":[23]},{\"bar\":[42]}]",
                parse("[{\"foo\":[23]},{\"bar\":[42]}]").toString());
    }

    @Test
    public void arraysillegalSyntax() {
        assertParseException(1, "Expected value", "[,]");
        assertParseException(4, "Expected ',' or ']'", "[23 42]");
        assertParseException(4, "Expected value", "[23,]");
    }

    @Test
    public void arraysincomplete() {
        assertParseException(1, "Unexpected end of input", "[");
        assertParseException(2, "Unexpected end of input", "[ ");
        assertParseException(3, "Unexpected end of input", "[23");
        assertParseException(4, "Unexpected end of input", "[23 ");
        assertParseException(4, "Unexpected end of input", "[23,");
        assertParseException(5, "Unexpected end of input", "[23, ");
    }

    @Test
    public void objectsempty() throws IOException {
        assertEquals("{}", parse("{}").toString());
    }

    @Test
    public void objectssingleValue() throws IOException {
        assertEquals("{\"foo\":23}", parse("{\"foo\":23}").toString());
    }

    @Test
    public void objectsmultipleValues() throws IOException {
        assertEquals("{\"foo\":23,\"bar\":42}", parse("{\"foo\":23,\"bar\":42}").toString());
    }

    @Test
    public void objectswhitespace() throws IOException {
        assertEquals("{\"foo\":23,\"bar\":42}", parse("{ \"foo\" : 23, \"bar\" : 42 }").toString());
    }

    @Test
    public void objectsnested() throws IOException {
        assertEquals("{\"foo\":{}}", parse("{\"foo\":{}}").toString());
        assertEquals("{\"foo\":{\"bar\":42}}", parse("{\"foo\":{\"bar\": 42}}").toString());
        assertEquals("{\"foo\":{\"bar\":{\"baz\":42}}}",
                parse("{\"foo\":{\"bar\": {\"baz\": 42}}}").toString());
        assertEquals("{\"foo\":[{\"bar\":{\"baz\":[[42]]}}]}",
                parse("{\"foo\":[{\"bar\": {\"baz\": [[42]]}}]}").toString());
    }

    @Test
    public void objectsillegalSyntax() {
        assertParseException(1, "Expected name", "{,}");
        assertParseException(1, "Expected name", "{:}");
        assertParseException(1, "Expected name", "{23}");
        assertParseException(4, "Expected ':'", "{\"a\"}");
        assertParseException(5, "Expected ':'", "{\"a\" \"b\"}");
        assertParseException(5, "Expected value", "{\"a\":}");
        assertParseException(8, "Expected name", "{\"a\":23,}");
        assertParseException(8, "Expected name", "{\"a\":23,42");
    }

    @Test
    public void objectsincomplete() {
        assertParseException(1, "Unexpected end of input", "{");
        assertParseException(2, "Unexpected end of input", "{ ");
        assertParseException(2, "Unexpected end of input", "{\"");
        assertParseException(4, "Unexpected end of input", "{\"a\"");
        assertParseException(5, "Unexpected end of input", "{\"a\" ");
        assertParseException(5, "Unexpected end of input", "{\"a\":");
        assertParseException(6, "Unexpected end of input", "{\"a\": ");
        assertParseException(7, "Unexpected end of input", "{\"a\":23");
        assertParseException(8, "Unexpected end of input", "{\"a\":23 ");
        assertParseException(8, "Unexpected end of input", "{\"a\":23,");
        assertParseException(9, "Unexpected end of input", "{\"a\":23, ");
    }

    @Test
    public void stringsemptyStringisAccepted() throws IOException {
        assertEquals("", parse("\"\"").asString());
    }

    @Test
    public void stringsasciiCharactersareAccepted() throws IOException {
        assertEquals(" ", parse("\" \"").asString());
        assertEquals("a", parse("\"a\"").asString());
        assertEquals("foo", parse("\"foo\"").asString());
        assertEquals("A2-D2", parse("\"A2-D2\"").asString());
        assertEquals("\u007f", parse("\"\u007f\"").asString());
    }

    @Test
    public void stringsnonAsciiCharactersareAccepted() throws IOException {
        assertEquals("Русский", parse("\"Русский\"").asString());
        assertEquals("العربية", parse("\"العربية\"").asString());
        assertEquals("日本語", parse("\"日本語\"").asString());
    }

    @Test
    public void stringscontrolCharactersareRejected() {
        // JSON string must not contain characters < 0x20
        assertParseException(3, "Expected valid string character", "\"--\n--\"");
        assertParseException(3, "Expected valid string character", "\"--\r\n--\"");
        assertParseException(3, "Expected valid string character", "\"--\t--\"");
        assertParseException(3, "Expected valid string character", "\"--\u0000--\"");
        assertParseException(3, "Expected valid string character", "\"--\u001f--\"");
    }

    @Test
    public void stringsvalidEscapesareAccepted() throws IOException {
        // valid escapes are \" \\ \/ \b \f \n \r \t and unicode escapes
        assertEquals(" \" ", parse("\" \\\" \"").asString());
        assertEquals(" \\ ", parse("\" \\\\ \"").asString());
        assertEquals(" / ", parse("\" \\/ \"").asString());
        assertEquals(" \u0008 ", parse("\" \\b \"").asString());
        assertEquals(" \u000c ", parse("\" \\f \"").asString());
        assertEquals(" \r ", parse("\" \\r \"").asString());
        assertEquals(" \n ", parse("\" \\n \"").asString());
        assertEquals(" \t ", parse("\" \\t \"").asString());
    }

    @Test
    public void stringsescapeatStart() throws IOException {
        assertEquals("\\x", parse("\"\\\\x\"").asString());
    }

    @Test
    public void stringsescapeatEnd() throws IOException {
        assertEquals("x\\", parse("\"x\\\\\"").asString());
    }

    @Test
    public void stringsillegalEscapesareRejected() {
        assertParseException(2, "Expected valid escape sequence", "\"\\a\"");
        assertParseException(2, "Expected valid escape sequence", "\"\\x\"");
        assertParseException(2, "Expected valid escape sequence", "\"\\000\"");
    }

    @Test
    public void stringsvalidUnicodeEscapesareAccepted() throws IOException {
        assertEquals("\u0021", parse("\"\\u0021\"").asString());
        assertEquals("\u4711", parse("\"\\u4711\"").asString());
        assertEquals("\uffff", parse("\"\\uffff\"").asString());
        assertEquals("\uabcdx", parse("\"\\uabcdx\"").asString());
    }

    @Test
    public void stringsillegalUnicodeEscapesareRejected() {
        assertParseException(3, "Expected hexadecimal digit", "\"\\u \"");
        assertParseException(3, "Expected hexadecimal digit", "\"\\ux\"");
        assertParseException(5, "Expected hexadecimal digit", "\"\\u20 \"");
        assertParseException(6, "Expected hexadecimal digit", "\"\\u000x\"");
    }

    @Test
    public void stringsincompleteStringsareRejected() {
        assertParseException(1, "Unexpected end of input", "\"");
        assertParseException(4, "Unexpected end of input", "\"foo");
        assertParseException(5, "Unexpected end of input", "\"foo\\");
        assertParseException(6, "Unexpected end of input", "\"foo\\n");
        assertParseException(6, "Unexpected end of input", "\"foo\\u");
        assertParseException(7, "Unexpected end of input", "\"foo\\u0");
        assertParseException(9, "Unexpected end of input", "\"foo\\u000");
        assertParseException(10, "Unexpected end of input", "\"foo\\u0000");
    }

    @Test
    public void numbersinteger() throws IOException {
        assertEquals(new JsonNumber("0"), parse("0"));
        assertEquals(new JsonNumber("-0"), parse("-0"));
        assertEquals(new JsonNumber("1"), parse("1"));
        assertEquals(new JsonNumber("-1"), parse("-1"));
        assertEquals(new JsonNumber("23"), parse("23"));
        assertEquals(new JsonNumber("-23"), parse("-23"));
        assertEquals(new JsonNumber("1234567890"), parse("1234567890"));
        assertEquals(new JsonNumber("123456789012345678901234567890"),
                parse("123456789012345678901234567890"));
    }

    @Test
    public void numbersminusZero() throws IOException {
        // allowed by JSON, allowed by Java
        JsonValue value = parse("-0");
        assertEquals(0, value.asInt());
        assertEquals(0L, value.asLong());
        assertEquals(0f, value.asFloat(), 0);
        assertEquals(0d, value.asDouble(), 0);
    }

    @Test
    public void numbersdecimal() throws IOException {
        assertEquals(new JsonNumber("0.23"), parse("0.23"));
        assertEquals(new JsonNumber("-0.23"), parse("-0.23"));
        assertEquals(new JsonNumber("1234567890.12345678901234567890"),
                parse("1234567890.12345678901234567890"));
    }

    @Test
    public void numberswithExponent() throws IOException {
        assertEquals(new JsonNumber("0.1e9"), parse("0.1e9"));
        assertEquals(new JsonNumber("0.1e9"), parse("0.1e9"));
        assertEquals(new JsonNumber("0.1E9"), parse("0.1E9"));
        assertEquals(new JsonNumber("-0.23e9"), parse("-0.23e9"));
        assertEquals(new JsonNumber("0.23e9"), parse("0.23e9"));
        assertEquals(new JsonNumber("0.23e+9"), parse("0.23e+9"));
        assertEquals(new JsonNumber("0.23e-9"), parse("0.23e-9"));
    }

    @Test
    public void numberswithInvalidFormat() {
        assertParseException(0, "Expected value", "+1");
        assertParseException(0, "Expected value", ".1");
        assertParseException(1, "Unexpected character", "02");
        assertParseException(2, "Unexpected character", "-02");
        assertParseException(1, "Expected digit", "-x");
        assertParseException(2, "Expected digit", "1.x");
        assertParseException(2, "Expected digit", "1ex");
        assertParseException(3, "Unexpected character", "1e1x");
    }

    @Test
    public void numbersincomplete() {
        assertParseException(1, "Unexpected end of input", "-");
        assertParseException(2, "Unexpected end of input", "1.");
        assertParseException(4, "Unexpected end of input", "1.0e");
        assertParseException(5, "Unexpected end of input", "1.0e-");
    }

    @Test
    public void nullcomplete() throws IOException {
        assertEquals(JsonLiteral.NULL, parse("null"));
    }

    @Test
    public void nullincomplete() {
        assertParseException(1, "Unexpected end of input", "n");
        assertParseException(2, "Unexpected end of input", "nu");
        assertParseException(3, "Unexpected end of input", "nul");
    }

    @Test
    public void nullwithIllegalCharacter() {
        assertParseException(1, "Expected 'u'", "nx");
        assertParseException(2, "Expected 'l'", "nux");
        assertParseException(3, "Expected 'l'", "nulx");
        assertParseException(4, "Unexpected character", "nullx");
    }

    @Test
    public void truecomplete() throws IOException {
        assertSame(JsonLiteral.TRUE, parse("true"));
    }

    @Test
    public void trueincomplete() {
        assertParseException(1, "Unexpected end of input", "t");
        assertParseException(2, "Unexpected end of input", "tr");
        assertParseException(3, "Unexpected end of input", "tru");
    }

    @Test
    public void truewithIllegalCharacter() {
        assertParseException(1, "Expected 'r'", "tx");
        assertParseException(2, "Expected 'u'", "trx");
        assertParseException(3, "Expected 'e'", "trux");
        assertParseException(4, "Unexpected character", "truex");
    }

    @Test
    public void falsecomplete() throws IOException {
        assertSame(JsonLiteral.FALSE, parse("false"));
    }

    @Test
    public void falseincomplete() {
        assertParseException(1, "Unexpected end of input", "f");
        assertParseException(2, "Unexpected end of input", "fa");
        assertParseException(3, "Unexpected end of input", "fal");
        assertParseException(4, "Unexpected end of input", "fals");
    }

    @Test
    public void falsewithIllegalCharacter() {
        assertParseException(1, "Expected 'a'", "fx");
        assertParseException(2, "Expected 'l'", "fax");
        assertParseException(3, "Expected 's'", "falx");
        assertParseException(4, "Expected 'e'", "falsx");
        assertParseException(5, "Unexpected character", "falsex");
    }

    private void assertParseException(int offset, String message, final String json) {
        TestHandler handler = new TestHandler();
        JsonReader reader = new JsonReader(new StringReader(json), handler);
        Assertions.assertThrows(JsonException.class, () -> {
            try {
                reader.parse();
            } catch (IOException e) {
                // ignore
            }
        });
    }

    private static class TestHandler implements JsonHandler {

        List<String> log = new LinkedList<>();

        JsonValue value;

        @Override
        public void nullValue() {
            record("null");
            this.value = new JsonNull();
        }

        @Override
        public void booleanValue(boolean value) {
            record("bool", value);
            this.value = new JsonBool(value);
        }

        @Override
        public void stringValue(String string) {
            record("string", string);
            this.value = new JsonString(string);
        }

        @Override
        public void numberValue(String string) {
            record("number", string);
            this.value = new JsonNumber(string);
        }

        @Override
        public void startArray() {
            record("startArray");
        }

        @Override
        public void endArray(JsonArray array) {
            record("endArray", array);
            this.value = array;
        }

        @Override
        public void startObject() {
            record("startObject");
        }

        @Override
        public void endObject(JsonObject object) {
            record("endObject", object);
            this.value = object;
        }

        @Override
        public void objectName(JsonObject object, String name) {
            record("objectName", object, name);
        }

        @Override
        public void objectValue(JsonObject object, String name) {
            record("objectValue", object, name);
        }

        @Override
        public JsonValue getValue() {
            return value;
        }

        private void record(String event, Object... args) {
            log.add(event + (args.length == 0 ? "" : args.length == 1 ? " " + args[0] : " " + Arrays.asList(args)));
        }

        String getLog() {
            return String.join("\n", log);
        }

    }
}
