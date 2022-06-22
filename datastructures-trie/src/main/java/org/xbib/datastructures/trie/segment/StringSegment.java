package org.xbib.datastructures.trie.segment;

public class StringSegment implements TrieKeySegment<String> {

    private final String segment;

    public StringSegment(String segment) {
        this.segment = segment;
    }

    public static StringSegment of(String segment) {
        return new StringSegment(segment);
    }

    @Override
    public int compareTo(String o) {
        return segment.compareTo(o);
    }

    @Override
    public String toString() {
        return segment;
    }
}
