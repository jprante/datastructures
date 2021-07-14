package org.xbib.datastructures.tiny;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TinyListTest {

    @Test
    public void testSimple() {
        TinyList.Builder<Object> builder = TinyList.builder();
        builder.add("a");
        builder.add("b");
        builder.add("c");
        assertEquals("[a, b, c]", builder.build().toString());
    }

    @Test
    public void testIndexOfAndLastIndexOf() {
        TinyList.Builder<String> builder = TinyList.builder();
        builder.add("aaa");
        builder.add("bbb");
        builder.add("bbb");
        builder.add("bbb");
        builder.add("ccc");
        assertThat(builder.indexOf("bbb"), equalTo(builder.build().indexOf("bbb")));
        assertThat(builder.indexOf("bbb"), equalTo(1));
        assertThat(builder.lastIndexOf("bbb"), equalTo(builder.build().lastIndexOf("bbb")));
        assertThat(builder.lastIndexOf("bbb"), equalTo(3));
        assertThat(builder.indexOf("xxx"), equalTo(builder.build().indexOf("xxx")));
        assertThat(builder.indexOf("xxx"), equalTo(-1));
        assertThat(builder.lastIndexOf("xxx"), equalTo(builder.build().lastIndexOf("xxx")));
        assertThat(builder.lastIndexOf("xxx"), equalTo(-1));
    }

    @Test
    public void testBuildAndGet() {
        TinyList.Builder<Object> builder = TinyList.builder();
        assertThat(builder.size(), equalTo(0));
        builder.add("aaa");
        builder.add(123);
        assertThat(builder.size(), equalTo(2));
        List<Object> list = builder.build();
        assertThat(list.get(0), equalTo("aaa"));
        assertThat(list.get(1), equalTo(123));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> list.get(2));
        assertThat(list.size(), equalTo(2));
        assertTrue(list.containsAll(List.of("aaa", 123)));
    }

    @Test
    public void testBuildEmpty() {
        testCount(0);
    }

    @Test
    public void testBuildGiant() {
        testCount(1000);
    }

    @Test
    public void testBuildAlmostThere() {
        testCount(255);
    }

    private void testCount(int count) {
        TinyList.Builder<Object> builder = TinyList.builder();
        List<String> expected = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            builder.add("aaa" + i);
            expected.add("aaa" + i);
        }
        TinyList<Object> list = builder.build();
        assertThat(list.size(), equalTo(expected.size()));
        for (int i = 0; i < expected.size(); i++) {
            assertThat(list.get(i), equalTo(expected.get(i)));
        }
        assertThat(list.toString(), equalTo(expected.toString()));
        assertThat(list, equalTo(expected));
        assertThat(expected, equalTo(list));
        assertThat(list.hashCode(), equalTo(expected.hashCode()));
    }

    @Test
    public void immutableIsImmutable() {
        TinyList.Builder<Object> builder = TinyList.builder();
        builder.add("aaa");
        TinyList<Object> map = builder.build();
        Assertions.assertThrows(UnsupportedOperationException.class, () -> map.clear());
        Assertions.assertThrows(UnsupportedOperationException.class, () -> map.remove("aaa"));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> map.addAll(Collections.singleton("abc")));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> map.add("abc"));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> map.set(42, "abc"));
    }
}
