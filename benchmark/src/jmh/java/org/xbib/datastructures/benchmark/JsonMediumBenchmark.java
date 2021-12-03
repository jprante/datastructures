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
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.annotations.Timeout;
import org.xbib.datastructures.json.tiny.EmptyJsonListener;
import org.xbib.datastructures.json.tiny.StandardJsonListener;
import org.xbib.datastructures.json.tiny.StringParser;
import org.xbib.datastructures.json.tiny.TinyJsonListener;
import org.xbib.datastructures.json.flat.Json;
import org.xbib.datastructures.json.noggit.ObjectBuilder;
import org.xbib.datastructures.json.simple.JSONParser;
import org.xbib.datastructures.json.simple.ParseException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
@Fork(value = 1, jvmArgsAppend = "-Xmx512m")
@Threads(4)
@Timeout(time = 10, timeUnit = TimeUnit.MINUTES)
public class JsonMediumBenchmark {

    private String mediumInput;

    private Gson gson;

    private ObjectMapper objectMapper;

    private DslJson<?> dslJson;

    @Setup(Level.Trial)
    public void setup() throws IOException {
        try (InputStream inputStream = JsonMediumBenchmark.class.getResourceAsStream("medium.json")) {
            if (inputStream != null) {
                byte[] b = inputStream.readAllBytes();
                this.mediumInput = new String(b, StandardCharsets.UTF_8);
            }
        }
        gson = new Gson();
        objectMapper = new ObjectMapper();
        dslJson = new DslJson<>();
    }

    @Benchmark
    public Object noggit() throws IOException {
        return ObjectBuilder.fromJSON(mediumInput).toString();
    }

    @Benchmark
    public Object simple() throws ParseException {
        return new JSONParser().parse(mediumInput).toString();
    }

    @Benchmark
    public Object orgjson() {
        return new org.json.JSONObject(mediumInput).toString();
    }

    @Benchmark
    public Object gson() {
        return gson.fromJson(mediumInput, Map.class).toString();
    }

    @Benchmark
    public Object jackson() throws IOException {
        return objectMapper.readValue(mediumInput, Map.class).toString();
    }

    @Benchmark
    public Object minimal() throws IOException {
        return org.xbib.datastructures.json.minimal.Json.parse(mediumInput).asObject().toString();
    }

    @Benchmark
    public Object flat() throws IOException {
        return Json.parse(mediumInput).asMap().toString();
    }

    @Benchmark
    public Object jsoniter() {
        return org.xbib.datastructures.json.iterator.JsonIterator.deserialize(mediumInput).asMap().toString();
    }

    @Benchmark
    public Object jsondsl() throws IOException {
        byte[] b = mediumInput.getBytes(StandardCharsets.UTF_8);
        return dslJson.deserialize(Map.class, b, b.length);
    }

    @Benchmark
    public Object datastructuresJsonTiny() {
        return org.xbib.datastructures.json.tiny.Json.toMap(mediumInput).toString();
    }
}
