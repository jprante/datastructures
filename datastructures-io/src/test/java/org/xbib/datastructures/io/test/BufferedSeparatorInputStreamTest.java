package org.xbib.datastructures.io.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;
import org.xbib.datastructures.io.BufferedSeparatorInputStream;
import org.xbib.datastructures.io.Information;
import org.xbib.datastructures.io.InformationSeparator;
import org.xbib.datastructures.io.FilteredSeparatorInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public class BufferedSeparatorInputStreamTest {

    private int dataCount = 0;
    private int unitCount = 0;
    private int recordCount = 0;
    private int groupCount = 0;
    private int fileCount = 0;

    private void incDataCount(int len) {
        dataCount += len;
    }

    private void incUnitCount() {
        unitCount++;
    }

    private void incRecordCount() {
        recordCount++;
    }

    private void incGroupCount() {
        groupCount++;
    }

    private void incFileCount() {
        fileCount++;
    }

    @Test
    public void testStreamSpearators() throws Exception {
        String s = "/sequential.groupstream";
        InputStream in = getClass().getResourceAsStream(s);
        BufferedSeparatorInputStream bufferedSeparatorInputStream =
                new BufferedSeparatorInputStream(in, 8192, 1024, StandardCharsets.ISO_8859_1);

        Consumer<Information> consumer = information -> {
            switch (information.getSeparator()) {
                case InformationSeparator.US:
                    incUnitCount();
                    break;
                case InformationSeparator.RS:
                    incRecordCount();
                    break;
                case InformationSeparator.GS:
                    incGroupCount();
                    break;
                case InformationSeparator.FS:
                    incFileCount();
                    break;
            }
            String string = bufferedSeparatorInputStream.informationString();
            incDataCount(string.length());
        };
        for( Information information : bufferedSeparatorInputStream) {
            consumer.accept(information);
        }
        in.close();
        assertEquals(0, fileCount);
        assertEquals(10, groupCount);
        assertEquals(356, recordCount);
        assertEquals(23, unitCount);
        assertEquals(9307, dataCount);
    }

    @Test
    public void testGroupStream() throws Exception {
        String s = "/sequential.groupstream";
        InputStream in = getClass().getResourceAsStream(s);
        final AtomicInteger count = new AtomicInteger(0);
        BufferedSeparatorInputStream bufferedSeparatorInputStream =
                new BufferedSeparatorInputStream(in, 8192, 1024, StandardCharsets.ISO_8859_1);
        Consumer<Information> consumer = information -> count.incrementAndGet();
        for (Information information: bufferedSeparatorInputStream) {
            consumer.accept(information);
        }
        in.close();
        assertEquals(389, count.get());
    }

    @Test
    public void testSpeed() throws Exception {
        String s = "/periouni.mrc";
        Map<Integer, Integer> map2 = new LinkedHashMap<>();
        InputStream in2 = getClass().getResourceAsStream(s);
        final AtomicInteger count2 = new AtomicInteger(0);
        BufferedSeparatorInputStream bufferedSeparatorInputStream =
                new BufferedSeparatorInputStream(in2, 8192, 1024, StandardCharsets.ISO_8859_1);
        Consumer<Information> informationConsumer =
                information -> map2.put(count2.incrementAndGet(), information.getCount());
        for (Information information: bufferedSeparatorInputStream) {
            informationConsumer.accept(information);
        }
        in2.close();

        // compare to slow FilteredSeparatorInputStream
        Map<Integer, Integer> map = new LinkedHashMap<>();
        InputStream in = getClass().getResourceAsStream(s);
        final AtomicInteger count = new AtomicInteger(0);
        FilteredSeparatorInputStream filteredSeparatorInputStream =
                new FilteredSeparatorInputStream(in, 1024, StandardCharsets.ISO_8859_1);
        Consumer<Information> consumer =
                information1 -> map.put(count.incrementAndGet(), information1.getCount());
        for (Information information1: filteredSeparatorInputStream) {
            consumer.accept(information1);
        }
        in.close();

        assertEquals(map.size(), map2.size());
        for (int i = 1; i <= map.size(); i++) {
            if (!map.get(i).equals(map2.get(i))) {
                fail("diff: " + i + " " + map.get(i) + " != " + map2.get(i));
            }
        }
    }

    @Test
    public void testInformationStreamInformation() {
        String s = "/periouni.mrc";
        InputStream in = getClass().getResourceAsStream(s);
        BufferedSeparatorInputStream bufferedSeparatorInputStream =
                new BufferedSeparatorInputStream(in, 8192, 1024, StandardCharsets.ISO_8859_1);
        AtomicLong l = new AtomicLong();
        bufferedSeparatorInputStream.stream().forEach(information -> l.addAndGet(bufferedSeparatorInputStream.informationString().length()));
        assertEquals(3400860, l.get());
    }

    @Test
    public void testInformationStreamCount() {
        String s = "/periouni.mrc";
        InputStream in = getClass().getResourceAsStream(s);
        BufferedSeparatorInputStream bufferedSeparatorInputStream =
                new BufferedSeparatorInputStream(in, 8192, 1024, StandardCharsets.ISO_8859_1);
        long l = bufferedSeparatorInputStream.stream().count();
        assertEquals(192247, l);
    }

    @Test
    public void testMARC() throws Exception {
        String s = "/summerland.mrc";
        Map<Integer, Integer> map = new LinkedHashMap<>();
        InputStream in = getClass().getResourceAsStream(s);
        final AtomicInteger count = new AtomicInteger(0);
        BufferedSeparatorInputStream bufferedSeparatorInputStream =
                new BufferedSeparatorInputStream(in, 8192, 1024, StandardCharsets.ISO_8859_1);
        Consumer<Information> consumer = information -> map.put(count.incrementAndGet(), information.getCount());
        for (Information information: bufferedSeparatorInputStream) {
            consumer.accept(information);
        }
        assertEquals("{1=204, 2=8, 3=16, 4=40, 5=2, 6=11, 7=2, 8=18, 9=2, 10=4, 11=4, 12=4, 13=2, 14=17, 15=2, "
                + "16=13, 17=16, 18=2, 19=8, 20=2, 21=11, 22=43, 23=7, 24=2, 25=9, 26=7, 27=2, 28=171, 29=2, 30=9, "
                + "31=2, 32=9, 33=9, 34=2, 35=6, 36=9, 37=0}", map.toString());
        in.close();
    }
}
