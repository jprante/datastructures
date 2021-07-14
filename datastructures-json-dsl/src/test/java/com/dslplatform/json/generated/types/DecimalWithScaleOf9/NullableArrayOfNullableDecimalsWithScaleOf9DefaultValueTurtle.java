package com.dslplatform.json.generated.types.DecimalWithScaleOf9;


import com.dslplatform.json.generated.ocd.javaasserts.DecimalWithScaleOf9Asserts;
import com.dslplatform.json.generated.types.StaticJson;
import java.io.IOException;

public class NullableArrayOfNullableDecimalsWithScaleOf9DefaultValueTurtle {
    private static StaticJson.JsonSerialization jsonSerialization;

    @org.junit.BeforeClass
    public static void initializeJsonSerialization() throws IOException {
        jsonSerialization = StaticJson.getSerialization();
    }

    @org.junit.Test
    public void testDefaultValueEquality() throws IOException {
        final java.math.BigDecimal[] defaultValue = null;
        final StaticJson.Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final java.math.BigDecimal[] defaultValueJsonDeserialized = jsonSerialization.deserialize(java.math.BigDecimal[].class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        DecimalWithScaleOf9Asserts.assertNullableArrayOfNullableEquals(defaultValue, defaultValueJsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final java.math.BigDecimal[] borderValue1 = new java.math.BigDecimal[]{null};
        final StaticJson.Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final java.math.BigDecimal[] borderValue1JsonDeserialized = jsonSerialization.deserialize(java.math.BigDecimal[].class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        DecimalWithScaleOf9Asserts.assertNullableArrayOfNullableEquals(borderValue1, borderValue1JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final java.math.BigDecimal[] borderValue2 = new java.math.BigDecimal[]{java.math.BigDecimal.ZERO.setScale(9)};
        final StaticJson.Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final java.math.BigDecimal[] borderValue2JsonDeserialized = jsonSerialization.deserialize(java.math.BigDecimal[].class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        DecimalWithScaleOf9Asserts.assertNullableArrayOfNullableEquals(borderValue2, borderValue2JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final java.math.BigDecimal[] borderValue3 = new java.math.BigDecimal[]{new java.math.BigDecimal("1E19")};
        final StaticJson.Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        final java.math.BigDecimal[] borderValue3JsonDeserialized = jsonSerialization.deserialize(java.math.BigDecimal[].class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
        DecimalWithScaleOf9Asserts.assertNullableArrayOfNullableEquals(borderValue3, borderValue3JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue4Equality() throws IOException {
        final java.math.BigDecimal[] borderValue4 = new java.math.BigDecimal[]{java.math.BigDecimal.ZERO.setScale(9), java.math.BigDecimal.ONE, new java.math.BigDecimal("3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679").setScale(9, java.math.BigDecimal.ROUND_HALF_UP), new java.math.BigDecimal("-1E-9"), new java.math.BigDecimal("1E19")};
        final StaticJson.Bytes borderValue4JsonSerialized = jsonSerialization.serialize(borderValue4);
        final java.math.BigDecimal[] borderValue4JsonDeserialized = jsonSerialization.deserialize(java.math.BigDecimal[].class, borderValue4JsonSerialized.content, borderValue4JsonSerialized.length);
        DecimalWithScaleOf9Asserts.assertNullableArrayOfNullableEquals(borderValue4, borderValue4JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue5Equality() throws IOException {
        final java.math.BigDecimal[] borderValue5 = new java.math.BigDecimal[]{null, java.math.BigDecimal.ZERO.setScale(9), java.math.BigDecimal.ONE, new java.math.BigDecimal("3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679").setScale(9, java.math.BigDecimal.ROUND_HALF_UP), new java.math.BigDecimal("-1E-9"), new java.math.BigDecimal("1E19")};
        final StaticJson.Bytes borderValue5JsonSerialized = jsonSerialization.serialize(borderValue5);
        final java.math.BigDecimal[] borderValue5JsonDeserialized = jsonSerialization.deserialize(java.math.BigDecimal[].class, borderValue5JsonSerialized.content, borderValue5JsonSerialized.length);
        DecimalWithScaleOf9Asserts.assertNullableArrayOfNullableEquals(borderValue5, borderValue5JsonDeserialized);
    }
}
