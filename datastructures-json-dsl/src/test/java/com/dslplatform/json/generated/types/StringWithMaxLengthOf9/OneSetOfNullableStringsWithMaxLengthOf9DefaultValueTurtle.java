package com.dslplatform.json.generated.types.StringWithMaxLengthOf9;


import com.dslplatform.json.generated.ocd.javaasserts.StringWithMaxLengthOf9Asserts;
import com.dslplatform.json.generated.types.StaticJson;
import java.io.IOException;

public class OneSetOfNullableStringsWithMaxLengthOf9DefaultValueTurtle {
    private static StaticJson.JsonSerialization jsonSerialization;

    @org.junit.BeforeClass
    public static void initializeJsonSerialization() throws IOException {
        jsonSerialization = StaticJson.getSerialization();
    }

    @org.junit.Test
    public void testDefaultValueEquality() throws IOException {
        final java.util.Set<String> defaultValue = new java.util.HashSet<String>(0);
        final StaticJson.Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final java.util.List<String> deserializedTmpList = jsonSerialization.deserializeList(String.class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        final java.util.Set<String> defaultValueJsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<String>(deserializedTmpList);
        StringWithMaxLengthOf9Asserts.assertOneSetOfNullableEquals(defaultValue, defaultValueJsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final java.util.Set<String> borderValue1 = new java.util.HashSet<String>(java.util.Arrays.asList((String) null));
        final StaticJson.Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final java.util.List<String> deserializedTmpList = jsonSerialization.deserializeList(String.class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        final java.util.Set<String> borderValue1JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<String>(deserializedTmpList);
        StringWithMaxLengthOf9Asserts.assertOneSetOfNullableEquals(borderValue1, borderValue1JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final java.util.Set<String> borderValue2 = new java.util.HashSet<String>(java.util.Arrays.asList(""));
        final StaticJson.Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final java.util.List<String> deserializedTmpList = jsonSerialization.deserializeList(String.class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        final java.util.Set<String> borderValue2JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<String>(deserializedTmpList);
        StringWithMaxLengthOf9Asserts.assertOneSetOfNullableEquals(borderValue2, borderValue2JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final java.util.Set<String> borderValue3 = new java.util.HashSet<String>(java.util.Arrays.asList("xxxxxxxxx"));
        final StaticJson.Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        final java.util.List<String> deserializedTmpList = jsonSerialization.deserializeList(String.class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
        final java.util.Set<String> borderValue3JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<String>(deserializedTmpList);
        StringWithMaxLengthOf9Asserts.assertOneSetOfNullableEquals(borderValue3, borderValue3JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue4Equality() throws IOException {
        final java.util.Set<String> borderValue4 = new java.util.HashSet<String>(java.util.Arrays.asList("", "\"", "'/\\[](){}", "\u0001\u0002\u0003\u0004\u0005\u0006\u0007\b\t", "xxxxxxxxx"));
        final StaticJson.Bytes borderValue4JsonSerialized = jsonSerialization.serialize(borderValue4);
        final java.util.List<String> deserializedTmpList = jsonSerialization.deserializeList(String.class, borderValue4JsonSerialized.content, borderValue4JsonSerialized.length);
        final java.util.Set<String> borderValue4JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<String>(deserializedTmpList);
        StringWithMaxLengthOf9Asserts.assertOneSetOfNullableEquals(borderValue4, borderValue4JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue5Equality() throws IOException {
        final java.util.Set<String> borderValue5 = new java.util.HashSet<String>(java.util.Arrays.asList(null, "", "\"", "'/\\[](){}", "\u0001\u0002\u0003\u0004\u0005\u0006\u0007\b\t", "xxxxxxxxx"));
        final StaticJson.Bytes borderValue5JsonSerialized = jsonSerialization.serialize(borderValue5);
        final java.util.List<String> deserializedTmpList = jsonSerialization.deserializeList(String.class, borderValue5JsonSerialized.content, borderValue5JsonSerialized.length);
        final java.util.Set<String> borderValue5JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<String>(deserializedTmpList);
        StringWithMaxLengthOf9Asserts.assertOneSetOfNullableEquals(borderValue5, borderValue5JsonDeserialized);
    }
}
