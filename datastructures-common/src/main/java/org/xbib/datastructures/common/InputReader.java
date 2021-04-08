
package org.xbib.datastructures.common;

import java.io.IOException;
import java.io.InputStream;

public class InputReader {

    private static final int DEFAULT_BUFFER_SIZE = 1 << 16;

    private static final InputStream DEFAULT_STREAM = System.in;

    private static final int MAX_DECIMAL_PRECISION = 21;

    private int c;

    private final byte[] buf;

    private int bufIndex;

    private int numBytesRead;

    private final InputStream stream;

    private static final byte EOF = -1;

    private static final byte NEW_LINE = 10;

    private static final byte SPACE = 32;

    private static final byte DASH = 45;

    private static final byte DOT = 46;

    private char[] charBuffer;

    private static final int[] ints = new int[58];

    static {
        int value = 0;
        for (int i = 48; i < 58; i++) {
            ints[i] = value++;
        }
    }

    private static final double[][] doubles = {
            {0.0d, 0.00d, 0.000d, 0.0000d, 0.00000d, 0.000000d, 0.0000000d, 0.00000000d, 0.000000000d, 0.0000000000d, 0.00000000000d, 0.000000000000d, 0.0000000000000d, 0.00000000000000d, 0.000000000000000d, 0.0000000000000000d, 0.00000000000000000d, 0.000000000000000000d, 0.0000000000000000000d, 0.00000000000000000000d, 0.000000000000000000000d},
            {0.1d, 0.01d, 0.001d, 0.0001d, 0.00001d, 0.000001d, 0.0000001d, 0.00000001d, 0.000000001d, 0.0000000001d, 0.00000000001d, 0.000000000001d, 0.0000000000001d, 0.00000000000001d, 0.000000000000001d, 0.0000000000000001d, 0.00000000000000001d, 0.000000000000000001d, 0.0000000000000000001d, 0.00000000000000000001d, 0.000000000000000000001d},
            {0.2d, 0.02d, 0.002d, 0.0002d, 0.00002d, 0.000002d, 0.0000002d, 0.00000002d, 0.000000002d, 0.0000000002d, 0.00000000002d, 0.000000000002d, 0.0000000000002d, 0.00000000000002d, 0.000000000000002d, 0.0000000000000002d, 0.00000000000000002d, 0.000000000000000002d, 0.0000000000000000002d, 0.00000000000000000002d, 0.000000000000000000002d},
            {0.3d, 0.03d, 0.003d, 0.0003d, 0.00003d, 0.000003d, 0.0000003d, 0.00000003d, 0.000000003d, 0.0000000003d, 0.00000000003d, 0.000000000003d, 0.0000000000003d, 0.00000000000003d, 0.000000000000003d, 0.0000000000000003d, 0.00000000000000003d, 0.000000000000000003d, 0.0000000000000000003d, 0.00000000000000000003d, 0.000000000000000000003d},
            {0.4d, 0.04d, 0.004d, 0.0004d, 0.00004d, 0.000004d, 0.0000004d, 0.00000004d, 0.000000004d, 0.0000000004d, 0.00000000004d, 0.000000000004d, 0.0000000000004d, 0.00000000000004d, 0.000000000000004d, 0.0000000000000004d, 0.00000000000000004d, 0.000000000000000004d, 0.0000000000000000004d, 0.00000000000000000004d, 0.000000000000000000004d},
            {0.5d, 0.05d, 0.005d, 0.0005d, 0.00005d, 0.000005d, 0.0000005d, 0.00000005d, 0.000000005d, 0.0000000005d, 0.00000000005d, 0.000000000005d, 0.0000000000005d, 0.00000000000005d, 0.000000000000005d, 0.0000000000000005d, 0.00000000000000005d, 0.000000000000000005d, 0.0000000000000000005d, 0.00000000000000000005d, 0.000000000000000000005d},
            {0.6d, 0.06d, 0.006d, 0.0006d, 0.00006d, 0.000006d, 0.0000006d, 0.00000006d, 0.000000006d, 0.0000000006d, 0.00000000006d, 0.000000000006d, 0.0000000000006d, 0.00000000000006d, 0.000000000000006d, 0.0000000000000006d, 0.00000000000000006d, 0.000000000000000006d, 0.0000000000000000006d, 0.00000000000000000006d, 0.000000000000000000006d},
            {0.7d, 0.07d, 0.007d, 0.0007d, 0.00007d, 0.000007d, 0.0000007d, 0.00000007d, 0.000000007d, 0.0000000007d, 0.00000000007d, 0.000000000007d, 0.0000000000007d, 0.00000000000007d, 0.000000000000007d, 0.0000000000000007d, 0.00000000000000007d, 0.000000000000000007d, 0.0000000000000000007d, 0.00000000000000000007d, 0.000000000000000000007d},
            {0.8d, 0.08d, 0.008d, 0.0008d, 0.00008d, 0.000008d, 0.0000008d, 0.00000008d, 0.000000008d, 0.0000000008d, 0.00000000008d, 0.000000000008d, 0.0000000000008d, 0.00000000000008d, 0.000000000000008d, 0.0000000000000008d, 0.00000000000000008d, 0.000000000000000008d, 0.0000000000000000008d, 0.00000000000000000008d, 0.000000000000000000008d},
            {0.9d, 0.09d, 0.009d, 0.0009d, 0.00009d, 0.000009d, 0.0000009d, 0.00000009d, 0.000000009d, 0.0000000009d, 0.00000000009d, 0.000000000009d, 0.0000000000009d, 0.00000000000009d, 0.000000000000009d, 0.0000000000000009d, 0.00000000000000009d, 0.000000000000000009d, 0.0000000000000000009d, 0.00000000000000000009d, 0.000000000000000000009d}
    };

    public InputReader() {
        this(DEFAULT_STREAM, DEFAULT_BUFFER_SIZE);
    }

    public InputReader(int bufferSize) {
        this(DEFAULT_STREAM, bufferSize);
    }

    public InputReader(InputStream stream) {
        this(stream, DEFAULT_BUFFER_SIZE);
    }

    public InputReader(InputStream stream, int bufferSize) {
        if (stream == null || bufferSize <= 0) {
            throw new IllegalArgumentException();
        }
        buf = new byte[bufferSize];
        charBuffer = new char[128];
        this.stream = stream;
    }

    private byte read() throws IOException {
        if (numBytesRead == EOF) {
            throw new IOException();
        }
        if (bufIndex >= numBytesRead) {
            bufIndex = 0;
            numBytesRead = stream.read(buf);
            if (numBytesRead == EOF) {
                return EOF;
            }
        }
        return buf[bufIndex++];
    }

    private int readJunk(int token) throws IOException {
        if (numBytesRead == EOF) {
            return EOF;
        }
        do {
            while (bufIndex < numBytesRead) {
                if (buf[bufIndex] > token) {
                    return 0;
                }
                bufIndex++;
            }
            numBytesRead = stream.read(buf);
            if (numBytesRead == EOF) {
                return EOF;
            }
            bufIndex = 0;
        } while (true);
    }

    public byte nextByte() throws IOException {
        return (byte) nextInt();
    }

    public int nextInt() throws IOException {
        if (readJunk(DASH - 1) == EOF) {
            throw new IOException();
        }
        int sgn = 1, res = 0;
        c = buf[bufIndex];
        if (c == DASH) {
            sgn = -1;
            bufIndex++;
        }
        do {
            while (bufIndex < numBytesRead) {
                if (buf[bufIndex] > SPACE) {
                    res = (res << 3) + (res << 1);
                    res += ints[buf[bufIndex++]];
                } else {
                    bufIndex++;
                    return res * sgn;
                }
            }
            numBytesRead = stream.read(buf);
            if (numBytesRead == EOF) {
                return res * sgn;
            }
            bufIndex = 0;
        } while (true);
    }

    public long nextLong() throws IOException {
        if (readJunk(DASH - 1) == EOF) {
            throw new IOException();
        }
        int sgn = 1;
        long res = 0L;
        c = buf[bufIndex];
        if (c == DASH) {
            sgn = -1;
            bufIndex++;
        }
        do {
            while (bufIndex < numBytesRead) {
                if (buf[bufIndex] > SPACE) {
                    res = (res << 3) + (res << 1);
                    res += ints[buf[bufIndex++]];
                } else {
                    bufIndex++;
                    return res * sgn;
                }
            }
            numBytesRead = stream.read(buf);
            if (numBytesRead == EOF) {
                return res * sgn;
            }
            bufIndex = 0;
        } while (true);
    }

    private void doubleCharBufferSize() {
        char[] newBuffer = new char[charBuffer.length << 1];
        System.arraycopy(charBuffer, 0, newBuffer, 0, charBuffer.length);
        charBuffer = newBuffer;
    }

    public String nextLine() throws IOException {
        try {
            c = read();
        } catch (IOException e) {
            return null;
        }
        if (c == NEW_LINE) {
            return "";
        }
        if (c == EOF) {
            return null;
        }
        int i = 0;
        charBuffer[i++] = (char) c;
        do {
            while (bufIndex < numBytesRead) {
                if (buf[bufIndex] != NEW_LINE) {
                    if (i == charBuffer.length) {
                        doubleCharBufferSize();
                    }
                    charBuffer[i++] = (char) buf[bufIndex++];
                } else {
                    bufIndex++;
                    return new String(charBuffer, 0, i);
                }
            }
            numBytesRead = stream.read(buf);
            if (numBytesRead == EOF) {
                return new String(charBuffer, 0, i);
            }
            bufIndex = 0;
        } while (true);
    }

    public String nextString() throws IOException {
        if (numBytesRead == EOF) {
            return null;
        }
        if (readJunk(SPACE) == EOF) {
            return null;
        }
        for (int i = 0; ; ) {
            while (bufIndex < numBytesRead) {
                if (buf[bufIndex] > SPACE) {
                    if (i == charBuffer.length) {
                        doubleCharBufferSize();
                    }
                    charBuffer[i++] = (char) buf[bufIndex++];
                } else {
                    bufIndex++;
                    return new String(charBuffer, 0, i);
                }
            }
            numBytesRead = stream.read(buf);
            if (numBytesRead == EOF) {
                return new String(charBuffer, 0, i);
            }
            bufIndex = 0;
        }
    }

    public double nextDouble() throws IOException {
        String doubleVal = nextString();
        if (doubleVal == null) {
            throw new IOException();
        }
        return Double.parseDouble(doubleVal);
    }

    public double nextDoubleFast() throws IOException {
        c = read();
        int sgn = 1;
        while (c <= SPACE) {
            c = read();
        }
        if (c == DASH) {
            sgn = -1;
            c = read();
        }
        double res = 0.0;
        while (c > DOT) {
            res *= 10.0;
            res += ints[c];
            c = read();
        }
        if (c == DOT) {
            int i = 0;
            c = read();
            while (c > SPACE && i < MAX_DECIMAL_PRECISION) {
                res += doubles[ints[c]][i++];
                c = read();
            }
        }
        return res * sgn;
    }

    public byte[] nextByteArray(int n) throws IOException {
        byte[] ar = new byte[n];
        for (int i = 0; i < n; i++) {
            ar[i] = nextByte();
        }
        return ar;
    }

    public int[] nextIntArray(int n) throws IOException {
        int[] ar = new int[n];
        for (int i = 0; i < n; i++) {
            ar[i] = nextInt();
        }
        return ar;
    }

    public long[] nextLongArray(int n) throws IOException {
        long[] ar = new long[n];
        for (int i = 0; i < n; i++) {
            ar[i] = nextLong();
        }
        return ar;
    }

    public double[] nextDoubleArray(int n) throws IOException {
        double[] ar = new double[n];
        for (int i = 0; i < n; i++) {
            ar[i] = nextDouble();
        }
        return ar;
    }

    public double[] nextDoubleArrayFast(int n) throws IOException {
        double[] ar = new double[n];
        for (int i = 0; i < n; i++) {
            ar[i] = nextDoubleFast();
        }
        return ar;
    }

    public String[] nextStringArray(int n) throws IOException {
        String[] ar = new String[n];
        for (int i = 0; i < n; i++) {
            String str = nextString();
            if (str == null) {
                throw new IOException();
            }
            ar[i] = str;
        }
        return ar;
    }

    public byte[] nextByteArray1(int n) throws IOException {
        byte[] ar = new byte[n + 1];
        for (int i = 1; i <= n; i++) {
            ar[i] = nextByte();
        }
        return ar;
    }

    public int[] nextIntArray1(int n) throws IOException {
        int[] ar = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            ar[i] = nextInt();
        }
        return ar;
    }

    // Read a 1-based long array of size n+1
    public long[] nextLongArray1(int n) throws IOException {
        long[] ar = new long[n + 1];
        for (int i = 1; i <= n; i++) {
            ar[i] = nextLong();
        }
        return ar;
    }

    // Read a 1-based double array of size n+1
    public double[] nextDoubleArray1(int n) throws IOException {
        double[] ar = new double[n + 1];
        for (int i = 1; i <= n; i++) {
            ar[i] = nextDouble();
        }
        return ar;
    }

    // Quickly read a 1-based double array of size n+1
    public double[] nextDoubleArrayFast1(int n) throws IOException {
        double[] ar = new double[n + 1];
        for (int i = 1; i <= n; i++) {
            ar[i] = nextDoubleFast();
        }
        return ar;
    }

    // Read a 1-based string array of size n+1
    public String[] nextStringArray1(int n) throws IOException {
        String[] ar = new String[n + 1];
        for (int i = 1; i <= n; i++) {
            ar[i] = nextString();
        }
        return ar;
    }

    // Read a two dimensional matrix of bytes of size rows x cols
    public byte[][] nextByteMatrix(int rows, int cols) throws IOException {
        byte[][] matrix = new byte[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = nextByte();
            }
        }
        return matrix;
    }

    // Read a two dimensional matrix of ints of size rows x cols
    public int[][] nextIntMatrix(int rows, int cols) throws IOException {
        int[][] matrix = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = nextInt();
            }
        }
        return matrix;
    }

    // Read a two dimensional matrix of longs of size rows x cols
    public long[][] nextLongMatrix(int rows, int cols) throws IOException {
        long[][] matrix = new long[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = nextLong();
            }
        }
        return matrix;
    }

    // Read a two dimensional matrix of doubles of size rows x cols
    public double[][] nextDoubleMatrix(int rows, int cols) throws IOException {
        double[][] matrix = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = nextDouble();
            }
        }
        return matrix;
    }

    // Quickly read a two dimensional matrix of doubles of size rows x cols
    public double[][] nextDoubleMatrixFast(int rows, int cols) throws IOException {
        double[][] matrix = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = nextDoubleFast();
            }
        }
        return matrix;
    }

    // Read a two dimensional matrix of Strings of size rows x cols
    public String[][] nextStringMatrix(int rows, int cols) throws IOException {
        String[][] matrix = new String[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = nextString();
            }
        }
        return matrix;
    }

    // Read a 1-based two dimensional matrix of bytes of size rows x cols
    public byte[][] nextByteMatrix1(int rows, int cols) throws IOException {
        byte[][] matrix = new byte[rows + 1][cols + 1];
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= cols; j++) {
                matrix[i][j] = nextByte();
            }
        }
        return matrix;
    }

    // Read a 1-based two dimensional matrix of ints of size rows x cols
    public int[][] nextIntMatrix1(int rows, int cols) throws IOException {
        int[][] matrix = new int[rows + 1][cols + 1];
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= cols; j++) {
                matrix[i][j] = nextInt();
            }
        }
        return matrix;
    }

    // Read a 1-based two dimensional matrix of longs of size rows x cols
    public long[][] nextLongMatrix1(int rows, int cols) throws IOException {
        long[][] matrix = new long[rows + 1][cols + 1];
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= cols; j++) {
                matrix[i][j] = nextLong();
            }
        }
        return matrix;
    }

    // Read a 1-based two dimensional matrix of doubles of size rows x cols
    public double[][] nextDoubleMatrix1(int rows, int cols) throws IOException {
        double[][] matrix = new double[rows + 1][cols + 1];
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= cols; j++) {
                matrix[i][j] = nextDouble();
            }
        }
        return matrix;
    }

    // Quickly read a 1-based two dimensional matrix of doubles of size rows x cols
    public double[][] nextDoubleMatrixFast1(int rows, int cols) throws IOException {
        double[][] matrix = new double[rows + 1][cols + 1];
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= cols; j++) {
                matrix[i][j] = nextDoubleFast();
            }
        }
        return matrix;
    }

    // Read a 1-based two dimensional matrix of Strings of size rows x cols
    public String[][] nextStringMatrix1(int rows, int cols) throws IOException {
        String[][] matrix = new String[rows + 1][cols + 1];
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= cols; j++) {
                matrix[i][j] = nextString();
            }
        }
        return matrix;
    }

    public void close() throws IOException {
        stream.close();
    }
}
