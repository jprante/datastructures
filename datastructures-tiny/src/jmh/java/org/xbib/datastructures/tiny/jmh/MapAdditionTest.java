package org.xbib.datastructures.tiny.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.xbib.datastructures.tiny.TinyMap;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 5)
@Fork(value = 1)
@Measurement(iterations = 5)
public class MapAdditionTest {

    private static final int COLLECTION_SIZE = 1_000;

    @Benchmark
    public Map<Long, String> hashMap() {
        Map<Long, String> map = new HashMap<>();
        for (long i = 0; i < COLLECTION_SIZE; i++) {
            map.put(i, String.valueOf(i));
        }
        return map;
    }

    @Benchmark
    public Map<Long, String> linkedHashMap() {
        Map<Long, String> map = new LinkedHashMap<>();
        for (long i = 0; i < COLLECTION_SIZE; i++) {
            map.put(i, String.valueOf(i));
        }
        return map;
    }

    @Benchmark
    public Map<Long, String> treeMap() {
        Map<Long, String> map = new TreeMap<>();
        for (long i = 0; i < COLLECTION_SIZE; i++) {
            map.put(i, String.valueOf(i));
        }
        return map;
    }

    @Benchmark
    public Map<Long, String> synchronizedHashMap() {
        Map<Long, String> map = new HashMap<>();
        map = Collections.synchronizedMap(map);
        for (long i = 0; i < COLLECTION_SIZE; i++) {
            map.put(i, String.valueOf(i));
        }
        return map;
    }

    @Benchmark
    public Map<Long, String> concurrentHashMap() {
        ConcurrentHashMap<Long, String> map = new ConcurrentHashMap<>();
        for (long i = 0; i < COLLECTION_SIZE; i++) {
            map.put(i, String.valueOf(i));
        }
        return map;
    }

    @Benchmark
    public Map<Long, String> tinyMap() {
        TinyMap.Builder<Long, String> map = TinyMap.<Long, String>builder();
        for (long i = 0; i < COLLECTION_SIZE; i++) {
            map.put(i, String.valueOf(i));
        }
        return map.build();
    }
}
