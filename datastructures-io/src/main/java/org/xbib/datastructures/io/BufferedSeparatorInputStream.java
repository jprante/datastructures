package org.xbib.datastructures.io;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * A buffered input stream for iterating over data streams with information
 * separators.
 *
 * The information separators of the C0 control group are defined in:
 * - ANSI X3.4-1967 (ASCII)
 * - IETF RFC 20 (Vint Cerf, 1969)
 * - ISO-646:1972
 * - ECMA-6 3rd revision August 1973
 * - ECMA-48
 * - ISO/IEC 6429
 * - CCITT International Telegraph Alphabet Number 5 (ITA-5)
 *
 * From ASCII-1967:
 * "Can be used as delimiters to mark fields of data structures.
 * If used for hierarchical levels, US is the lowest level (dividing
 * plain-text data items), while RS, GS, and FS are of increasing level
 * to divide groups made up of items of the level beneath it."
 *
 * Form IETF RFC 20:
 * "Information Separator: A character which is used to separate
 * and qualify information in a logical sense.  There is a group of four
 * such characters, which are to be used in a hierarchical order."
 *
 * From ECMA-48 (ISO/IEC 6429):
 *
 * "Each information separator is given two names. The names,
 * INFORMATION SEPARATOR FOUR (IS4), INFORMATION SEPARATOR THREE (IS3),
 * INFORMATION SEPARATOR TWO (IS2), and INFORMATION SEPARATOR ONE (IS1)
 * are the general names. The names FILE SEPARATOR (FS), GROUP SEPARATOR (GS),
 * RECORD SEPARATOR (RS), and UNIT SEPARATOR (US) are the specific names and
 * are intended mainly for applications where the information separators are
 * used hierarchically. The ascending order is then US, RS, GS, FS.
 * In this case, data normally delimited by a particular separator cannot
 * be split by a higher-order separator but will be considered as delimited by
 * any other higher-order separator.
 * In ISO/IEC 10538, IS3 and IS4 are given the names PAGE TERMINATOR (PT)
 * and DOCUMENT TERMINATOR (DT), respectively and may be used to reset
 * presentation attributes to the default state."
 */
public class BufferedSeparatorInputStream extends BufferedInputStream implements Iterable<Information> {

    private final ByteOutput byteOutput;

    private final int buffersize;

    private final Charset charset;

    private int begin;

    private int end;

    private byte separator;

    private final Information information;

    private Consumer<Information> consumer;

    /**
     * Create a buffered information separator stream.
     * @param in the underlying input stream
     * @param inputBuffersize the buffer size for the input stream
     * @param outputBufferSize the buffer size for the output buffer stream
     */
    public BufferedSeparatorInputStream(InputStream in,
                                        int inputBuffersize,
                                        int outputBufferSize,
                                        Charset charset) {
        super(in, inputBuffersize);
        this.buffersize = inputBuffersize;
        this.charset = charset;
        this.begin = 0;
        this.end = -1;
        this.separator = InformationSeparator.FS;
        this.byteOutput = new BytesStreamOutput(outputBufferSize);
        this.information = new Information();
    }

    public void setConsumer(Consumer<Information> consumer) {
        this.consumer = consumer;
    }

    @Override
    public Iterator<Information> iterator() {
        return new Iterator<>() {
            Information information = null;

            @Override
            public boolean hasNext() {
                if (information != null) {
                    return true;
                } else {
                    try {
                        information = nextInformation();
                        return information != null;
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                }
            }

            @Override
            public Information next() {
                if (information != null || hasNext()) {
                    Information data = information;
                    information = null;
                    return data;
                } else {
                    throw new NoSuchElementException();
                }
            }
        };
    }

    public Stream<Information> stream() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator(),
                Spliterator.ORDERED | Spliterator.NONNULL), false);
    }

    public ByteOutput byteOutput() {
        return byteOutput;
    }

    public byte getByte(int i) {
        return byteOutput.bytes()[i];
    }

    public int count() {
        return information.getCount();
    }

    public byte[] informationBytes() {
        return Arrays.copyOf(byteOutput.bytes(), information.getCount());
    }

    public String informationString() {
        return new String(byteOutput.bytes(), 0, information.getCount(), charset);
    }

    private Information nextInformation() throws IOException {
        while (true) {
            if (end - begin <= 0) {
                begin = 0;
                end = super.read(buf, begin, buffersize);
            }
            if (end == -1) {
                return null;
            }
            for (int i = begin; i < end; i++) {
                byte b = buf[i];
                switch (b) {
                    case InformationSeparator.US:
                    case InformationSeparator.RS:
                    case InformationSeparator.GS:
                    case InformationSeparator.FS:
                        pos = i;
                        separator = b;
                        byteOutput.write(buf, begin, pos - begin);
                        information.setSeparator(separator);
                        information.setCount(byteOutput.count());
                        if (consumer != null) {
                            consumer.accept(information);
                        }
                        byteOutput.reset();
                        begin = pos + 1;
                        return information;
                }
            }
            byteOutput.write(buf, begin, buffersize - begin);
            begin = buffersize;
        }
    }
}
