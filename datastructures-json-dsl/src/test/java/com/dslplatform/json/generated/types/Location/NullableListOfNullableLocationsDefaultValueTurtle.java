package com.dslplatform.json.generated.types.Location;


import com.dslplatform.json.generated.ocd.javaasserts.LocationAsserts;
import com.dslplatform.json.generated.types.StaticJson;
import java.io.IOException;

public class NullableListOfNullableLocationsDefaultValueTurtle {
    private static StaticJson.JsonSerialization jsonSerialization;

    @org.junit.BeforeClass
    public static void initializeJsonSerialization() throws IOException {
        jsonSerialization = StaticJson.getSerialization();
    }

    @org.junit.Test
    public void testDefaultValueEquality() throws IOException {
        final java.util.List<java.awt.geom.Point2D> defaultValue = null;
        final StaticJson.Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final java.util.List<java.awt.geom.Point2D> defaultValueJsonDeserialized = jsonSerialization.deserializeList(java.awt.geom.Point2D.class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        LocationAsserts.assertNullableListOfNullableEquals(defaultValue, defaultValueJsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final java.util.List<java.awt.geom.Point2D> borderValue1 = new java.util.ArrayList<java.awt.geom.Point2D>(java.util.Arrays.asList((java.awt.geom.Point2D) null));
        final StaticJson.Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final java.util.List<java.awt.geom.Point2D> borderValue1JsonDeserialized = jsonSerialization.deserializeList(java.awt.geom.Point2D.class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        LocationAsserts.assertNullableListOfNullableEquals(borderValue1, borderValue1JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final java.util.List<java.awt.geom.Point2D> borderValue2 = new java.util.ArrayList<java.awt.geom.Point2D>(java.util.Arrays.asList(new java.awt.geom.Point2D.Float()));
        final StaticJson.Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final java.util.List<java.awt.geom.Point2D> borderValue2JsonDeserialized = jsonSerialization.deserializeList(java.awt.geom.Point2D.class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        LocationAsserts.assertNullableListOfNullableEquals(borderValue2, borderValue2JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final java.util.List<java.awt.geom.Point2D> borderValue3 = new java.util.ArrayList<java.awt.geom.Point2D>(java.util.Arrays.asList(new java.awt.geom.Point2D.Double(-1.000000000000001, 1.000000000000001)));
        final StaticJson.Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        final java.util.List<java.awt.geom.Point2D> borderValue3JsonDeserialized = jsonSerialization.deserializeList(java.awt.geom.Point2D.class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
        LocationAsserts.assertNullableListOfNullableEquals(borderValue3, borderValue3JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue4Equality() throws IOException {
        final java.util.List<java.awt.geom.Point2D> borderValue4 = new java.util.ArrayList<java.awt.geom.Point2D>(java.util.Arrays.asList(new java.awt.geom.Point2D.Float(), new java.awt.Point(Integer.MIN_VALUE, Integer.MAX_VALUE), new java.awt.Point(-1000000000, 1000000000), new java.awt.geom.Point2D.Float(Float.MIN_VALUE, Float.MAX_VALUE), new java.awt.geom.Point2D.Float(-1.0000001f, 1.0000001f), new java.awt.geom.Point2D.Double(Double.MIN_VALUE, Double.MAX_VALUE), new java.awt.geom.Point2D.Double(-1.000000000000001, 1.000000000000001)));
        final StaticJson.Bytes borderValue4JsonSerialized = jsonSerialization.serialize(borderValue4);
        final java.util.List<java.awt.geom.Point2D> borderValue4JsonDeserialized = jsonSerialization.deserializeList(java.awt.geom.Point2D.class, borderValue4JsonSerialized.content, borderValue4JsonSerialized.length);
        LocationAsserts.assertNullableListOfNullableEquals(borderValue4, borderValue4JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue5Equality() throws IOException {
        final java.util.List<java.awt.geom.Point2D> borderValue5 = new java.util.ArrayList<java.awt.geom.Point2D>(java.util.Arrays.asList((java.awt.geom.Point2D) null, new java.awt.geom.Point2D.Float(), new java.awt.Point(Integer.MIN_VALUE, Integer.MAX_VALUE), new java.awt.Point(-1000000000, 1000000000), new java.awt.geom.Point2D.Float(Float.MIN_VALUE, Float.MAX_VALUE), new java.awt.geom.Point2D.Float(-1.0000001f, 1.0000001f), new java.awt.geom.Point2D.Double(Double.MIN_VALUE, Double.MAX_VALUE), new java.awt.geom.Point2D.Double(-1.000000000000001, 1.000000000000001)));
        final StaticJson.Bytes borderValue5JsonSerialized = jsonSerialization.serialize(borderValue5);
        final java.util.List<java.awt.geom.Point2D> borderValue5JsonDeserialized = jsonSerialization.deserializeList(java.awt.geom.Point2D.class, borderValue5JsonSerialized.content, borderValue5JsonSerialized.length);
        LocationAsserts.assertNullableListOfNullableEquals(borderValue5, borderValue5JsonDeserialized);
    }
}
