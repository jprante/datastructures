package com.dslplatform.json.generated.types.Boolean;


import com.dslplatform.json.generated.ocd.javaasserts.BooleanAsserts;
import com.dslplatform.json.generated.types.StaticJson;
import java.io.IOException;

public class NullableSetOfOneBooleansDefaultValueTurtle {
    private static StaticJson.JsonSerialization jsonSerialization;

    @org.junit.BeforeClass
    public static void initializeJsonSerialization() throws IOException {
        jsonSerialization = StaticJson.getSerialization();
    }

    @org.junit.Test
    public void testDefaultValueEquality() throws IOException {
        final java.util.Set<Boolean> defaultValue = null;
        final StaticJson.Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final java.util.List<Boolean> deserializedTmpList = jsonSerialization.deserializeList(Boolean.class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        final java.util.Set<Boolean> defaultValueJsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<Boolean>(deserializedTmpList);
        BooleanAsserts.assertNullableSetOfOneEquals(defaultValue, defaultValueJsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final java.util.Set<Boolean> borderValue1 = new java.util.HashSet<Boolean>(java.util.Arrays.asList(false));
        final StaticJson.Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final java.util.List<Boolean> deserializedTmpList = jsonSerialization.deserializeList(Boolean.class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        final java.util.Set<Boolean> borderValue1JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<Boolean>(deserializedTmpList);
        BooleanAsserts.assertNullableSetOfOneEquals(borderValue1, borderValue1JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final java.util.Set<Boolean> borderValue2 = new java.util.HashSet<Boolean>(java.util.Arrays.asList(true));
        final StaticJson.Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final java.util.List<Boolean> deserializedTmpList = jsonSerialization.deserializeList(Boolean.class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        final java.util.Set<Boolean> borderValue2JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<Boolean>(deserializedTmpList);
        BooleanAsserts.assertNullableSetOfOneEquals(borderValue2, borderValue2JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final java.util.Set<Boolean> borderValue3 = new java.util.HashSet<Boolean>(java.util.Arrays.asList(false, true));
        final StaticJson.Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        final java.util.List<Boolean> deserializedTmpList = jsonSerialization.deserializeList(Boolean.class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
        final java.util.Set<Boolean> borderValue3JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<Boolean>(deserializedTmpList);
        BooleanAsserts.assertNullableSetOfOneEquals(borderValue3, borderValue3JsonDeserialized);
    }
}
