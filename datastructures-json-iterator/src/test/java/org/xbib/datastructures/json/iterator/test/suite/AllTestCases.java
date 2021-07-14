package org.xbib.datastructures.json.iterator.test.suite;

import org.xbib.datastructures.json.iterator.test.TestAnnotation;
import org.xbib.datastructures.json.iterator.test.TestAnnotationJsonCreator;
import org.xbib.datastructures.json.iterator.test.TestAnnotationJsonObject;
import org.xbib.datastructures.json.iterator.test.TestAnnotationJsonWrapper;
import org.xbib.datastructures.json.iterator.test.TestBoolean;
import org.xbib.datastructures.json.iterator.test.TestCustomizeType;
import org.xbib.datastructures.json.iterator.test.TestDemo;
import org.xbib.datastructures.json.iterator.test.TestExisting;
import org.xbib.datastructures.json.iterator.test.TestFloat;
import org.xbib.datastructures.json.iterator.test.TestGenerics;
import org.xbib.datastructures.json.iterator.test.TestGson;
import org.xbib.datastructures.json.iterator.test.TestIO;
import org.xbib.datastructures.json.iterator.test.TestNested;
import org.xbib.datastructures.json.iterator.test.TestObject;
import org.xbib.datastructures.json.iterator.test.TestOmitValue;
import org.xbib.datastructures.json.iterator.test.TestReadAny;
import org.xbib.datastructures.json.iterator.test.TestSkip;
import org.xbib.datastructures.json.iterator.test.TestSlice;
import org.xbib.datastructures.json.iterator.test.TestSpiPropertyDecoder;
import org.xbib.datastructures.json.iterator.test.TestSpiTypeDecoder;
import org.xbib.datastructures.json.iterator.test.TestString;
import org.xbib.datastructures.json.iterator.test.any.TestList;
import org.xbib.datastructures.json.iterator.test.any.TestLong;
import org.xbib.datastructures.json.iterator.test.output.TestAnnotationJsonUnwrapper;
import org.xbib.datastructures.json.iterator.test.output.TestAny;
import org.xbib.datastructures.json.iterator.test.output.TestCollection;
import org.xbib.datastructures.json.iterator.test.output.TestInteger;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.xbib.datastructures.json.iterator.test.TestWhatIsNext;
import org.xbib.datastructures.json.iterator.test.output.TestAnnotationJsonIgnore;
import org.xbib.datastructures.json.iterator.test.output.TestAnnotationJsonProperty;
import org.xbib.datastructures.json.iterator.test.output.TestArray;
import org.xbib.datastructures.json.iterator.test.output.TestJackson;
import org.xbib.datastructures.json.iterator.test.output.TestMap;
import org.xbib.datastructures.json.iterator.test.output.TestNative;
import org.xbib.datastructures.json.iterator.test.output.TestSpiPropertyEncoder;
import org.xbib.datastructures.json.iterator.test.output.TestSpiTypeEncoder;
import org.xbib.datastructures.json.iterator.test.output.TestStreamBuffer;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        org.xbib.datastructures.json.iterator.test.TestAnnotationJsonIgnore.class,
        TestAnnotationJsonIgnore.class,
        org.xbib.datastructures.json.iterator.test.TestAnnotationJsonProperty.class,
        TestAnnotationJsonProperty.class,
        TestAnnotationJsonWrapper.class,
        TestAnnotationJsonUnwrapper.class,
        TestAnnotation.class,
        TestAnnotationJsonCreator.class,
        org.xbib.datastructures.json.iterator.test.output.TestGenerics.class,
        TestCustomizeType.class, TestDemo.class,
        TestExisting.class, TestGenerics.class, TestGenerics.class, TestIO.class,
        TestNested.class,
        org.xbib.datastructures.json.iterator.test.output.TestNested.class,
        TestObject.class,
        org.xbib.datastructures.json.iterator.test.output.TestObject.class,
        TestReadAny.class, TestSkip.class, TestSlice.class,
        TestString.class,
        org.xbib.datastructures.json.iterator.test.output.TestString.class,
        TestWhatIsNext.class,
        TestAny.class,
        TestArray.class,
        org.xbib.datastructures.json.iterator.test.any.TestArray.class,
        org.xbib.datastructures.json.iterator.test.TestArray.class,
        TestSpiPropertyEncoder.class,
        org.xbib.datastructures.json.iterator.test.TestMap.class,
        TestMap.class,
        TestNative.class,
        TestBoolean.class, TestFloat.class, org.xbib.datastructures.json.iterator.test.output.TestFloat.class,
        TestList.class, TestInteger.class, TestInteger.class,
        TestJackson.class,
        org.xbib.datastructures.json.iterator.test.TestJackson.class,
        TestSpiTypeEncoder.class,
        TestSpiTypeDecoder.class,
        TestSpiPropertyDecoder.class,
        TestGson.class,
        org.xbib.datastructures.json.iterator.test.output.TestGson.class,
        TestStreamBuffer.class,
        TestCollection.class,
        TestList.class,
        TestAnnotationJsonObject.class,
        TestLong.class,
        TestOmitValue.class})
public abstract class AllTestCases {
}
