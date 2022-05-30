package org.xbib.datastructures.trie.patricia;

/**
 * A {@link KeyAnalyzer} for {@link Integer}s.
 */
public class IntegerKeyAnalyzer extends AbstractKeyAnalyzer<Integer> {

    public static final IntegerKeyAnalyzer INSTANCE = new IntegerKeyAnalyzer();

    /**
     * A bit mask where the first bit is 1 and the others are zero
     */
    private static final int MSB = 1 << Integer.SIZE - 1;

    /**
     * Returns a bit mask where the given bit is set
     */
    private static int mask(int bit) {
        return MSB >>> bit;
    }

    @Override
    public int lengthInBits(Integer key) {
        return Integer.SIZE;
    }

    @Override
    public boolean isBitSet(Integer key, int bitIndex) {
        return (key & mask(bitIndex)) != 0;
    }

    @Override
    public int bitIndex(Integer key, Integer otherKey) {
        int keyValue = key.intValue();
        if (keyValue == 0) {
            return NULL_BIT_KEY;
        }

        int otherValue = otherKey.intValue();

        if (keyValue != otherValue) {
            int xorValue = keyValue ^ otherValue;
            for (int i = 0; i < Integer.SIZE; i++) {
                if ((xorValue & mask(i)) != 0) {
                    return i;
                }
            }
        }

        return KeyAnalyzer.EQUAL_BIT_KEY;
    }

    @Override
    public boolean isPrefix(Integer key, Integer prefix) {
        return key.equals(prefix);
    }
}
