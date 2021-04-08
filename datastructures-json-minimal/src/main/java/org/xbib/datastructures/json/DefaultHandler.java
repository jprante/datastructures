package org.xbib.datastructures.json;

public class DefaultHandler implements JsonHandler {

    protected JsonValue value;

    @Override
    public JsonValue getValue() {
        return value;
    }

    @Override
    public void startObject() {
    }

    @Override
    public void nullValue() {
        value = JsonLiteral.NULL;
    }

    @Override
    public void booleanValue(boolean bool) {
        value = bool ? JsonLiteral.TRUE : JsonLiteral.FALSE;
    }

    @Override
    public void stringValue(String string) {
        value = new JsonString(string);
    }

    @Override
    public void numberValue(String string) {
        value = new JsonNumber(string);
    }

    @Override
    public void startArray() {
    }

    @Override
    public void endArray(JsonArray array) {
        value = array;
    }

    @Override
    public void endObject(JsonObject object) {
        value = object;
    }

    @Override
    public void objectName(JsonObject object, String name) {
    }

    @Override
    public void objectValue(JsonObject object, String name) {
        object.add(name, value);
    }
}
