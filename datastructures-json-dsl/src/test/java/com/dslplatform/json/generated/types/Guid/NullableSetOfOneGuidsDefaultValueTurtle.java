package com.dslplatform.json.generated.types.Guid;


import com.dslplatform.json.generated.ocd.javaasserts.GuidAsserts;
import com.dslplatform.json.generated.types.StaticJson;
import java.io.IOException;

public class NullableSetOfOneGuidsDefaultValueTurtle {
    private static StaticJson.JsonSerialization jsonSerialization;

    @org.junit.BeforeClass
    public static void initializeJsonSerialization() throws IOException {
        jsonSerialization = StaticJson.getSerialization();
    }

    @org.junit.Test
    public void testDefaultValueEquality() throws IOException {
        final java.util.Set<java.util.UUID> defaultValue = null;
        final StaticJson.Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final java.util.List<java.util.UUID> deserializedTmpList = jsonSerialization.deserializeList(java.util.UUID.class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        final java.util.Set<java.util.UUID> defaultValueJsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<java.util.UUID>(deserializedTmpList);
        GuidAsserts.assertNullableSetOfOneEquals(defaultValue, defaultValueJsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final java.util.Set<java.util.UUID> borderValue1 = new java.util.HashSet<java.util.UUID>(java.util.Arrays.asList(java.util.UUID.randomUUID()));
        final StaticJson.Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final java.util.List<java.util.UUID> deserializedTmpList = jsonSerialization.deserializeList(java.util.UUID.class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        final java.util.Set<java.util.UUID> borderValue1JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<java.util.UUID>(deserializedTmpList);
        GuidAsserts.assertNullableSetOfOneEquals(borderValue1, borderValue1JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final java.util.Set<java.util.UUID> borderValue2 = new java.util.HashSet<java.util.UUID>(java.util.Arrays.asList(new java.util.UUID(0L, 0L)));
        final StaticJson.Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final java.util.List<java.util.UUID> deserializedTmpList = jsonSerialization.deserializeList(java.util.UUID.class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        final java.util.Set<java.util.UUID> borderValue2JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<java.util.UUID>(deserializedTmpList);
        GuidAsserts.assertNullableSetOfOneEquals(borderValue2, borderValue2JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final java.util.Set<java.util.UUID> borderValue3 = new java.util.HashSet<java.util.UUID>(java.util.Arrays.asList(java.util.UUID.randomUUID(), java.util.UUID.fromString("1-2-3-4-5"), new java.util.UUID(0L, 0L)));
        final StaticJson.Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        final java.util.List<java.util.UUID> deserializedTmpList = jsonSerialization.deserializeList(java.util.UUID.class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
        final java.util.Set<java.util.UUID> borderValue3JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<java.util.UUID>(deserializedTmpList);
        GuidAsserts.assertNullableSetOfOneEquals(borderValue3, borderValue3JsonDeserialized);
    }
}
