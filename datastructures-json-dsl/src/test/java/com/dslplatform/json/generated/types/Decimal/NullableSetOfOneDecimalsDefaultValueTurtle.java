package com.dslplatform.json.generated.types.Decimal;


import com.dslplatform.json.generated.ocd.javaasserts.DecimalAsserts;
import com.dslplatform.json.generated.types.StaticJson;
import java.io.IOException;

public class NullableSetOfOneDecimalsDefaultValueTurtle {
    private static StaticJson.JsonSerialization jsonSerialization;

    @org.junit.BeforeClass
    public static void initializeJsonSerialization() throws IOException {
        jsonSerialization = StaticJson.getSerialization();
    }

    @org.junit.Test
    public void testDefaultValueEquality() throws IOException {
        final java.util.Set<java.math.BigDecimal> defaultValue = null;
        final StaticJson.Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final java.util.List<java.math.BigDecimal> deserializedTmpList = jsonSerialization.deserializeList(java.math.BigDecimal.class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        final java.util.Set<java.math.BigDecimal> defaultValueJsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<java.math.BigDecimal>(deserializedTmpList);
        DecimalAsserts.assertNullableSetOfOneEquals(defaultValue, defaultValueJsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final java.util.Set<java.math.BigDecimal> borderValue1 = new java.util.HashSet<java.math.BigDecimal>(java.util.Arrays.asList(java.math.BigDecimal.ZERO));
        final StaticJson.Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final java.util.List<java.math.BigDecimal> deserializedTmpList = jsonSerialization.deserializeList(java.math.BigDecimal.class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        final java.util.Set<java.math.BigDecimal> borderValue1JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<java.math.BigDecimal>(deserializedTmpList);
        DecimalAsserts.assertNullableSetOfOneEquals(borderValue1, borderValue1JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final java.util.Set<java.math.BigDecimal> borderValue2 = new java.util.HashSet<java.math.BigDecimal>(java.util.Arrays.asList(new java.math.BigDecimal("1E28")));
        final StaticJson.Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final java.util.List<java.math.BigDecimal> deserializedTmpList = jsonSerialization.deserializeList(java.math.BigDecimal.class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        final java.util.Set<java.math.BigDecimal> borderValue2JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<java.math.BigDecimal>(deserializedTmpList);
        DecimalAsserts.assertNullableSetOfOneEquals(borderValue2, borderValue2JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final java.util.Set<java.math.BigDecimal> borderValue3 = new java.util.HashSet<java.math.BigDecimal>(java.util.Arrays.asList(java.math.BigDecimal.ZERO, java.math.BigDecimal.ONE, new java.math.BigDecimal("3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679").setScale(28, java.math.BigDecimal.ROUND_HALF_UP), new java.math.BigDecimal("-1E-28"), new java.math.BigDecimal("1E28")));
        final StaticJson.Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        final java.util.List<java.math.BigDecimal> deserializedTmpList = jsonSerialization.deserializeList(java.math.BigDecimal.class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
        final java.util.Set<java.math.BigDecimal> borderValue3JsonDeserialized = deserializedTmpList == null ? null : new java.util.HashSet<java.math.BigDecimal>(deserializedTmpList);
        DecimalAsserts.assertNullableSetOfOneEquals(borderValue3, borderValue3JsonDeserialized);
    }
}
