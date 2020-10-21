package org.xbib.datastructures.tiny.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.xbib.datastructures.tiny.TinySet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.TreeSet;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 10)
@Fork(1)
@Measurement(iterations = 10)
public class CollectionRetrievalTest {

    private static final int COLLECTION_SIZE = 25_000;

    private ArrayList<Integer> arrayList;
    private LinkedList<Integer> linkedList;
    private HashSet<Integer> hashSet;
    private LinkedHashSet<Integer> linkedHashSet;
    private TreeSet<Integer> treeSet;
    private TinySet<Integer> tinySet;

    @Setup(Level.Trial)
    public void setup() {
        arrayList = new ArrayList<>();
        linkedList = new LinkedList<>();
        hashSet = new HashSet<>();
        linkedHashSet = new LinkedHashSet<>();
        treeSet = new TreeSet<>();
        TinySet.Builder<Integer> tinySetBuilder = TinySet.builder();
        for (int i = 0; i < COLLECTION_SIZE; i++) {
            arrayList.add(i);
            linkedList.add(i);
            hashSet.add(i);
            linkedHashSet.add(i);
            treeSet.add(i);
            tinySetBuilder.add(i);
        }
        tinySet = tinySetBuilder.build();
    }

    @Benchmark
    public void arrayList(Blackhole bh) {
        for (int i = 0; i < COLLECTION_SIZE; i++) {
            Integer elem = arrayList.get(i);
            bh.consume(elem);
        }
    }

    @Benchmark
    public void linkedList(Blackhole bh) {
        for (int i = 0; i < COLLECTION_SIZE; i++) {
            Integer elem = linkedList.get(i);
            bh.consume(elem);
        }
    }

    @Benchmark
    public void hashSetIterator(Blackhole bh) {
        for (Integer elem : hashSet) {
            bh.consume(elem);
        }
    }

    @Benchmark
    public void linkedHashSetIterator(Blackhole bh) {
        for (Integer elem : linkedHashSet) {
            bh.consume(elem);
        }
    }

    @Benchmark
    public void treeSetIterator(Blackhole bh) {
        for (Integer elem : treeSet) {
            bh.consume(elem);
        }
    }

    @Benchmark
    public void tinySetIterator(Blackhole bh) {
        for (Integer elem : tinySet) {
            bh.consume(elem);
        }
    }
}
