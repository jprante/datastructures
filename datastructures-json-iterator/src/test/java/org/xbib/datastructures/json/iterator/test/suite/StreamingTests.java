package org.xbib.datastructures.json.iterator.test.suite;

import org.xbib.datastructures.json.iterator.JsonIterator;
import org.junit.BeforeClass;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Categories.class)
@Suite.SuiteClasses({AllTestCases.class})
public class StreamingTests {

    @BeforeClass
    public static void setup() {
        JsonIterator.enableStreamingSupport();
    }
}
