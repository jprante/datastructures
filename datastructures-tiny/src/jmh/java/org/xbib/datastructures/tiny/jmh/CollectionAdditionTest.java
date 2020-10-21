package org.xbib.datastructures.tiny.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.xbib.datastructures.tiny.TinySet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 10)
@Fork(1)
@Measurement(iterations = 10)
public class CollectionAdditionTest {

    private static final int COLLECTION_SIZE = 1_000_000;

    @Benchmark
    public List<Integer> arrayList() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < COLLECTION_SIZE; i++) {
            list.add(i);
        }
        return list;
    }

    @Benchmark
    public List<Integer> linkedList() {
        List<Integer> list = new LinkedList<>();
        for (int i = 0; i < COLLECTION_SIZE; i++) {
            list.add(i);
        }
        return list;
    }

    @Benchmark
    public Set<Integer> hashSet() {
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < COLLECTION_SIZE; i++) {
            set.add(i);
        }
        return set;
    }

    @Benchmark
    public Set<Integer> linkedHashSet() {
        Set<Integer> set = new LinkedHashSet<>();
        for (int i = 0; i < COLLECTION_SIZE; i++) {
            set.add(i);
        }
        return set;
    }

    @Benchmark
    public Set<Integer> treeSet() {
        Set<Integer> set = new TreeSet<>();
        for (int i = 0; i < COLLECTION_SIZE; i++) {
            set.add(i);
        }
        return set;
    }


    @Benchmark
    public Set<Integer> tinySet() {
        TinySet.Builder<Integer> setBuilder = TinySet.builder();
        for (int i = 0; i < COLLECTION_SIZE; i++) {
            setBuilder.add(i);
        }
        return setBuilder.build();
    }
}
