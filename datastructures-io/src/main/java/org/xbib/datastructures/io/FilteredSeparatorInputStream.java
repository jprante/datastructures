package org.xbib.datastructures.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * An unbuffered separator stream which reads character by character. This is a very slow implementation.
 */
public class FilteredSeparatorInputStream extends FilterInputStream implements Iterable<Information> {

    private final ByteOutput byteOutput;

    private final Information information;

    private final Charset charset;

    private Consumer<Information> consumer;

    /**
     * Create separator stream.
     * @param in the underlying input stream
     */
    public FilteredSeparatorInputStream(InputStream in,
                                        int outputBufferSize,
                                        Charset charset) {
        super(in);
        this.charset = charset;
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

    public String informationString() {
        return new String(byteOutput.bytes(), 0, information.getCount(), charset);
    }

    /**
     * Read next chunk. This is slow, it uses the {@code read()} method.
     * @return the next chunk
     * @throws IOException if chunk reading fails
     */
    private Information nextInformation() throws IOException {
        while (true) {
            int ch = super.read();
            if (ch == -1) {
                return null;
            }
            switch (ch) {
                case InformationSeparator.US:
                case InformationSeparator.RS:
                case InformationSeparator.GS:
                case InformationSeparator.FS:
                    information.setSeparator((byte) ch);
                    information.setCount(byteOutput.count());
                    if (consumer != null) {
                        consumer.accept(information);
                    }
                    byteOutput.reset();
                    return information;
                default:
                    byteOutput.writeByte((byte) ch);
            }
        }
    }
}
