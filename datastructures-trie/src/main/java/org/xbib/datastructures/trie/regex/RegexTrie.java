package org.xbib.datastructures.trie.regex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * The RegexTrie is a trie where each _stored_ segment of the key is a regex {@link Pattern}.  Thus,
 * the full _stored_ key is a List<Pattern> rather than a String as in a standard trie.  Note that
 * the retrieve method requires a List<String>, which will be matched against the
 * {@link Pattern}s, rather than checked for equality as in a standard trie. It will likely perform
 * poorly for large datasets.
 * <p/>
 * One can also use a {@code null} entry in the {@code Pattern} sequence to serve as a wildcard.  If
 * a {@code null} is encountered, all subsequent entries in the sequence will be ignored.
 * When the retrieval code encounters a {@code null} {@code Pattern}, it will first wait to see if a
 * more-specific entry matches the sequence.  If one does, that more-specific entry will proceed,
 * even if it subsequently fails to match.
 * <p/>
 * If no more-specific entry matches, the wildcard match will add all remaining {@code String}s
 * to the list of captures (if enabled) and return the value associated with the wildcard.
 * <p/>
 * A short sample of the wildcard functionality:
 * <pre>
 * List&lt;List&lt;String&gt;&gt; captures = new LinkedList&lt;List&lt;String&gt;&gt;();
 * RegexTrie<Integer> trie = new RegexTrie<Integer>();
 * trie.put(2, "a", null);
 * trie.put(4, "a", "b");
 * trie.retrieve(captures, "a", "c", "e");
 * // returns 2.  captures is now [[], ["c"], ["e"]]
 * trie.retrieve(captures, "a", "b");
 * // returns 4.  captures is now [[], []]
 * trie.retrieve(captures, "a", "b", "c");
 * // returns null.  captures is now [[], []]
 * </pre>
 */
public class RegexTrie<V> {

    private V value;

    private final Map<CompPattern, RegexTrie<V>> children;

    public RegexTrie() {
        children = new LinkedHashMap<>();
    }

    public void clear() {
        value = null;
        for (RegexTrie<V> child : children.values()) {
            child.clear();
        }
        children.clear();
    }

    public boolean containsKey(List<String> strings) {
        return resolve(strings) != null;
    }

    public void add(String pattern, V value) {
        put(value, Arrays.stream(pattern.split("/")).distinct().collect(Collectors.toList()));
    }

    /**
     * Add an entry to the trie.
     *
     * @param value The value to set
     * @param patterns The sequence of {@link Pattern}s that must be sequentially matched to
     *        retrieve the associated {@code value}
     */
    public void put(V value, List<?> patterns) {
        List<CompPattern> list = new ArrayList<>(patterns.size());
        for (Object object : patterns) {
            CompPattern compPattern = null;
            if (object instanceof Pattern) {
                compPattern = new CompPattern((Pattern) object);
            } else if (object instanceof String) {
                compPattern = new CompPattern(Pattern.compile((String) object));
            }
            list.add(compPattern);
        }
        validateAndPut(value, list);
    }

    /**
     * Resolve a value from the trie, by matching the provided sequence of {@link String}s to a
     * sequence of {@link Pattern}s stored in the trie.
     *
     * @param strings A sequence of {@link String}s to match
     * @return The associated value, or {@code null} if no value was found
     */
    public V resolve(List<String> strings) {
        return resolve(null, strings);
    }

    /**
     * Resolve a value from the trie, by matching the provided sequence of {@link String}s to a
     * sequence of {@link Pattern}s stored in the trie.  This version of the method also returns
     * a {@link List} of capture groups for each {@link Pattern} that was matched.
     * <p/>
     * Each entry in the outer List corresponds to one level of {@code Pattern} in the trie.
     * For each level, the list of capture groups will be stored.  If there were no captures
     * for a particular level, an empty list will be stored.
     * <p/>
     * Note that {@code captures} will be {@link List#clear()}ed before the retrieval begins.
     * Also, if the retrieval fails after a partial sequence of matches, {@code captures} will
     * still reflect the capture groups from the partial match.
     *
     * @param captures A {@code List<List<String>>} through which capture groups will be returned.
     * @param strings A sequence of {@link String}s to match
     * @return The associated value, or {@code null} if no value was found
     */
    public V resolve(List<List<String>> captures, List<String> strings) {
        if (strings.size() == 0) {
            throw new IllegalArgumentException("string list must be non-empty");
        }
        if (captures != null) {
            captures.clear();
        }
        return recursiveRetrieve(captures, strings);
    }

    /**
     * A helper method to consolidate validation before adding an entry to the trie.
     *
     * @param value The value to set
     * @param list The sequence of {@link CompPattern}s that must be sequentially matched to
     *        retrieve the associated {@code value}
     */
    private V validateAndPut(V value, List<CompPattern> list) {
        if (list.size() == 0) {
            throw new IllegalArgumentException("pattern list must be non-empty");
        }
        return recursivePut(value, list);
    }

    private V recursivePut(V value, List<CompPattern> patterns) {
        // Cases:
        // 1) patterns is empty -- set our value
        // 2) patterns is non-empty -- recurse downward, creating a child if necessary
        if (patterns.isEmpty()) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        } else {
            CompPattern curKey = patterns.get(0);
            List<CompPattern> nextKeys = patterns.subList(1, patterns.size());
            // Create a new child to handle
            RegexTrie<V> nextChild = children.get(curKey);
            if (nextChild == null) {
                nextChild = new RegexTrie<V>();
                children.put(curKey, nextChild);
            }
            return nextChild.recursivePut(value, nextKeys);
        }
    }

    private V recursiveRetrieve(List<List<String>> captures, List<String> strings) {
        // Cases:
        // 1) strings is empty -- return our value
        // 2) strings is non-empty -- find the first child that matches, recurse downward
        if (strings.isEmpty()) {
            return value;
        } else {
            boolean wildcardMatch = false;
            V wildcardValue = null;
            String curKey = strings.get(0);
            List<String> nextKeys = strings.subList(1, strings.size());
            for (Map.Entry<CompPattern, RegexTrie<V>> child : children.entrySet()) {
                CompPattern pattern = child.getKey();
                if (pattern == null) {
                    wildcardMatch = true;
                    wildcardValue = child.getValue().value;
                    continue;
                }
                Matcher matcher = pattern.matcher(curKey);
                if (matcher.matches()) {
                    if (captures != null) {
                        List<String> curCaptures = new ArrayList<>(matcher.groupCount());
                        for (int i = 0; i < matcher.groupCount(); i++) {
                            // i+1 since group 0 is the entire matched string
                            curCaptures.add(matcher.group(i + 1));
                        }
                        captures.add(curCaptures);
                    }
                    return child.getValue().recursiveRetrieve(captures, nextKeys);
                }
            }
            if (wildcardMatch) {
                // stick the rest of the query string into the captures list and return
                if (captures != null) {
                    for (String str : strings) {
                        captures.add(List.of(str));
                    }
                }
                return wildcardValue;
            }
            // no match
            return null;
        }
    }

    @Override
    public String toString() {
        return String.format("{V: %s, C: %s}", value, children);
    }

    /**
     * Patterns aren't comparable by default, which prevents you from retrieving them from a Map.
     * This is a simple stub class that makes a Pattern with a working
     * {@link CompPattern#equals(Object)} method.
     */
    private static class CompPattern {

        protected final Pattern pattern;

        CompPattern(Pattern pattern) {
            Objects.requireNonNull(pattern);
            this.pattern = pattern;
        }

        @Override
        public boolean equals(Object other) {
            Pattern otherPat;
            if (other instanceof Pattern) {
                otherPat = (Pattern) other;
            } else if (other instanceof CompPattern) {
                CompPattern otherCPat = (CompPattern) other;
                otherPat = otherCPat.pattern;
            } else {
                return false;
            }
            return pattern.toString().equals(otherPat.toString());
        }

        @Override
        public int hashCode() {
            return pattern.toString().hashCode();
        }

        @Override
        public String toString() {
            return String.format("P(%s)", pattern);
        }

        public Matcher matcher(String string) {
            return pattern.matcher(string);
        }
    }
}
