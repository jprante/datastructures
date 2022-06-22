package org.xbib.datastructures.trie.segment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TrieKeyImpl<T> implements TrieKey<T>, Comparable<TrieKeyImpl<T>> {

    private final List<TrieKeySegment<T>> segments;

    public TrieKeyImpl() {
        this(new ArrayList<>());
    }

    public TrieKeyImpl(List<TrieKeySegment<T>> segments) {
        this.segments = segments;
    }

    public TrieKeyImpl<T> add(TrieKeySegment<T> segment) {
        segments.add(segment);
        return this;
    }

    @Override
    public List<TrieKeySegment<T>> getSegments() {
        return segments;
    }

    @Override
    public int size() {
        return segments.size();
    }

    @Override
    public TrieKey<T> subKey(int i) {
        return new TrieKeyImpl<>(segments.subList(1, segments.size()));
    }

    @Override
    public TrieKey<T> append(TrieKeySegment<T> trieKeySegment) {
        segments.add(trieKeySegment);
        return this;
    }

    @Override
    public void set(int i, TrieKeySegment<T> trieKeySegment) {
        segments.set(i, trieKeySegment);
    }

    @Override
    public TrieKeySegment<T> get(int i) {
        return segments.get(i);
    }

    @SuppressWarnings("unchecked")
    @Override
    public int compareTo(TrieKeyImpl<T> o) {
        for (int i = 0; i < segments.size(); i++) {
            TrieKeySegment<T> segment1 = segments.get(i);
            T segment2 = i < o.segments.size() ? (T) o.segments.get(i) : null;
            if (segment2 == null) {
                return 1;
            }
            int c = segment1.compareTo(segment2);
            if (c != 0) {
                return c;
            }
        }
        return 0;
    }

    public static TrieKey<String> stringKey(String... segments ) {
        TrieKey<String> trieKey = new TrieKeyImpl<>();
        Arrays.stream(segments).forEach(s -> {
            trieKey.append(new StringSegment(s));
        });
        return trieKey;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (TrieKeySegment<T> segment : segments) {
            sb.append(segment.toString());
        }
        return sb.toString();
    }
}
