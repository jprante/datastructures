package org.xbib.datastructures.json.iterator.test.suite;

import org.xbib.datastructures.json.iterator.test.StreamingCategory;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Categories.class)
@Categories.ExcludeCategory(StreamingCategory.class)
@Suite.SuiteClasses({AllTestCases.class})
public class NonStreamingTests {

}
