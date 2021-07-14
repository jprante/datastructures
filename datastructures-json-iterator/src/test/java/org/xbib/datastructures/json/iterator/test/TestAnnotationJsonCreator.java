package org.xbib.datastructures.json.iterator.test;

import org.xbib.datastructures.json.iterator.JsonIterator;
import org.xbib.datastructures.json.iterator.annotation.JsonCreator;
import org.xbib.datastructures.json.iterator.annotation.JsonIgnore;
import org.xbib.datastructures.json.iterator.annotation.JsonProperty;
import org.xbib.datastructures.json.iterator.annotation.JsonWrapper;
import org.xbib.datastructures.json.iterator.any.Any;
import junit.framework.TestCase;

import java.io.IOException;
import java.util.Properties;

public class TestAnnotationJsonCreator extends TestCase {


    public static class TestObject2 {
        private int field1;

        @JsonCreator
        public TestObject2(@JsonProperty("field1") int field1) {
            this.field1 = field1;
        }
    }

    public void test_ctor() throws IOException {
        JsonIterator iter = JsonIterator.parse("{'field1': 100}".replace('\'', '"'));
        TestObject2 obj = iter.read(TestObject2.class);
        assertEquals(100, obj.field1);
    }

    public static class TestObject {

        @JsonIgnore
        private final String id;
        @JsonIgnore
        private final Properties properties;

        @JsonCreator
        public TestObject(@JsonProperty("name") final String name) {
            this.id = name;
            properties = new Properties();
        }

        @JsonWrapper
        public void setProperties(@JsonProperty("props") final Any props) {
            // Set props
        }
    }

    public void test_ctor_and_setter_binding() throws IOException {
        JsonIterator iter = JsonIterator.parse("{\"name\": \"test\", \"props\": {\"val\": \"42\"}}");
        iter.read(TestObject.class);
    }
}
