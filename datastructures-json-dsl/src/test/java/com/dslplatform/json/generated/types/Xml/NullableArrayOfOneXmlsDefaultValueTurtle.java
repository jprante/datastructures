package com.dslplatform.json.generated.types.Xml;


import com.dslplatform.json.generated.ocd.javaasserts.XmlAsserts;
import com.dslplatform.json.generated.ocd.test.Utils;
import com.dslplatform.json.generated.types.StaticJson;
import java.io.IOException;

public class NullableArrayOfOneXmlsDefaultValueTurtle {
    private static StaticJson.JsonSerialization jsonSerialization;

    @org.junit.BeforeClass
    public static void initializeJsonSerialization() throws IOException {
        jsonSerialization = StaticJson.getSerialization();
    }

    @org.junit.Test
    public void testDefaultValueEquality() throws IOException {
        final org.w3c.dom.Element[] defaultValue = null;
        final StaticJson.Bytes defaultValueJsonSerialized = jsonSerialization.serialize(defaultValue);
        final org.w3c.dom.Element[] defaultValueJsonDeserialized = jsonSerialization.deserialize(org.w3c.dom.Element[].class, defaultValueJsonSerialized.content, defaultValueJsonSerialized.length);
        XmlAsserts.assertNullableArrayOfOneEquals(defaultValue, defaultValueJsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue1Equality() throws IOException {
        final org.w3c.dom.Element[] borderValue1 = new org.w3c.dom.Element[]{Utils.stringToElement("<ns3000:NamespacedElement/>")};
        final StaticJson.Bytes borderValue1JsonSerialized = jsonSerialization.serialize(borderValue1);
        final org.w3c.dom.Element[] borderValue1JsonDeserialized = jsonSerialization.deserialize(org.w3c.dom.Element[].class, borderValue1JsonSerialized.content, borderValue1JsonSerialized.length);
        XmlAsserts.assertNullableArrayOfOneEquals(borderValue1, borderValue1JsonDeserialized);
    }

    @org.junit.Test
    public void testBorderValue2Equality() throws IOException {
        final org.w3c.dom.Element[] borderValue2 = new org.w3c.dom.Element[]{Utils.stringToElement("<document/>"), Utils.stringToElement("<TextElement>some text &amp; &lt;stuff&gt;</TextElement>"), Utils.stringToElement("<ElementWithCData>&lt;?xml?&gt;&lt;xml&gt;&lt;!xml!&gt;</ElementWithCData>"), Utils.stringToElement("<AtributedElement foo=\"bar\" qwe=\"poi\"/>"), Utils.stringToElement("<NestedTextElement><FirstNest><SecondNest>bird</SecondNest></FirstNest></NestedTextElement>"), Utils.stringToElement("<ns3000:NamespacedElement/>")};
        final StaticJson.Bytes borderValue2JsonSerialized = jsonSerialization.serialize(borderValue2);
        final org.w3c.dom.Element[] borderValue2JsonDeserialized = jsonSerialization.deserialize(org.w3c.dom.Element[].class, borderValue2JsonSerialized.content, borderValue2JsonSerialized.length);
        XmlAsserts.assertNullableArrayOfOneEquals(borderValue2, borderValue2JsonDeserialized);
    }
}
