package com.dslplatform.json.generated.types.Ip;


import com.dslplatform.json.generated.ocd.javaasserts.IpAsserts;
import com.dslplatform.json.generated.ocd.test.TypeFactory;
import com.dslplatform.json.generated.types.StaticJson;
import java.io.IOException;

public class OneListOfOneIpsDefaultValueTurtle {
    private static StaticJson.JsonSerialization jsonSerialization;

    @org.junit.BeforeClass
    public static void initializeJsonSerialization() throws IOException {
        jsonSerialization = StaticJson.getSerialization();
    }

    @org.junit.Test
    public void testDefaultValueEquality() throws IOException {
        final java.util.List<java.net.InetAddress> defaultValue = new java.util.ArrayList<java.net.InetAddress>(0);
        final StaticJson.Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final java.util.List<java.net.InetAddress> defaultValueJsonDeserialized = jsonSerialization.deserializeList(java.net.InetAddress.class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        IpAsserts.assertOneListOfOneEquals(defaultValue, defaultValueJsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final java.util.List<java.net.InetAddress> borderValue1 = new java.util.ArrayList<java.net.InetAddress>(java.util.Arrays.asList(TypeFactory.buildIP("ffff::ffff")));
        final StaticJson.Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final java.util.List<java.net.InetAddress> borderValue1JsonDeserialized = jsonSerialization.deserializeList(java.net.InetAddress.class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        IpAsserts.assertOneListOfOneEquals(borderValue1, borderValue1JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final java.util.List<java.net.InetAddress> borderValue2 = new java.util.ArrayList<java.net.InetAddress>(java.util.Arrays.asList(TypeFactory.buildIP("127.0.0.1"), TypeFactory.buildIP("0"), TypeFactory.buildIP("255.255.255.255"), TypeFactory.buildIP("::1"), TypeFactory.buildIP("ffff::ffff")));
        final StaticJson.Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final java.util.List<java.net.InetAddress> borderValue2JsonDeserialized = jsonSerialization.deserializeList(java.net.InetAddress.class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        IpAsserts.assertOneListOfOneEquals(borderValue2, borderValue2JsonDeserialized);
    }
}
