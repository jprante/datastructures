package org.xbib.datastructures.json.iterator.test.output;

import org.xbib.datastructures.json.iterator.output.JsonStream;
import org.xbib.datastructures.json.iterator.spi.Encoder;
import org.xbib.datastructures.json.iterator.spi.JsoniterSpi;
import org.xbib.datastructures.json.iterator.spi.TypeLiteral;
import junit.framework.TestCase;
import java.io.IOException;

public class TestSpiPropertyEncoder extends TestCase {

    public static class TestObject1<A> {
        public String field1;
    }

    public void test_PropertyEncoder() throws IOException {
        JsoniterSpi.registerPropertyEncoder(TestObject1.class, "field1", new Encoder() {
            @Override
            public void encode(Object obj, JsonStream stream) throws IOException {
                String str = (String) obj;
                stream.writeVal(Integer.valueOf(str));
            }
        });
        TestObject1 obj = new TestObject1();
        obj.field1 = "100";
        String output = JsonStream.serialize(obj);
        assertEquals("{'field1':100}".replace('\'', '"'), output);
    }

    public void test_PropertyEncoder_for_type_literal() throws IOException {
        TypeLiteral<TestObject1<Object>> typeLiteral = new TypeLiteral<TestObject1<Object>>() {
        };
        JsoniterSpi.registerPropertyEncoder(typeLiteral, "field1", new Encoder() {
            @Override
            public void encode(Object obj, JsonStream stream) throws IOException {
                String str = (String) obj;
                stream.writeVal(Integer.valueOf(str) + 1);
            }
        });
        TestObject1 obj = new TestObject1();
        obj.field1 = "100";
        String output = JsonStream.serialize(typeLiteral, obj);
        assertEquals("{'field1':101}".replace('\'', '"'), output);
    }
}
