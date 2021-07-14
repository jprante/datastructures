package org.xbib.datastructures.benchmark;

import com.dslplatform.json.DslJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.xbib.datastructures.json.flat.Json;
import org.xbib.datastructures.json.noggit.ObjectBuilder;
import org.xbib.datastructures.json.simple.JSONParser;
import org.xbib.datastructures.json.simple.ParseException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonTest {

    private static final Logger logger = Logger.getLogger(JsonTest.class.getName());

    private static String input;

    @BeforeAll
    public static void setup() throws IOException {
        try (InputStream inputStream = JsonTest.class.getResourceAsStream("small.json")) {
            if (inputStream != null) {
                byte[] b = inputStream.readAllBytes();
                input = new String(b, StandardCharsets.UTF_8);
            }
        }
    }

    @Test
    public void testMap() throws IOException, ParseException {
        logger.log(Level.INFO, "noggit = " + ObjectBuilder.fromJSON(input).toString());
        logger.log(Level.INFO, "simple = " + new JSONParser().parse(input).toString()); // JSON
        logger.log(Level.INFO, "orgjson = " + new org.json.JSONObject(input).toString()); // JSON
        logger.log(Level.INFO, "gson = " + new Gson().fromJson(input, Map.class).toString());
        logger.log(Level.INFO, "jackson = " + new ObjectMapper().readValue(input, Map.class).toString());
        logger.log(Level.INFO, "minimal = " + org.xbib.datastructures.json.minimal.Json.parse(input).asObject().toString()); // JSON
        logger.log(Level.INFO, "flat = " + Json.parse(input).asMap().toString()); // JSON
        logger.log(Level.INFO, "jsoniter = " + org.xbib.datastructures.json.iterator.JsonIterator.deserialize(input).asMap().toString());  // JSON
        byte[] b = input.getBytes(StandardCharsets.UTF_8);
        logger.log(Level.INFO, "jsondsl = " + new DslJson<>().deserialize(Map.class, b, b.length));
    }
}
