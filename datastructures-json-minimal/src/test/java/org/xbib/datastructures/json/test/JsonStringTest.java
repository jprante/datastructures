package org.xbib.datastructures.json.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xbib.datastructures.json.JsonString;
import org.xbib.datastructures.json.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;

public class JsonStringTest {

    private StringWriter stringWriter;

    private JsonWriter jsonWriter;

    @BeforeEach
    public void setUp() {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
    }

    @Test
    public void constructor_failsWithNull() {
        Assertions.assertThrows(NullPointerException.class, () -> new JsonString(null));
    }

    @Test
    public void write() throws IOException {
        new JsonString("foo").write(jsonWriter);
        assertEquals("\"foo\"", stringWriter.toString());
    }

    @Test
    public void write_escapesStrings() throws IOException {
        new JsonString("foo\\bar").write(jsonWriter);
        assertEquals("\"foo\\\\bar\"", stringWriter.toString());
    }

    @Test
    public void isString() {
        assertTrue(new JsonString("foo").isString());
    }

    @Test
    public void asString() {
        assertEquals("foo", new JsonString("foo").asString());
    }

    @Test
    public void equals_trueForSameInstance() {
        JsonString string = new JsonString("foo");
        assertEquals(string, string);
    }

    @Test
    public void equals_trueForEqualStrings() {
        assertEquals(new JsonString("foo"), new JsonString("foo"));
    }

    @Test
    public void equals_falseForDifferentStrings() {
        assertNotEquals(new JsonString("foo"), new JsonString(""));
        assertNotEquals(new JsonString("bar"), new JsonString("foo"));
    }

    @Test
    public void equals_falseForNull() {
        assertNotEquals(new JsonString("foo"), null);
    }

    @Test
    public void equals_falseForSubclass() {
        assertNotEquals(new JsonString("foo") {}, new JsonString("foo"));
    }

    @Test
    public void hashCode_equalsForEqualStrings() {
        assertEquals(new JsonString("foo").hashCode(), new JsonString("foo").hashCode());
    }

    @Test
    public void hashCode_differsForDifferentStrings() {
        assertNotEquals(new JsonString("foo").hashCode(), new JsonString("").hashCode());
        assertNotEquals(new JsonString("bar").hashCode(), new JsonString("foo").hashCode());
    }
}
