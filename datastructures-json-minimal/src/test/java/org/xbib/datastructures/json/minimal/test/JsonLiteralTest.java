package org.xbib.datastructures.json.minimal.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.xbib.datastructures.json.minimal.JsonLiteral.FALSE;
import static org.xbib.datastructures.json.minimal.JsonLiteral.NULL;
import static org.xbib.datastructures.json.minimal.JsonLiteral.TRUE;

import org.junit.jupiter.api.Test;
import org.xbib.datastructures.json.minimal.Json;
import org.xbib.datastructures.json.minimal.JsonWriter;
import java.io.IOException;

public class JsonLiteralTest {

    @Test
    public void isNull() {
        assertTrue(NULL.isNull());
        assertFalse(TRUE.isNull());
        assertFalse(FALSE.isNull());
    }

    @Test
    public void isTrue() {
        assertTrue(TRUE.isTrue());
        assertFalse(NULL.isTrue());
        assertFalse(FALSE.isTrue());
    }

    @Test
    public void isFalse() {
        assertTrue(FALSE.isFalse());
        assertFalse(NULL.isFalse());
        assertFalse(TRUE.isFalse());
    }

    @Test
    public void isBoolean() {
        assertTrue(TRUE.isBoolean());
        assertTrue(FALSE.isBoolean());
        assertFalse(NULL.isBoolean());
    }

    @Test
    public void nullwrite() throws IOException {
        JsonWriter writer = mock(JsonWriter.class);
        NULL.write(writer);
        verify(writer).writeLiteral("null");
        verifyNoMoreInteractions(writer);
    }

    @Test
    public void truewrite() throws IOException {
        JsonWriter writer = mock(JsonWriter.class);
        TRUE.write(writer);
        verify(writer).writeLiteral("true");
        verifyNoMoreInteractions(writer);
    }

    @Test
    public void falsewrite() throws IOException {
        JsonWriter writer = mock(JsonWriter.class);
        FALSE.write(writer);
        verify(writer).writeLiteral("false");
        verifyNoMoreInteractions(writer);
    }

    @Test
    public void nulltoString() {
        assertEquals("null", NULL.toString());
    }

    @Test
    public void truetoString() {
        assertEquals("true", TRUE.toString());
    }

    @Test
    public void falsetoString() {
        assertEquals("false", FALSE.toString());
    }

    @Test
    public void nullequals() {
        assertEquals(NULL, NULL);
        assertNotEquals(NULL, null);
        assertNotEquals(TRUE, NULL);
        assertNotEquals(FALSE, NULL);
        assertNotEquals(Json.of("null"), NULL);
    }

    @Test
    public void trueequals() {
        assertEquals(TRUE, TRUE);
        assertNotEquals(TRUE, null);
        assertNotEquals(FALSE, TRUE);
        assertNotEquals(Boolean.TRUE, TRUE);
        assertNotEquals(Json.of("true"), NULL);
    }

    @Test
    public void falseequals() {
        assertEquals(FALSE, FALSE);
        assertNotEquals(FALSE, null);
        assertNotEquals(TRUE, FALSE);
        assertNotEquals(Boolean.FALSE, FALSE);
        assertNotEquals(Json.of("false"), NULL);
    }
}
