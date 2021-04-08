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
@Warmup(iterations = 5)
@Fork(1)
@Measurement(iterations = 5)
public class CollectionRemovalTest {

    private static final int COLLECTION_SIZE = 1_000;

    private ArrayList<Integer> arrayList;
    private LinkedList<Integer> linkedList;
    private HashSet<Integer> hashSet;
    private LinkedHashSet<Integer> linkedHashSet;
    private TreeSet<Integer> treeSet;
    private TinySet.Builder<Integer> tinySet;

    @Setup(Level.Trial)
    public void setup() {
        arrayList = new ArrayList<>();
        linkedList = new LinkedList<>();
        hashSet = new HashSet<>();
        linkedHashSet = new LinkedHashSet<>();
        treeSet = new TreeSet<>();
        tinySet = TinySet.builder();
        for (int i = 0; i < COLLECTION_SIZE; i++) {
            arrayList.add(i);
            linkedList.add(i);
            hashSet.add(i);
            linkedHashSet.add(i);
            treeSet.add(i);
            tinySet.add(i);
        }
    }

    @Benchmark
    public void arrayList(Blackhole bh) {
        for (int i = 0; i < COLLECTION_SIZE; i++) {
            Integer remove = arrayList.remove(i);
        }
    }

    @Benchmark
    public void linkedList(Blackhole bh) {
        for (int i = 0; i < COLLECTION_SIZE; i++) {
            Integer remove = linkedList.remove(i);
        }
    }

    @Benchmark
    public void hashSet(Blackhole bh) {
        for (int i = 0; i < COLLECTION_SIZE; i++) {
            boolean remove = hashSet.remove(i);
        }
    }

    @Benchmark
    public void linkedHashSet(Blackhole bh) {
        for (int i = 0; i < COLLECTION_SIZE; i++) {
            boolean remove = linkedHashSet.remove(i);
        }
    }

    @Benchmark
    public void treeSet(Blackhole bh) {
        for (int i = 0; i < COLLECTION_SIZE; i++) {
            boolean remove = treeSet.remove(i);
        }
    }

    @Benchmark
    public void tinySet(Blackhole bh) {
        for (int i = 0; i < COLLECTION_SIZE; i++) {
            boolean remove = tinySet.remove(i);
        }
    }
}
