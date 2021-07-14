package org.xbib.datastructures.json.iterator.test.suite;

import org.xbib.datastructures.json.iterator.spi.DecodingMode;
import org.xbib.datastructures.json.iterator.JsonIterator;
import org.xbib.datastructures.json.iterator.test.StreamingCategory;
import org.xbib.datastructures.json.iterator.output.EncodingMode;
import org.xbib.datastructures.json.iterator.output.JsonStream;
import org.junit.BeforeClass;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Categories.class)
@Categories.ExcludeCategory(StreamingCategory.class)
@Suite.SuiteClasses({AllTestCases.class})
public class NonStreamingTests4Hash {
    @BeforeClass
    public static void setup() {
        JsonStream.setMode(EncodingMode.DYNAMIC_MODE);
        JsonIterator.setMode(DecodingMode.DYNAMIC_MODE_AND_MATCH_FIELD_WITH_HASH);
    }
}
