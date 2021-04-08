package org.xbib.datastructures.json.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xbib.datastructures.json.Lexer;
import org.xbib.datastructures.json.token.TokenType;
import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Logger;

public class LexerText {

    private static final Logger logger = Logger.getLogger(LexerText.class.getName());

    private Gson gson;

    @BeforeEach
    public void setUp() {
        gson = new Gson();
    }

    @Test
    public void testDecoderIsLenientByDefaultButReaderIsNot() {
        Lexer lexer = new Lexer(new StringReader("{a:1}"));
        assertFalse(lexer.isLenient());
    }

    @Test
    public void testValid() throws IOException {
        // no ints because of gson adheres to "Javascript has no ints"
        assertValidJson(1, "{a:1.0}");
        assertValidJson(2, "{a:1.0}{b:3.0}");
        assertValidJson(2, "{a:1.0}\n\t{b:3.0}");
        assertValidJson(1, "{'a':[1.0, '2', true, null]}");
    }

    @Test
    public void testLists() throws Exception {
        //assertListOf("[1, 2, 3, 99999]", com.google.gson.stream.JsonReader::nextLong, Lexer::nextNumber);
        //assertListOf("[1.0, 2.0, 4.5, NaN]", com.google.gson.stream.JsonReader::nextDouble, Lexer::nextNumber);
        //assertListOf("[true, false]", com.google.gson.stream.JsonReader::nextBoolean, Lexer::nextBoolean);
        assertListOf("[1, '2', \"3\", 4.5, NaN]", com.google.gson.stream.JsonReader::nextString, Lexer::nextString);
        assertListOf("[null, null]", com.google.gson.stream.JsonReader::nextNull, Lexer::nextNull);
    }

    @Test
    public void testInvalid() throws IOException {
        assertInvalidJson(false, "Use setLenient(true)", "1-", com.google.gson.stream.JsonReader::peek, Lexer::next);
        assertInvalidJson(false, "Use setLenient(true)", "1+", com.google.gson.stream.JsonReader::peek, Lexer::next);
        assertInvalidJson(false, "Use setLenient(true)", "01", com.google.gson.stream.JsonReader::peek, Lexer::next);
        //assertInvalidJson(false, "Use setLenient(true)", "f", com.google.gson.stream.JsonReader::nextBoolean, Lexer::nextBoolean);
        //assertInvalidJson(false, "Use setLenient(true)", "felse", com.google.gson.stream.JsonReader::nextBoolean, Lexer::nextBoolean);
        //assertInvalidJson(true, "Expected a double, but was BOOLEAN", "true", com.google.gson.stream.JsonReader::nextDouble, Lexer::nextNumber);
        //assertInvalidJson(false, "NaN and infinities forbidden", "\"NaN\"", com.google.gson.stream.JsonReader::nextDouble, Lexer::nextNumber);
        assertInvalidJson(true, "Unterminated string", "\"NaN", com.google.gson.stream.JsonReader::nextString, Lexer::nextString);
        assertInvalidJson(true, "Unterminated string", "\"NaN", com.google.gson.stream.JsonReader::skipValue, Lexer::skipValue);
        assertInvalidJson(true, "Unterminated string", "'NaN", com.google.gson.stream.JsonReader::skipValue, Lexer::skipValue);
        assertInvalidJson(true, "Use setLenient(true)", "{test;", x -> {
            x.beginObject();
            x.peek();
            x.setLenient(false);
            x.skipValue();
        }, x -> {
            x.beginObject();
            x.next();
            x.setLenient(false);
            x.skipValue();
        });
        assertInvalidJson(true, "Use setLenient(true)", "{test;", x -> {
            x.beginObject();
            x.peek();
            x.setLenient(false);
            x.nextName();
        }, x -> {
            x.beginObject();
            x.next();
            x.setLenient(false);
            x.nextName();
        });
        /*assertInvalidJson(true, "Unterminated array ", "[1[", x -> {
            x.beginArray();
            x.nextDouble();
            x.nextDouble();
        }, x -> {
            x.beginArray();
            x.nextNumber();
            x.nextNumber();
        });*/
        assertInvalidJson(true, "Unterminated comment", "/*", com.google.gson.stream.JsonReader::skipValue, Lexer::skipValue);
    }

    private void assertValidJson(int expectedDocs, String s) throws IOException {
        int actualDocs = 0;
        try (com.google.gson.stream.JsonReader expectedReader = new com.google.gson.stream.JsonReader(new StringReader(s))) {
            Lexer lexer = new Lexer(new StringReader(s), true);
            expectedReader.setLenient(true);
            while (expectedReader.peek() != com.google.gson.stream.JsonToken.END_DOCUMENT) {
                Object expectedValue = gson.fromJson(expectedReader, Object.class);
                Object value = lexer.nextObject();
                logger.info("expected = " + expectedValue + " " + expectedValue.getClass());
                logger.info("value = " + value + " " + value.getClass());
                assertEquals(expectedValue.toString(), value.toString());
                actualDocs++;
            }
        }
        assertEquals(actualDocs, expectedDocs);
    }

    private <T> void assertListOf(String s, CheckedFunction<com.google.gson.stream.JsonReader, T> expectedFn, CheckedFunction<Lexer, T> actualFn) throws Exception {
        try (com.google.gson.stream.JsonReader expectedReader = new com.google.gson.stream.JsonReader(new StringReader(s))) {
            expectedReader.setLenient(true);
            expectedReader.beginArray();
            Lexer lexer = new Lexer(new StringReader(s), true);
            lexer.beginArray();
            while (expectedReader.hasNext()) {
                T expectedValue = expectedFn.apply(expectedReader);
                assertEquals(actualFn.apply(lexer), expectedValue);
            }
            assertFalse(lexer.hasNext());
            assertEquals(lexer.next(), TokenType.END_ARRAY);
            expectedReader.endArray();
            lexer.endArray();
            assertEquals(lexer.next().name(), com.google.gson.stream.JsonToken.END_DOCUMENT.name());
            assertEquals(expectedReader.peek().name(), com.google.gson.stream.JsonToken.END_DOCUMENT.name());
        }
    }

    private void assertListOf(String s, VoidCheckedFunction<com.google.gson.stream.JsonReader> expectedFn, VoidCheckedFunction<Lexer> actualFn) throws Exception {
        assertListOf(s, x -> {
            expectedFn.apply(x);
            return null;
        }, x -> {
            actualFn.apply(x);
            return null;
        });
    }

    private void assertInvalidJson(boolean isLenient, String exception, String s, VoidCheckedFunction<com.google.gson.stream.JsonReader> expectedFn, VoidCheckedFunction<Lexer> actualFn) throws IOException {
        assertInvalidJson(isLenient, exception, s, x -> {
            expectedFn.apply(x);
            return null;
        }, x -> {
            actualFn.apply(x);
            return null;
        });
    }

    private <T> void assertInvalidJson(boolean isLenient, String exception, String s, CheckedFunction<com.google.gson.stream.JsonReader, T> expectedFn, CheckedFunction<Lexer, T> actualFn) throws IOException {
        try (com.google.gson.stream.JsonReader expectedReader = new com.google.gson.stream.JsonReader(new StringReader(s))) {
            Lexer lexer = new Lexer(new StringReader(s), isLenient);
            expectedReader.setLenient(isLenient);
            Assertions.assertThrows(Exception.class, () -> {
                expectedFn.apply(expectedReader);
            });
            Assertions.assertThrows(Exception.class, () -> {
                actualFn.apply(lexer);
            });
        }
    }

    @FunctionalInterface
    public interface CheckedFunction<T, R> {
        R apply(T t) throws Exception;
    }

    @FunctionalInterface
    public interface VoidCheckedFunction<T> {
        void apply(T t) throws Exception;
    }
}
