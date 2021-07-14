package org.xbib.datastructures.common.jmh;

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
import org.xbib.datastructures.common.StrictArrayMap;
import org.xbib.datastructures.common.StrictArraySet;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.TreeMap;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 5)
@Fork(1)
@Measurement(iterations = 5)
public class StrictBenchmark {

    Integer[] integers = new Integer[1000];

    String[] strings = new String[1000];

    @Setup(Level.Trial)
    public void setup() {
        for (int i = 0; i< 1000; i++) {
            integers[i] = i;
        }
        for (int i = 0; i< 1000; i++) {
            strings[i] = "Hello " + i;
        }
    }

    @Benchmark
    public String strictArraySet() {
        StrictArraySet<Integer> set = new StrictArraySet<>(integers);
        return set.toString();
    }

    @Benchmark
    public String strictArrayMap() {
        StrictArrayMap<Integer, String> map = new StrictArrayMap<>(integers, strings);
        return map.toString();
    }

    @Benchmark
    public String arrayList() {
        List<Integer> set = Arrays.asList(integers);
        return set.toString();
    }

    @Benchmark
    public String linkedHashSetFromArrayList() {
        LinkedHashSet<Integer> set = new LinkedHashSet<>(Arrays.asList(integers));
        return set.toString();
    }

    @Benchmark
    public String linkedHashSet() {
        LinkedHashSet<Integer> set = new LinkedHashSet<>(Arrays.asList(integers).subList(0, 1000));
        return set.toString();
    }

    @Benchmark
    public String linkedHashMap() {
        LinkedHashMap<Integer, String> map = new LinkedHashMap<>();
        for (int i = 0; i < 1000; i++) {
            map.put(integers[i], strings[i]);
        }
        return map.toString();
    }

    @Benchmark
    public String hashMap() {
        HashMap<Integer, String> map = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            map.put(integers[i], strings[i]);
        }
        return map.toString();
    }

    @Benchmark
    public String treeMap() {
        TreeMap<Integer, String> map = new TreeMap<>();
        for (int i = 0; i < 1000; i++) {
            map.put(integers[i], strings[i]);
        }
        return map.toString();
    }
}
