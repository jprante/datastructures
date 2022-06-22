package org.xbib.datastructures.trie.radix;

/**
 * Exception thrown if a duplicate key is inserted in a {@link RadixTree}
 */
@SuppressWarnings("serial")
public class DuplicateKeyException extends RuntimeException {

    public DuplicateKeyException(String msg) {
        super(msg);
    }
}
