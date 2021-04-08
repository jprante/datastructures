package org.xbib.datastructures.json;

/**
 *
 */
public class HashIndexTable {

    private final byte[] hashTable = new byte[32];

    public void add(String name, int index) {
        int slot = hashSlotFor(name);
        if (index < 0xff) {
            hashTable[slot] = (byte) (index + 1);
        } else {
            hashTable[slot] = 0;
        }
    }

    public void remove(int index) {
        for (int i = 0; i < hashTable.length; i++) {
            if (hashTable[i] == index + 1) {
                hashTable[i] = 0;
            } else if (hashTable[i] > index + 1) {
                hashTable[i]--;
            }
        }
    }

    public int get(Object name) {
        int slot = hashSlotFor(name);
        return (hashTable[slot] & 0xff) - 1;
    }

    private int hashSlotFor(Object element) {
        return element.hashCode() & hashTable.length - 1;
    }
}
