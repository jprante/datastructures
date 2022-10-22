package org.xbib.datastructures.trie.segment;

import java.util.regex.Pattern;

public class PatternSegment implements TrieKeySegment<Pattern> {

    private final Pattern segment;

    public PatternSegment(Pattern segment) {
        this.segment = segment;
    }

    public Pattern getSegment() {
        return segment;
    }

    public static PatternSegment of(Pattern segment) {
        return new PatternSegment(segment);
    }

    @Override
    public int compareTo(Pattern o) {
        return segment.pattern().compareTo(o.pattern());
    }

    @Override
    public String toString() {
        return segment.pattern();
    }
}
