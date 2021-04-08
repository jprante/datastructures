package org.xbib.datastructures.charset.demo;

import org.xbib.datastructures.charset.CharsetUtil;
import org.xbib.datastructures.charset.ModifiedUTF8Charset;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class UTF8BenchmarkMain {

    private static final Logger logger = Logger.getLogger(UTF8BenchmarkMain.class.getName());
    
    static public void main(String[] args) throws Exception {
        String controlCharsString = createStringWithCharRange('\u0000', 0x20);
        String asciiOnlyString = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
        String iso88591CharsString = createStringWithCharRange('\u0080', 128);
        String remainingCharsString = createStringWithCharRange('\u0100', 65279);
        String twoKCharsString = createStringWithCharRange('\u0100', 2000);
        String latin300CharString = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent commodo vestibulum tellus at rutrum. Ut in ipsum augue, eget posuere nulla. Quisque elementum ante ut leo euismod nec hendrerit lectus lobortis. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia posuere.";
        int count = 1000000;
        String[] strings = new String[] { twoKCharsString };
        byte[][] byteArrays = createUTF8ByteArrays(strings);
        EncodeBenchmark[] encodeBenchmarks = new EncodeBenchmark[] {
            new EncodeBenchmark("JVM String.getBytes()", new EncodeStringGetBytes(), strings),
            new EncodeBenchmark("CharsetUtil.encode() w/ UTF-8 Charset", new EncodeCharsetUtil(), strings),
            new EncodeBenchmark("CharsetUtil.encode() w/ Modified UTF-8 Charset", new EncodeModifiedUTF8Charset(), strings),
        };
        
        DecodeBenchmark[] decodeBenchmarks = new DecodeBenchmark[] {
            new DecodeBenchmark("JVM new String()", new DecodeNewString(), byteArrays),
            new DecodeBenchmark("CharsetUtil.decode() w/ UTF-8 Charset", new DecodeCharsetUtil(), byteArrays),
            new DecodeBenchmark("CharsetUtil.decode() w/ Modified UTF-8 Charset", new DecodeModifiedUTF8Charset(), byteArrays)
        };
        
        // warmup each benchmark first
        for (EncodeBenchmark eb : encodeBenchmarks) {
            eb.warmup(1);
        }
        
        // warmup each benchmark first
        for (DecodeBenchmark db : decodeBenchmarks) {
            db.warmup(1);
        }
        
        // run each benchmark
        for (EncodeBenchmark eb : encodeBenchmarks) {
            System.gc();
            eb.run(count);
        }
        
        // run each benchmark
        for (DecodeBenchmark db : decodeBenchmarks) {
            System.gc();
            db.run(count);
        }
    }

    static public String createStringWithCharRange(char start, int length) {
        StringBuilder buf = new StringBuilder(length);
        int end = start+length;
        for (int i = start; i < end; i++) {
            buf.append((char)i);
        }
        return buf.toString();
    }
    
    static public byte[][] createUTF8ByteArrays(String[] strings) throws Exception {
        byte[][] byteArrays = new byte[strings.length][];
        for (int i = 0; i < strings.length; i++) {
            byteArrays[i] = strings[i].getBytes(StandardCharsets.UTF_8);
        }
        return byteArrays;
    }
    
    static public class EncodeBenchmark {
        public String name;
        public EncodeTest test;
        public String[] strings;

        public EncodeBenchmark(String name, EncodeTest test, String[] strings) {
            this.name = name;
            this.test = test;
            this.strings = strings;
        }
        
        public void warmup(int count) throws Exception {
            this.test.encode(strings, count);
        }
        
        public void run(int count) throws Exception {
            long startMillis = System.currentTimeMillis();
            this.test.encode(strings, count);
            long stopMillis = System.currentTimeMillis();
            System.out.println("Encode Benchmark: " + name);
            System.out.println(" count: " + count);
            System.out.println("  time: " + (stopMillis-startMillis) + " ms");
        }
    }
    
    static public class DecodeBenchmark {
        public String name;
        public DecodeTest test;
        public byte[][] byteArrays;

        public DecodeBenchmark(String name, DecodeTest test, byte[][] byteArrays) {
            this.name = name;
            this.test = test;
            this.byteArrays = byteArrays;
        }
        
        public void warmup(int count) throws Exception {
            this.test.decode(byteArrays, count);
        }
        
        public void run(int count) throws Exception {
            long startMillis = System.currentTimeMillis();
            this.test.decode(byteArrays, count);
            long stopMillis = System.currentTimeMillis();
            System.out.println("Decode Benchmark: " + name);
            System.out.println(" count: " + count);
            System.out.println("  time: " + (stopMillis-startMillis) + " ms");
        }
    }
    
    public interface EncodeTest {
        void encode(String[] strings, int count) throws Exception;
    }
    
    public interface DecodeTest {
        void decode(byte[][] byteArrays, int count) throws Exception;
    }
    
    static public class EncodeStringGetBytes implements EncodeTest {
        @Override
        public void encode(String[] strings, int count) throws Exception {
            for (int i = 0; i < count; i++) {
                for (String s : strings) {
                    byte[] b = s.getBytes(StandardCharsets.UTF_8);
                }
            }
        }
    }
    
    static public class EncodeCharsetUtil implements EncodeTest {
        @Override
        public void encode(String[] strings, int count) throws Exception {
            for (int i = 0; i < count; i++) {
                for (String s : strings) {
                    byte[] b = CharsetUtil.encode(new StringBuilder(s), CharsetUtil.CHARSET_UTF_8);
                }
            }
        }
    }
    
    static public class EncodeModifiedUTF8Charset implements EncodeTest {
        ModifiedUTF8Charset charset = new ModifiedUTF8Charset();
        @Override
        public void encode(String[] strings, int count) throws Exception {
            for (int i = 0; i < count; i++) {
                for (String s : strings) {
                    byte[] b = charset.encode(new StringBuilder(s));
                }
            }
        }
    }
    
    static public class DecodeNewString implements DecodeTest {
        @Override
        public void decode(byte[][] byteArrays, int count) throws Exception {
            for (int i = 0; i < count; i++) {
                for (byte[] b : byteArrays) {
                    String s = new String(b, StandardCharsets.UTF_8);
                }
            }
        }
    }
    
    static public class DecodeCharsetUtil implements DecodeTest {
        @Override
        public void decode(byte[][] byteArrays, int count) throws Exception {
            for (int i = 0; i < count; i++) {
                for (byte[] b : byteArrays) {
                    String s = CharsetUtil.decode(b, CharsetUtil.CHARSET_UTF_8);
                }
            }
        }
    }
    
    static public class DecodeModifiedUTF8Charset implements DecodeTest {
        ModifiedUTF8Charset charset = new ModifiedUTF8Charset();
        @Override
        public void decode(byte[][] byteArrays, int count) throws Exception {
            for (int i = 0; i < count; i++) {
                for (byte[] b : byteArrays) {
                    String s = charset.decode(b);
                }
            }
        }
    }
    
}
