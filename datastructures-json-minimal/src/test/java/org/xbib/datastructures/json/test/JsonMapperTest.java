package org.xbib.datastructures.json.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.xbib.datastructures.json.Json;
import org.xbib.datastructures.json.JsonMapper;
import org.xbib.datastructures.json.JsonValue;
import java.io.IOException;

public class JsonMapperTest {

    @Test
    public void mapperMapTest() throws IOException {
        String json = "{\"Hello\":\"World\"}";
        JsonValue jsonValue = Json.parse(json);
        Object object = JsonMapper.asObject(jsonValue);
        assertEquals("{Hello=World}", object.toString());
    }

    @Test
    public void mapperNumericMapTest() throws IOException {
        String json = "{\"Hello\":123}";
        JsonValue jsonValue = Json.parse(json);
        Object object = JsonMapper.asObject(jsonValue);
        assertEquals("{Hello=123}", object.toString());
    }

    @Test
    public void mapperArrayTest() throws IOException {
        String json = "[\"Hello\",\"World\"]";
        JsonValue jsonValue = Json.parse(json);
        Object object = JsonMapper.asObject(jsonValue);
        assertEquals("[Hello, World]", object.toString());
    }

    @Test
    public void mapperBooleanAndNullArrayTest() throws IOException {
        String json = "[true, false, null]";
        JsonValue jsonValue = Json.parse(json);
        Object object = JsonMapper.asObject(jsonValue);
        assertEquals("[true, false, null]", object.toString());
    }

    @Test
    public void mapperFloatArrayTest() throws IOException {
        String json = "[1.23, 4.56]";
        JsonValue jsonValue = Json.parse(json);
        Object object = JsonMapper.asObject(jsonValue);
        assertEquals("[1.23, 4.56]", object.toString());
    }

    @Test
    public void mapperIntArrayTest() throws IOException {
        String json = "[123, 456]";
        JsonValue jsonValue = Json.parse(json);
        Object object = JsonMapper.asObject(jsonValue);
        assertEquals("[123, 456]", object.toString());
    }
}
