package org.xbib.datastructures.trie.patricia;

/**
 * A {@link KeyAnalyzer} for {@link Character}s.
 */
public class CharacterKeyAnalyzer extends AbstractKeyAnalyzer<Character> {

    /**
     * A {@link CharacterKeyAnalyzer} that uses all bits (16) of a {@code char}.
     */
    public static final CharacterKeyAnalyzer CHAR = new CharacterKeyAnalyzer(Character.SIZE);

    /**
     * A {@link CharacterKeyAnalyzer} that uses only the lower 8bits of a {@code char}.
     */
    public static final CharacterKeyAnalyzer BYTE = new CharacterKeyAnalyzer(Byte.SIZE);

    public static final CharacterKeyAnalyzer INSTANCE = CHAR;

    private final int size;

    private final int msb;

    private CharacterKeyAnalyzer(int size) {
        this(size, 1 << size - 1);
    }

    private CharacterKeyAnalyzer(int size, int msb) {
        this.size = size;
        this.msb = msb;
    }

    /**
     * Returns a bit mask where the given bit is set
     */
    private int mask(int bit) {
        return msb >>> bit;
    }

    private char valueOf(Character ch) {
        char value = ch;
        if (size == Byte.SIZE) {
            value &= 0xFF;
        }
        return value;
    }

    @Override
    public int lengthInBits(Character key) {
        return size;
    }

    @Override
    public boolean isBitSet(Character key, int bitIndex) {
        return (key & mask(bitIndex)) != 0;
    }

    @Override
    public int bitIndex(Character key, Character otherKey) {
        char ch1 = valueOf(key);
        char ch2 = valueOf(otherKey);

        if (ch1 == 0) {
            return NULL_BIT_KEY;
        }

        if (ch1 != ch2) {
            int xor = ch1 ^ ch2;
            for (int i = 0; i < size; i++) {
                if ((xor & mask(i)) != 0) {
                    return i;
                }
            }
        }

        return KeyAnalyzer.EQUAL_BIT_KEY;
    }

    @Override
    public boolean isPrefix(Character key, Character prefix) {
        return key.equals(prefix);
    }
}
