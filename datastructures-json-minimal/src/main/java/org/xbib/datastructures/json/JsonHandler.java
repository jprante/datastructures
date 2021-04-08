package org.xbib.datastructures.json;

/**
 * An interface for parser events. A {@link JsonHandler} can be given to a {@link JsonReader}. The
 * parser will then call the methods of the given handler while reading the input.
 * Implementations that build an object representation of the parsed JSON can return arbitrary handler
 * objects for JSON arrays and JSON objects in {@link #startArray()} and {@link #startObject()}.
 * These handler objects will then be provided in all subsequent parser events for this particular
 * array or object. They can be used to keep track the elements of a JSON array or object.
 *
 * @see JsonReader
 */
public interface JsonHandler {

    /**
     * Indicates a <code>null</code> literal in the JSON input. This method will be
     * called when reading the first character of the literal.
     */
    void nullValue();

    /**
     * Indicates a boolean literal (<code>true</code> or <code>false</code>) in the JSON
     * input. This method will be called after reading the last character of the literal.
     *
     * @param value the parsed boolean value
     */
    void booleanValue(boolean value);

    /**
     * Indicates  a string in the JSON input. This method will be called after reading the
     * closing double quote character (<code>'&quot;'</code>).
     *
     * @param string the parsed string
     */
    void stringValue(String string);

    /**
     * Indicates a number in the JSON input. This method will be called after reading the
     * last character of the number.
     *
     * @param string the parsed number string
     */
    void numberValue(String string);

    /**
     * Indicates the beginning of an array in the JSON input. This method will be called when reading
     * the opening square bracket character (<code>'['</code>).
     */
    void startArray();

    /**
     * Indicates the end of an array in the JSON input. This method will be called after reading the
     * closing square bracket character (<code>']'</code>).
     *
     * @param array the array handler returned from {@link #startArray()}, or <code>null</code> if not
     *              provided
     */
    void endArray(JsonArray array);

    /**
     * Indicates the beginning of an object in the JSON input. This method will be called when reading
     * the opening curly bracket character (<code>'{'</code>).
     */
    void startObject();

    /**
     * Indicates the end of an object in the JSON input. This method will be called after reading the
     * closing curly bracket character (<code>'}'</code>).
     *
     * @param object the object handler returned from {@link #startObject()}, or null if not provided
     */
    void endObject(JsonObject object);

    /**
     * Indicates an object member name in the JSON input. This method will be called after
     * reading the closing quote character (<code>'"'</code>) of the member name.
     *
     * @param object the object handler returned from {@link #startObject()}, or null if not provided
     * @param name   the parsed member name
     */
    void objectName(JsonObject object, String name);

    /**
     * Indicates an object member value in the JSON input. This method will be called after
     * reading the last character of the member value, just after the <code>end</code> method for the
     * specific member type (like {@link #stringValue(String) endString()}, {@link #numberValue(String)
     * endNumber()}, etc.).
     *
     * @param object the object handler returned from {@link #startObject()}, or null if not provided
     * @param name   the parsed member name
     */
    void objectValue(JsonObject object, String name);

    JsonValue getValue();

}
