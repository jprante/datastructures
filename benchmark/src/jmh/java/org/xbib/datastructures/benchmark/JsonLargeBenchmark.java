package org.xbib.datastructures.benchmark;

import com.dslplatform.json.DslJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Timeout;
import org.openjdk.jmh.annotations.Warmup;
import org.xbib.datastructures.json.EmptyJsonListener;
import org.xbib.datastructures.json.StandardJsonListener;
import org.xbib.datastructures.json.StringParser;
import org.xbib.datastructures.json.TinyJsonListener;
import org.xbib.datastructures.json.flat.Json;
import org.xbib.datastructures.json.noggit.ObjectBuilder;
import org.xbib.datastructures.json.simple.JSONParser;
import org.xbib.datastructures.json.simple.ParseException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
@Fork(value = 1, jvmArgsAppend = "-Xmx1024m")
@Threads(4)
@Timeout(time = 10, timeUnit = TimeUnit.MINUTES)
public class JsonLargeBenchmark {

    private String largeInput;

    private Gson gson;

    private ObjectMapper objectMapper;

    private DslJson<?> dslJson;

    @Setup(Level.Trial)
    public void setup() throws IOException {
        try (InputStream inputStream = JsonLargeBenchmark.class.getResourceAsStream("large.json")) {
            if (inputStream != null) {
                byte[] b = inputStream.readAllBytes();
                this.largeInput = new String(b, StandardCharsets.UTF_8);
            }
        }
        gson = new Gson();
        objectMapper = new ObjectMapper();
        dslJson = new DslJson<>();
    }

    @Benchmark
    public Object noggit() throws IOException {
        return ObjectBuilder.fromJSON(largeInput).toString();
    }

    @Benchmark
    public Object simple() throws ParseException {
        return new JSONParser().parse(largeInput).toString();
    }

    @Benchmark
    public Object orgjson() {
        return new org.json.JSONArray(largeInput).toString();
    }

    @Benchmark
    public Object gson() {
        return gson.fromJson(largeInput, List.class).toString();
    }

    @Benchmark
    public Object jackson() throws IOException {
        return objectMapper.readValue(largeInput, List.class).toString();
    }

    @Benchmark
    public Object minimal() throws IOException {
        return org.xbib.datastructures.json.minimal.Json.parse(largeInput).asArray().toString();
    }

    @Benchmark
    public Object flat() throws IOException {
        return Json.parse(largeInput).asList().toString();
    }

    @Benchmark
    public Object jsoniter() {
        return org.xbib.datastructures.json.iterator.JsonIterator.deserialize(largeInput).asList().toString();
    }

    @Benchmark
    public Object jsondsl() throws IOException {
        byte[] b = largeInput.getBytes(StandardCharsets.UTF_8);
        return dslJson.deserialize(Map.class, b, b.length);
    }

    @Benchmark
    public Object datastructuresEmpty() {
        StringParser stringParser = new StringParser(new EmptyJsonListener());
        stringParser.parse(largeInput);
        return stringParser.getNode(); // there is no object in get()
    }

    @Benchmark
    public Object datastructuresTiny() {
        StringParser stringParser = new StringParser(new TinyJsonListener());
        stringParser.parse(largeInput);
        return stringParser.getNode().get().toString();
    }

    @Benchmark
    public Object datastructuresStandard() {
        StringParser stringParser = new StringParser(new StandardJsonListener());
        stringParser.parse(largeInput);
        return stringParser.getNode().get().toString();
    }
}
