package org.xbib.datastructures.json;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Represents a JSON value. This can be a JSON <strong>object</strong>, an <strong> array</strong>,
 * a <strong>number</strong>, a <strong>string</strong>, or one of the literals
 * <strong>true</strong>, <strong>false</strong>, and <strong>null</strong>.
 * <p>
 * The literals <strong>true</strong>, <strong>false</strong>, and <strong>null</strong> are
 * represented by the constants
 * {@code JsonLiteral.NULL}, {@code JsonLiteral.FALSE}, and {@code JsonLiteral.NULL}.
 * </p>
 * <p>
 * JSON <strong>objects</strong> and <strong>arrays</strong> are represented by the subtypes
 * {@link JsonObject} and {@link JsonArray}. Instances of these types can be created using the
 * public constructors of these classes.
 * </p>
 * <p>
 * Instances that represent JSON <strong>numbers</strong>, <strong>strings</strong> and
 * <strong>boolean</strong> values can be created using the static factory methods
 * {@code JSON.parse(String)}, {@code valueOf(long)}, {@code valueOf(double)}, etc.
 * </p>
 * <p>
 * In order to find out whether an instance of this class is of a certain type, the methods
 * {@link #isObject()}, {@link #isArray()}, {@link #isString()}, {@link #isInt()} etc. can be
 * used.
 * </p>
 * <p>
 * If the type of a JSON value is known, the methods {@link #asObject()}, {@link #asArray()},
 * {@link #asString()}, {@link #asInt()}, etc. can be used to get this value directly in the
 * appropriate target type.
 * </p>
 */
public abstract class JsonValue {

    /**
     * Detects whether this value represents a JSON object. If this is the case, this value is an
     * instance of {@link JsonObject}.
     *
     * @return <code>true</code> if this value is an instance of JsonObject
     */
    public boolean isObject() {
        return false;
    }

    /**
     * Detects whether this value represents a JSON array. If this is the case, this value is an
     * instance of {@link JsonArray}.
     *
     * @return <code>true</code> if this value is an instance of JsonArray
     */
    public boolean isArray() {
        return false;
    }

    /**
     * Detects whether this value represents a JSON number that is an integer.
     *
     * @return <code>true</code> if this value represents a JSON number that is an integer
     */
    public boolean isInt() {
        return false;
    }

    /**
     * Detects whether this value represents a JSON number that is an long.
     *
     * @return <code>true</code> if this value represents a JSON number that is an long
     */
    public boolean isLong() {
        return false;
    }

    /**
     * Detects whether this value represents a JSON number that is an float.
     *
     * @return <code>true</code> if this value represents a JSON number that is an float
     */
    public boolean isFloat() {
        return false;
    }

    /**
     * Detects whether this value represents a JSON number that is an double.
     *
     * @return <code>true</code> if this value represents a JSON number that is an double
     */
    public boolean isDouble() {
        return false;
    }

    /**
     * Detects whether this value represents a JSON string.
     *
     * @return <code>true</code> if this value represents a JSON string
     */
    public boolean isString() {
        return false;
    }

    /**
     * Detects whether this value represents a boolean value.
     *
     * @return <code>true</code> if this value represents either the JSON literal <code>true</code> or
     * <code>false</code>
     */
    public boolean isBoolean() {
        return false;
    }

    /**
     * Detects whether this value represents the JSON literal <code>true</code>.
     *
     * @return <code>true</code> if this value represents the JSON literal <code>true</code>
     */
    public boolean isTrue() {
        return false;
    }

    /**
     * Detects whether this value represents the JSON literal <code>false</code>.
     *
     * @return <code>true</code> if this value represents the JSON literal <code>false</code>
     */
    public boolean isFalse() {
        return false;
    }

    /**
     * Detects whether this value represents the JSON literal <code>null</code>.
     *
     * @return <code>true</code> if this value represents the JSON literal <code>null</code>
     */
    public boolean isNull() {
        return false;
    }

    /**
     * Returns this JSON value as {@link JsonObject}, assuming that this value represents a JSON
     * object. If this is not the case, an exception is thrown.
     *
     * @return a JSONObject for this value
     * @throws UnsupportedOperationException if this value is not a JSON object
     */
    public JsonObject asObject() {
        throw new UnsupportedOperationException("Not an object: " + toString());
    }

    /**
     * Returns this JSON value as {@link JsonArray}, assuming that this value represents a JSON array.
     * If this is not the case, an exception is thrown.
     *
     * @return a JSONArray for this value
     * @throws UnsupportedOperationException if this value is not a JSON array
     */
    public JsonArray asArray() {
        throw new UnsupportedOperationException("Not an array: " + toString());
    }

    /**
     * Returns this JSON value as an <code>int</code> value, assuming that this value represents a
     * JSON number that can be interpreted as Java <code>int</code>. If this is not the case, an
     * exception is thrown.
     * <p>
     * To be interpreted as Java <code>int</code>, the JSON number must neither contain an exponent
     * nor a fraction part. Moreover, the number must be in the <code>Integer</code> range.
     * </p>
     *
     * @return this value as <code>int</code>
     * @throws UnsupportedOperationException if this value is not a JSON number
     * @throws NumberFormatException         if this JSON number can not be interpreted as <code>int</code> value
     */
    public int asInt() {
        throw new UnsupportedOperationException("Not a number: " + toString());
    }

    /**
     * Returns this JSON value as a <code>long</code> value, assuming that this value represents a
     * JSON number that can be interpreted as Java <code>long</code>. If this is not the case, an
     * exception is thrown.
     * <p>
     * To be interpreted as Java <code>long</code>, the JSON number must neither contain an exponent
     * nor a fraction part. Moreover, the number must be in the <code>Long</code> range.
     * </p>
     *
     * @return this value as <code>long</code>
     * @throws UnsupportedOperationException if this value is not a JSON number
     * @throws NumberFormatException         if this JSON number can not be interpreted as <code>long</code> value
     */
    public long asLong() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns this JSON value as a <code>float</code> value, assuming that this value represents a
     * JSON number. If this is not the case, an exception is thrown.
     * <p>
     * If the JSON number is out of the <code>Float</code> range, {@link Float#POSITIVE_INFINITY} or
     * {@link Float#NEGATIVE_INFINITY} is returned.
     * </p>
     *
     * @return this value as <code>float</code>
     * @throws UnsupportedOperationException if this value is not a JSON number
     */
    public float asFloat() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns this JSON value as a <code>double</code> value, assuming that this value represents a
     * JSON number. If this is not the case, an exception is thrown.
     * <p>
     * If the JSON number is out of the <code>Double</code> range, {@link Double#POSITIVE_INFINITY} or
     * {@link Double#NEGATIVE_INFINITY} is returned.
     * </p>
     *
     * @return this value as <code>double</code>
     * @throws UnsupportedOperationException if this value is not a JSON number
     */
    public double asDouble() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns this JSON value as String, assuming that this value represents a JSON string. If this
     * is not the case, an exception is thrown.
     *
     * @return the string represented by this value
     * @throws UnsupportedOperationException if this value is not a JSON string
     */
    public String asString() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns this JSON value as a <code>boolean</code> value, assuming that this value is either
     * <code>true</code> or <code>false</code>. If this is not the case, an exception is thrown.
     *
     * @return this value as <code>boolean</code>
     * @throws UnsupportedOperationException if this value is neither <code>true</code> or <code>false</code>
     */
    public boolean asBoolean() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the JSON string for this value in its minimal form, without any additional whitespace.
     *
     * @return a JSON string that represents this value
     */
    @Override
    public String toString() {
        StringWriter writer = new StringWriter();
        toString(writer, JsonWriterConfig.minimal());
        return writer.toString();
    }

    /**
     * Returns the JSON string for this value using the given formatting.
     *
     * @param writer the writer
     * @param config a configuration that controls the formatting or <code>null</code> for the minimal form
     */
    public void toString(Writer writer, JsonWriterConfig config) {
        try {
            write(config.createWriter(writer));
        } catch (IOException exception) {
            throw new JsonException(exception);
        }
    }

    public abstract void write(JsonWriter writer) throws IOException;

}
