package org.xbib.datastructures.io.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.xbib.datastructures.io.BufferedSeparatorInputStream;
import org.xbib.datastructures.io.FilteredSeparatorInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 5)
@Fork(1)
@Measurement(iterations = 5)
public class StreamTest {

    private static final String NAME = "/periouni.mrc";

    @Benchmark
    public long parse64() {
        InputStream in = getClass().getResourceAsStream(NAME);
        BufferedSeparatorInputStream bufferedSeparatorInputStream =
                new BufferedSeparatorInputStream(in, 64, 1024, StandardCharsets.ISO_8859_1);
        return bufferedSeparatorInputStream.stream().count();
    }

    @Benchmark
    public long parse1024() {
        InputStream in = getClass().getResourceAsStream(NAME);
        BufferedSeparatorInputStream bufferedSeparatorInputStream =
                new BufferedSeparatorInputStream(in, 1024, 1024, StandardCharsets.ISO_8859_1);
        return bufferedSeparatorInputStream.stream().count();
    }

    @Benchmark
    public long parse4096() {
        InputStream in = getClass().getResourceAsStream(NAME);
        BufferedSeparatorInputStream bufferedSeparatorInputStream =
                new BufferedSeparatorInputStream(in, 4096, 4096, StandardCharsets.ISO_8859_1);
        return bufferedSeparatorInputStream.stream().count();
    }

    @Benchmark
    public long parse8192() {
        InputStream in = getClass().getResourceAsStream(NAME);
        BufferedSeparatorInputStream bufferedSeparatorInputStream =
                new BufferedSeparatorInputStream(in, 8192, 1024, StandardCharsets.ISO_8859_1);
        return bufferedSeparatorInputStream.stream().count();
    }

    @Benchmark
    public long parse65536() {
        InputStream in = getClass().getResourceAsStream(NAME);
        BufferedSeparatorInputStream bufferedSeparatorInputStream =
                new BufferedSeparatorInputStream(in, 65536, 1024, StandardCharsets.ISO_8859_1);
        return bufferedSeparatorInputStream.stream().count();
    }

    @Benchmark
    public long parse1MB() {
        InputStream in = getClass().getResourceAsStream(NAME);
        BufferedSeparatorInputStream bufferedSeparatorInputStream =
                new BufferedSeparatorInputStream(in, 1024 * 1024, 1024, StandardCharsets.ISO_8859_1);
        return bufferedSeparatorInputStream.stream().count();
    }

    @Benchmark
    public long parseFiltered() {
        InputStream in = getClass().getResourceAsStream(NAME);
        FilteredSeparatorInputStream filteredSeparatorInputStream =
                new FilteredSeparatorInputStream(in, 1024, StandardCharsets.ISO_8859_1);
        return filteredSeparatorInputStream.stream().count();
    }
}