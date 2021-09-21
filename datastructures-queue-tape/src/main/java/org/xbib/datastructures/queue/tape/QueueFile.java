package org.xbib.datastructures.queue.tape;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static java.lang.Math.min;

public final class QueueFile implements Closeable, Iterable<byte[]> {

    private static final int VERSIONED_HEADER = 0x80000001;

    public static final int INITIAL_LENGTH = 4096; // one file system block

    private static final byte[] ZEROES = new byte[INITIAL_LENGTH];

    public final RandomAccessFile raf;

    final File file;

    final boolean versioned;

    final int headerLength;

    public long fileLength;

    int elementCount;

    Element first;

    private Element last;

    private final byte[] buffer = new byte[32];

    int modCount = 0;

    private final boolean zero;

    boolean closed;

    static RandomAccessFile initializeFromFile(File file, boolean forceLegacy) throws IOException {
        if (!file.exists()) {
            File tempFile = new File(file.getPath() + ".tmp");
            try (RandomAccessFile raf = open(tempFile)) {
                raf.setLength(INITIAL_LENGTH);
                raf.seek(0);
                if (forceLegacy) {
                    raf.writeInt(INITIAL_LENGTH);
                } else {
                    raf.writeInt(VERSIONED_HEADER);
                    raf.writeLong(INITIAL_LENGTH);
                }
            }
            if (!tempFile.renameTo(file)) {
                throw new IOException("Rename failed!");
            }
        }
        return open(file);
    }

    private static RandomAccessFile open(File file) throws FileNotFoundException {
        return new RandomAccessFile(file, "rwd");
    }

    public QueueFile(File file, RandomAccessFile raf, boolean zero, boolean forceLegacy) throws IOException {
        this.file = file;
        this.raf = raf;
        this.zero = zero;
        raf.seek(0);
        raf.readFully(buffer);
        versioned = !forceLegacy && (buffer[0] & 0x80) != 0;
        long firstOffset;
        long lastOffset;
        if (versioned) {
            headerLength = 32;
            int version = readInt(buffer, 0) & 0x7FFFFFFF;
            if (version != 1) {
                throw new IOException("Unable to read version " + version + " format. Supported versions are 1 and legacy.");
            }
            fileLength = readLong(buffer, 4);
            elementCount = readInt(buffer, 12);
            firstOffset = readLong(buffer, 16);
            lastOffset = readLong(buffer, 24);
        } else {
            headerLength = 16;
            fileLength = readInt(buffer, 0);
            elementCount = readInt(buffer, 4);
            firstOffset = readInt(buffer, 8);
            lastOffset = readInt(buffer, 12);
        }
        if (fileLength > raf.length()) {
            throw new IOException("File is truncated. Expected length: " + fileLength + ", Actual length: " + raf.length());
        } else if (fileLength <= headerLength) {
            throw new IOException("File is corrupt; length stored in header (" + fileLength + ") is invalid.");
        }
        first = readElement(firstOffset);
        last = readElement(lastOffset);
    }

    private static void writeInt(byte[] buffer, int offset, int value) {
        buffer[offset] = (byte) (value >> 24);
        buffer[offset + 1] = (byte) (value >> 16);
        buffer[offset + 2] = (byte) (value >> 8);
        buffer[offset + 3] = (byte) value;
    }

    private static int readInt(byte[] buffer, int offset) {
        return ((buffer[offset] & 0xff) << 24)
                + ((buffer[offset + 1] & 0xff) << 16)
                + ((buffer[offset + 2] & 0xff) << 8)
                + (buffer[offset + 3] & 0xff);
    }

    private static void writeLong(byte[] buffer, int offset, long value) {
        buffer[offset] = (byte) (value >> 56);
        buffer[offset + 1] = (byte) (value >> 48);
        buffer[offset + 2] = (byte) (value >> 40);
        buffer[offset + 3] = (byte) (value >> 32);
        buffer[offset + 4] = (byte) (value >> 24);
        buffer[offset + 5] = (byte) (value >> 16);
        buffer[offset + 6] = (byte) (value >> 8);
        buffer[offset + 7] = (byte) value;
    }

    private static long readLong(byte[] buffer, int offset) {
        return ((buffer[offset] & 0xffL) << 56)
                + ((buffer[offset + 1] & 0xffL) << 48)
                + ((buffer[offset + 2] & 0xffL) << 40)
                + ((buffer[offset + 3] & 0xffL) << 32)
                + ((buffer[offset + 4] & 0xffL) << 24)
                + ((buffer[offset + 5] & 0xffL) << 16)
                + ((buffer[offset + 6] & 0xffL) << 8)
                + (buffer[offset + 7] & 0xffL);
    }

    private void writeHeader(long fileLength, int elementCount, long firstPosition, long lastPosition)
            throws IOException {
        raf.seek(0);

        if (versioned) {
            writeInt(buffer, 0, VERSIONED_HEADER);
            writeLong(buffer, 4, fileLength);
            writeInt(buffer, 12, elementCount);
            writeLong(buffer, 16, firstPosition);
            writeLong(buffer, 24, lastPosition);
            raf.write(buffer, 0, 32);
            return;
        }

        // Legacy queue header.
        writeInt(buffer, 0, (int) fileLength); // Signed, so leading bit is always 0 aka legacy.
        writeInt(buffer, 4, elementCount);
        writeInt(buffer, 8, (int) firstPosition);
        writeInt(buffer, 12, (int) lastPosition);
        raf.write(buffer, 0, 16);
    }

    Element readElement(long position) throws IOException {
        if (position == 0) return Element.NULL;
        ringRead(position, buffer, 0, Element.HEADER_LENGTH);
        int length = readInt(buffer, 0);
        return new Element(position, length);
    }

    long wrapPosition(long position) {
        return position < fileLength ? position
                : headerLength + position - fileLength;
    }

    private void ringWrite(long position, byte[] buffer, int offset, int count) throws IOException {
        position = wrapPosition(position);
        if (position + count <= fileLength) {
            raf.seek(position);
            raf.write(buffer, offset, count);
        } else {
            int beforeEof = (int) (fileLength - position);
            raf.seek(position);
            raf.write(buffer, offset, beforeEof);
            raf.seek(headerLength);
            raf.write(buffer, offset + beforeEof, count - beforeEof);
        }
    }

    private void ringErase(long position, long length) throws IOException {
        while (length > 0) {
            int chunk = (int) min(length, ZEROES.length);
            ringWrite(position, ZEROES, 0, chunk);
            length -= chunk;
            position += chunk;
        }
    }

    void ringRead(long position, byte[] buffer, int offset, int count) throws IOException {
        position = wrapPosition(position);
        if (position + count <= fileLength) {
            raf.seek(position);
            raf.readFully(buffer, offset, count);
        } else {
            int beforeEof = (int) (fileLength - position);
            raf.seek(position);
            raf.readFully(buffer, offset, beforeEof);
            raf.seek(headerLength);
            raf.readFully(buffer, offset + beforeEof, count - beforeEof);
        }
    }

    public void add(byte[] data) throws IOException {
        add(data, 0, data.length);
    }

    public void add(byte[] data, int offset, int count) throws IOException {
        if (data == null) {
            throw new NullPointerException("data == null");
        }
        if ((offset | count) < 0 || count > data.length - offset) {
            throw new IndexOutOfBoundsException();
        }
        if (closed) throw new IllegalStateException("closed");
        expandIfNecessary(count);
        boolean wasEmpty = isEmpty();
        long position = wasEmpty ? headerLength : wrapPosition(last.position + Element.HEADER_LENGTH + last.length);
        Element newLast = new Element(position, count);
        writeInt(buffer, 0, count);
        ringWrite(newLast.position, buffer, 0, Element.HEADER_LENGTH);
        ringWrite(newLast.position + Element.HEADER_LENGTH, data, offset, count);
        long firstPosition = wasEmpty ? newLast.position : first.position;
        writeHeader(fileLength, elementCount + 1, firstPosition, newLast.position);
        last = newLast;
        elementCount++;
        modCount++;
        if (wasEmpty) first = last;
    }

    private long usedBytes() {
        if (elementCount == 0) return headerLength;
        if (last.position >= first.position) {
            return (last.position - first.position)
                    + Element.HEADER_LENGTH + last.length
                    + headerLength;
        } else {
            return last.position
                    + Element.HEADER_LENGTH + last.length
                    + fileLength - first.position;
        }
    }

    private long remainingBytes() {
        return fileLength - usedBytes();
    }

    public boolean isEmpty() {
        return elementCount == 0;
    }

    /**
     * If necessary, expands the file to accommodate an additional element of the given length.
     *
     * @param dataLength length of data being added
     */
    private void expandIfNecessary(long dataLength) throws IOException {
        long elementLength = Element.HEADER_LENGTH + dataLength;
        long remainingBytes = remainingBytes();
        if (remainingBytes >= elementLength) return;

        // Expand.
        long previousLength = fileLength;
        long newLength;
        // Double the length until we can fit the new data.
        do {
            remainingBytes += previousLength;
            newLength = previousLength << 1;
            previousLength = newLength;
        } while (remainingBytes < elementLength);

        setLength(newLength);

        // Calculate the position of the tail end of the data in the ring buffer
        long endOfLastElement = wrapPosition(last.position + Element.HEADER_LENGTH + last.length);
        long count = 0;
        // If the buffer is split, we need to make it contiguous
        if (endOfLastElement <= first.position) {
            FileChannel channel = raf.getChannel();
            channel.position(fileLength); // destination position
            count = endOfLastElement - headerLength;
            if (channel.transferTo(headerLength, count, channel) != count) {
                throw new AssertionError("Copied insufficient number of bytes!");
            }
        }

        // Commit the expansion.
        if (last.position < first.position) {
            long newLastPosition = fileLength + last.position - headerLength;
            writeHeader(newLength, elementCount, first.position, newLastPosition);
            last = new Element(newLastPosition, last.length);
        } else {
            writeHeader(newLength, elementCount, first.position, last.position);
        }

        fileLength = newLength;

        if (zero) {
            ringErase(headerLength, count);
        }
    }

    /**
     * Sets the length of the file.
     */
    private void setLength(long newLength) throws IOException {
        // Set new file length (considered metadata) and sync it to storage.
        raf.setLength(newLength);
        raf.getChannel().force(true);
    }

    /**
     * Reads the eldest element. Returns null if the queue is empty.
     */
    public byte[] peek() throws IOException {
        if (closed) throw new IllegalStateException("closed");
        if (isEmpty()) return null;
        int length = first.length;
        byte[] data = new byte[length];
        ringRead(first.position + Element.HEADER_LENGTH, data, 0, length);
        return data;
    }

    /**
     * Returns an iterator over elements in this QueueFile.
     *
     * <p>The iterator disallows modifications to be made to the QueueFile during iteration. Removing
     * elements from the head of the QueueFile is permitted during iteration using
     * {@link Iterator#remove()}.
     *
     * <p>The iterator may throw an unchecked {@link IOException} during {@link Iterator#next()}
     * or {@link Iterator#remove()}.
     */
    @Override
    public Iterator<byte[]> iterator() {
        return new ElementIterator();
    }

    private final class ElementIterator implements Iterator<byte[]> {
        int nextElementIndex = 0;
        private long nextElementPosition = first.position;
        int expectedModCount = modCount;

        ElementIterator() {
        }

        private void checkForComodification() {
            if (modCount != expectedModCount) throw new ConcurrentModificationException();
        }

        @Override
        public boolean hasNext() {
            if (closed) throw new IllegalStateException("closed");
            checkForComodification();
            return nextElementIndex != elementCount;
        }

        @Override
        public byte[] next() {
            if (closed) throw new IllegalStateException("closed");
            checkForComodification();
            if (isEmpty()) throw new NoSuchElementException();
            if (nextElementIndex >= elementCount) throw new NoSuchElementException();
            try {
                Element current = readElement(nextElementPosition);
                byte[] buffer = new byte[current.length];
                nextElementPosition = wrapPosition(current.position + Element.HEADER_LENGTH);
                ringRead(nextElementPosition, buffer, 0, current.length);
                nextElementPosition = wrapPosition(current.position + Element.HEADER_LENGTH + current.length);
                nextElementIndex++;
                return buffer;
            } catch (IOException e) {
                throw QueueFile.<Error>getSneakyThrowable(e);
            }
        }

        @Override
        public void remove() {
            checkForComodification();
            if (isEmpty()) throw new NoSuchElementException();
            if (nextElementIndex != 1) {
                throw new UnsupportedOperationException("Removal is only permitted from the head.");
            }
            try {
                QueueFile.this.remove();
            } catch (IOException e) {
                throw QueueFile.<Error>getSneakyThrowable(e);
            }
            expectedModCount = modCount;
            nextElementIndex--;
        }
    }

    public int size() {
        return elementCount;
    }

    public void remove() throws IOException {
        remove(1);
    }

    public void remove(int n) throws IOException {
        if (n < 0) {
            throw new IllegalArgumentException("Cannot remove negative (" + n + ") number of elements.");
        }
        if (n == 0) {
            return;
        }
        if (n == elementCount) {
            clear();
            return;
        }
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        if (n > elementCount) {
            throw new IllegalArgumentException("Cannot remove more elements (" + n + ") than present in queue (" + elementCount + ").");
        }
        long eraseStartPosition = first.position;
        long eraseTotalLength = 0;
        long newFirstPosition = first.position;
        int newFirstLength = first.length;
        for (int i = 0; i < n; i++) {
            eraseTotalLength += Element.HEADER_LENGTH + newFirstLength;
            newFirstPosition = wrapPosition(newFirstPosition + Element.HEADER_LENGTH + newFirstLength);
            ringRead(newFirstPosition, buffer, 0, Element.HEADER_LENGTH);
            newFirstLength = readInt(buffer, 0);
        }
        writeHeader(fileLength, elementCount - n, newFirstPosition, last.position);
        elementCount -= n;
        modCount++;
        first = new Element(newFirstPosition, newFirstLength);
        if (zero) {
            ringErase(eraseStartPosition, eraseTotalLength);
        }
    }

    public void clear() throws IOException {
        if (closed) throw new IllegalStateException("closed");
        writeHeader(INITIAL_LENGTH, 0, 0, 0);
        if (zero) {
            raf.seek(headerLength);
            raf.write(ZEROES, 0, INITIAL_LENGTH - headerLength);
        }
        elementCount = 0;
        first = Element.NULL;
        last = Element.NULL;
        if (fileLength > INITIAL_LENGTH) setLength(INITIAL_LENGTH);
        fileLength = INITIAL_LENGTH;
        modCount++;
    }

    public File file() {
        return file;
    }

    @Override
    public void close() throws IOException {
        closed = true;
        raf.close();
    }

    @Override
    public String toString() {
        return "QueueFile{"
                + "file=" + file
                + ", zero=" + zero
                + ", versioned=" + versioned
                + ", length=" + fileLength
                + ", size=" + elementCount
                + ", first=" + first
                + ", last=" + last
                + '}';
    }

    public static class Element {
        static final Element NULL = new Element(0, 0);
        public static final int HEADER_LENGTH = 4;
        final long position;
        final int length;
        public Element(long position, int length) {
            this.position = position;
            this.length = length;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName()
                    + "[position=" + position
                    + ", length=" + length
                    + "]";
        }
    }

    public static final class Builder {
        final File file;
        boolean zero = true;
        boolean forceLegacy = false;

        public Builder(File file) {
            if (file == null) {
                throw new NullPointerException("file == null");
            }
            this.file = file;
        }

        public Builder zero(boolean zero) {
            this.zero = zero;
            return this;
        }

        public Builder forceLegacy(boolean forceLegacy) {
            this.forceLegacy = forceLegacy;
            return this;
        }

        public QueueFile build() throws IOException {
            RandomAccessFile raf = initializeFromFile(file, forceLegacy);
            QueueFile qf = null;
            try {
                qf = new QueueFile(file, raf, zero, forceLegacy);
                return qf;
            } finally {
                if (qf == null) {
                    raf.close();
                }
            }
        }
    }

    @SuppressWarnings({"unchecked", "TypeParameterUnusedInFormals"})
    static <T extends Throwable> T getSneakyThrowable(Throwable t) throws T {
        throw (T) t;
    }
}
