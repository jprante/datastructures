package com.dslplatform.json.generated.types.Integer;


import com.dslplatform.json.generated.ocd.javaasserts.IntegerAsserts;
import com.dslplatform.json.generated.types.StaticJson;
import java.io.IOException;

public class OneArrayOfNullableIntegersDefaultValueTurtle {
    private static StaticJson.JsonSerialization jsonSerialization;

    @org.junit.BeforeClass
    public static void initializeJsonSerialization() throws IOException {
        jsonSerialization = StaticJson.getSerialization();
    }

    @org.junit.Test
    public void testDefaultValueEquality() throws IOException {
        final Integer[] defaultValue = new Integer[0];
        final StaticJson.Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final Integer[] defaultValueJsonDeserialized = jsonSerialization.deserialize(Integer[].class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        IntegerAsserts.assertOneArrayOfNullableEquals(defaultValue, defaultValueJsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final Integer[] borderValue1 = new Integer[]{null};
        final StaticJson.Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final Integer[] borderValue1JsonDeserialized = jsonSerialization.deserialize(Integer[].class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        IntegerAsserts.assertOneArrayOfNullableEquals(borderValue1, borderValue1JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final Integer[] borderValue2 = new Integer[]{0};
        final StaticJson.Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final Integer[] borderValue2JsonDeserialized = jsonSerialization.deserialize(Integer[].class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        IntegerAsserts.assertOneArrayOfNullableEquals(borderValue2, borderValue2JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final Integer[] borderValue3 = new Integer[]{1000000000};
        final StaticJson.Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        final Integer[] borderValue3JsonDeserialized = jsonSerialization.deserialize(Integer[].class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
        IntegerAsserts.assertOneArrayOfNullableEquals(borderValue3, borderValue3JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue4Equality() throws IOException {
        final Integer[] borderValue4 = new Integer[]{0, Integer.MIN_VALUE, Integer.MAX_VALUE, -1000000000, 1000000000};
        final StaticJson.Bytes borderValue4JsonSerialized = jsonSerialization.serialize(borderValue4);
        final Integer[] borderValue4JsonDeserialized = jsonSerialization.deserialize(Integer[].class, borderValue4JsonSerialized.content, borderValue4JsonSerialized.length);
        IntegerAsserts.assertOneArrayOfNullableEquals(borderValue4, borderValue4JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue5Equality() throws IOException {
        final Integer[] borderValue5 = new Integer[]{null, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, -1000000000, 1000000000};
        final StaticJson.Bytes borderValue5JsonSerialized = jsonSerialization.serialize(borderValue5);
        final Integer[] borderValue5JsonDeserialized = jsonSerialization.deserialize(Integer[].class, borderValue5JsonSerialized.content, borderValue5JsonSerialized.length);
        IntegerAsserts.assertOneArrayOfNullableEquals(borderValue5, borderValue5JsonDeserialized);
    }
}
