package org.xbib.datastructures.trie.patricia;

/**
 * A {@link KeyAnalyzer} for {@link String}s.
 */
public class StringKeyAnalyzer extends AbstractKeyAnalyzer<String> {

    /**
     * A {@link StringKeyAnalyzer} that uses all bits (16) of a {@code char}.
     */
    public static final StringKeyAnalyzer CHAR = new StringKeyAnalyzer(Character.SIZE);

    /**
     * A {@link StringKeyAnalyzer} that uses only the lower 8 bits of each {@code char}.
     */
    public static final StringKeyAnalyzer BYTE = new StringKeyAnalyzer(Byte.SIZE);

    public static final StringKeyAnalyzer INSTANCE = CHAR;

    private final int size;

    private final int msb;

    private StringKeyAnalyzer(int size) {
        this(size, 1 << size - 1);
    }

    private StringKeyAnalyzer(int size, int msb) {
        this.size = size;
        this.msb = msb;
    }

    /**
     * Returns a bit mask where the given bit is set
     */
    private int mask(int bit) {
        return msb >>> bit;
    }

    /**
     * Returns the {@code char} at the given index.
     */
    private char valueAt(String value, int index) {
        if (index < value.length()) {
            char ch = value.charAt(index);
            if (size == Byte.SIZE) {
                ch &= 0xFF;
            }
            return ch;
        }
        return 0;
    }

    @Override
    public int lengthInBits(String key) {
        return key.length() * size;
    }

    @Override
    public boolean isBitSet(String key, int bitIndex) {
        if (bitIndex >= lengthInBits(key)) {
            return false;
        }

        int index = bitIndex / size;
        int bit = bitIndex % size;

        return (key.charAt(index) & mask(bit)) != 0;
    }

    @Override
    public boolean isPrefix(String key, String prefix) {
        return key.startsWith(prefix);
    }

    @Override
    public int bitIndex(String key, String otherKey) {

        boolean allNull = true;
        int length = Math.max(key.length(), otherKey.length());

        for (int i = 0; i < length; i++) {

            char ch1 = valueAt(key, i);
            char ch2 = valueAt(otherKey, i);

            if (ch1 != ch2) {
                int xor = ch1 ^ ch2;
                for (int j = 0; j < size; j++) {
                    if ((xor & mask(j)) != 0) {
                        return (i * size) + j;
                    }
                }
            }

            if (ch1 != 0) {
                allNull = false;
            }
        }

        // All bits are 0
        if (allNull) {
            return KeyAnalyzer.NULL_BIT_KEY;
        }

        // Both keys are equal
        return KeyAnalyzer.EQUAL_BIT_KEY;
    }
}
