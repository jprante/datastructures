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
import org.xbib.datastructures.tiny.TinyMap;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 10)
@Fork(1)
@Measurement(iterations = 10)
public class MapRetrievalTest {

    private static final int COLLECTION_SIZE = 25_000;

    private HashMap<Long, String> hashMap;
    private LinkedHashMap<Long, String> linkedHashMap;
    private TreeMap<Long, String> treeMap;
    private ConcurrentHashMap<Long, String> concurrentHashMap;
    private Map<Long, String> synchronizedHashMap;
    private TinyMap<Long, String> tinyMap;

    @Setup(Level.Trial)
    public void setup() {
        hashMap = new HashMap<>();
        linkedHashMap = new LinkedHashMap<>();
        treeMap = new TreeMap<>();
        concurrentHashMap = new ConcurrentHashMap<>();
        synchronizedHashMap = new HashMap<>();
        TinyMap.Builder<Long, String> tinyMapBuilder = TinyMap.builder();
        for (long i = 0; i < COLLECTION_SIZE; i++) {
            hashMap.put(i, String.valueOf(i));
            linkedHashMap.put(i, String.valueOf(i));
            treeMap.put(i, String.valueOf(i));
            concurrentHashMap.put(i, String.valueOf(i));
            synchronizedHashMap.put(i, String.valueOf(i));
            tinyMapBuilder.put(i, String.valueOf(i));
        }
        synchronizedHashMap = Collections.synchronizedMap(synchronizedHashMap);
        tinyMap = tinyMapBuilder.build();
    }

    @Benchmark
    public void hashMap(Blackhole bh) {
        for (long i = 0; i < COLLECTION_SIZE; i++) {
            String s = hashMap.get(i);
            bh.consume(s);
        }
    }

    @Benchmark
    public void linkedHashMap(Blackhole bh) {
        for (long i = 0; i < COLLECTION_SIZE; i++) {
            String s = linkedHashMap.get(i);
            bh.consume(s);
        }
    }

    @Benchmark
    public void treeMap(Blackhole bh) {
        for (long i = 0; i < COLLECTION_SIZE; i++) {
            String s = treeMap.get(i);
            bh.consume(s);
        }
    }

    @Benchmark
    public void synchronizedHashMap(Blackhole bh) {
        for (long i = 0; i < COLLECTION_SIZE; i++) {
            String s = synchronizedHashMap.get(i);
            bh.consume(s);
        }
    }

    @Benchmark
    public void concurrentHashMap(Blackhole bh) {
        for (long i = 0; i < COLLECTION_SIZE; i++) {
            String s = concurrentHashMap.get(i);
            bh.consume(s);
        }
    }

    @Benchmark
    public void tinyhMap(Blackhole bh) {
        for (long i = 0; i < COLLECTION_SIZE; i++) {
            String s = tinyMap.get(i);
            bh.consume(s);
        }
    }
}
