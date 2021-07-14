package com.dslplatform.json.generated.types.Ip;


import com.dslplatform.json.generated.ocd.javaasserts.IpAsserts;
import com.dslplatform.json.generated.ocd.test.TypeFactory;
import com.dslplatform.json.generated.types.StaticJson;
import java.io.IOException;

public class OneArrayOfNullableIpsDefaultValueTurtle {
    private static StaticJson.JsonSerialization jsonSerialization;

    @org.junit.BeforeClass
    public static void initializeJsonSerialization() throws IOException {
        jsonSerialization = StaticJson.getSerialization();
    }

    @org.junit.Test
    public void testDefaultValueEquality() throws IOException {
        final java.net.InetAddress[] defaultValue = new java.net.InetAddress[0];
        final StaticJson.Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final java.net.InetAddress[] defaultValueJsonDeserialized = jsonSerialization.deserialize(java.net.InetAddress[].class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        IpAsserts.assertOneArrayOfNullableEquals(defaultValue, defaultValueJsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final java.net.InetAddress[] borderValue1 = new java.net.InetAddress[]{null};
        final StaticJson.Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final java.net.InetAddress[] borderValue1JsonDeserialized = jsonSerialization.deserialize(java.net.InetAddress[].class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        IpAsserts.assertOneArrayOfNullableEquals(borderValue1, borderValue1JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final java.net.InetAddress[] borderValue2 = new java.net.InetAddress[]{TypeFactory.buildIP("ffff::ffff")};
        final StaticJson.Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final java.net.InetAddress[] borderValue2JsonDeserialized = jsonSerialization.deserialize(java.net.InetAddress[].class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        IpAsserts.assertOneArrayOfNullableEquals(borderValue2, borderValue2JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue3Equality() throws IOException {
        final java.net.InetAddress[] borderValue3 = new java.net.InetAddress[]{TypeFactory.buildIP("127.0.0.1"), TypeFactory.buildIP("0"), TypeFactory.buildIP("255.255.255.255"), TypeFactory.buildIP("::1"), TypeFactory.buildIP("ffff::ffff")};
        final StaticJson.Bytes borderValue3JsonSerialized = jsonSerialization.serialize(borderValue3);
        final java.net.InetAddress[] borderValue3JsonDeserialized = jsonSerialization.deserialize(java.net.InetAddress[].class, borderValue3JsonSerialized.content, borderValue3JsonSerialized.length);
        IpAsserts.assertOneArrayOfNullableEquals(borderValue3, borderValue3JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue4Equality() throws IOException {
        final java.net.InetAddress[] borderValue4 = new java.net.InetAddress[]{null, TypeFactory.buildIP("127.0.0.1"), TypeFactory.buildIP("0"), TypeFactory.buildIP("255.255.255.255"), TypeFactory.buildIP("::1"), TypeFactory.buildIP("ffff::ffff")};
        final StaticJson.Bytes borderValue4JsonSerialized = jsonSerialization.serialize(borderValue4);
        final java.net.InetAddress[] borderValue4JsonDeserialized = jsonSerialization.deserialize(java.net.InetAddress[].class, borderValue4JsonSerialized.content, borderValue4JsonSerialized.length);
        IpAsserts.assertOneArrayOfNullableEquals(borderValue4, borderValue4JsonDeserialized);
    }
}
