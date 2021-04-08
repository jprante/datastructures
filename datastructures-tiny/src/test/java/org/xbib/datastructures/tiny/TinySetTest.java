package org.xbib.datastructures.tiny;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.number.OrderingComparison.lessThan;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class TinySetTest {

    @Test
    public void testSimple() {
        TinySet.Builder<Object> builder = TinySet.builder();
        builder.add("a");
        builder.add("b");
        builder.add("c");
        assertEquals("[a, b, c]", builder.build().toString());
    }

    @Test
    public void testBuildAndGet() {
        TinySet.Builder<String> builder = TinySet.builder();
        assertThat(builder.size(), equalTo(0));
        builder.add("aaa");
        builder.add("bbb");
        builder.add("aaa");
        assertThat(builder.size(), equalTo(2));
        TinySet<String> set = builder.build();
        assertThat(set.getIndex("aaa"), equalTo(0));
        assertThat(set.getIndex("bbb"), equalTo(1));
        assertThat(set.getIndex("ccc"), lessThan(0));
        assertThat(set.size(), equalTo(2));
    }

    @Test
    public void canBuildWithDuplicateKeys() {
        TinySet.Builder<String> builder = TinySet.builder();
        builder.add("aaa");
        builder.add("aaa");
        builder.add("bbb");
        assertThat(builder.size(), equalTo(2));
        assertThat(builder.build(), equalTo(Set.of("aaa", "bbb")));
        assertThat(builder.size(), equalTo(2));
        assertThat(builder.build(), equalTo(Set.of("aaa", "bbb")));
    }

    @Test
    public void canBuildWithNull() {
        TinySet.Builder<String> builder = TinySet.builder();
        builder.add(null);
        assertThat(builder.build(), equalTo(Collections.singleton(null)));
    }

    @Test
    public void canBuildMediumWithDuplicateKeys() {
        TinySet.Builder<String> builder = TinySet.builder();
        builder.add("aaa");
        builder.add("aaa");
        for (int i = 0; i < 1000; i++) {
            builder.add("aaa" + i);
        }
        assertThat(builder.build().size(), equalTo(1001));
    }

    @Test
    public void canBuildLargeWithDuplicateKeys() {
        TinySet.Builder<String> builder = TinySet.builder();
        builder.add("aaa");
        builder.add("aaa");
        for (int i = 0; i < 0x10000; i++) {
            builder.add("aaa" + i);
        }
        assertThat(builder.build().size(), equalTo(65537));
    }

    @Test
    public void testBuildEmpty() throws Exception {
        testCount(0, false);
        testCount(0, true);
    }

    @Test
    public void testBuildMedium() throws Exception {
        testCount(1000, false);
        testCount(1000, true);
    }

    @Test
    public void testBuildLarge() throws Exception {
        testCount(0x10000, true);
    }

    @Test
    public void testBuildAlmostThere() throws Exception {
        testCount(255, false);
        testCount(255, true);
    }

    @Test
    public void testBuildSmall() throws Exception {
        testCount(123, false);
        testCount(123, true);
    }

    private void testCount(int count, boolean withNull) {
        TinySet.Builder<String> builder = TinySet.builder();
        LinkedHashSet<String> expectedSet = new LinkedHashSet<>();
        for (int i = 0; i < count; i++) {
            if (count < 1000) {
                builder.build();
            }
            builder.addAll(Collections.singleton("aaa" + i));
            expectedSet.add("aaa" + i);
        }
        if (withNull) {
            builder.add(null);
            expectedSet.add(null);
        }
        TinySet<String> set = builder.build();
        assertThat(expectedSet, CoreMatchers.is(set));
    }

    @Test
    public void immutableIsImmutable() {
        TinySet.Builder<Object> builder = TinySet.builder();
        builder.add("aaa");
        TinySet<Object> map = builder.build();
        Assertions.assertThrows(UnsupportedOperationException.class, map::clear);
        Assertions.assertThrows(UnsupportedOperationException.class, () -> map.remove("aaa"));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> map.addAll(Collections.singleton("abc")));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> map.add("abc"));
    }
}
