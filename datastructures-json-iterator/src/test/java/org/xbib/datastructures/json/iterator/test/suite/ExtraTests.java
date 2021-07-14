package org.xbib.datastructures.json.iterator.test.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.xbib.datastructures.json.iterator.test.extra.TestBase64;
import org.xbib.datastructures.json.iterator.test.extra.TestNamingStrategy;
import org.xbib.datastructures.json.iterator.test.extra.TestPreciseFloat;

@RunWith(Suite.class)
@Suite.SuiteClasses({TestBase64.class, TestNamingStrategy.class, TestPreciseFloat.class})
public class ExtraTests {

}
