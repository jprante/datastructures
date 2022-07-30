package org.xbib.datastructures.trie.regex;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RegexTrieTest {

    @Test
    public void testRegexTrie() {
        List<List<String>> captures = new LinkedList<>();
        RegexTrie<Integer> trie = new RegexTrie<>();
        trie.put(2, List.of("a", ""));
        trie.put(4, List.of("a", "b"));
        assertEquals(2, trie.resolve(captures, List.of("a", "c", "e")));
        // returns 2.  captures is now [[], ["c"], ["e"]]
        assertEquals(4, trie.resolve(captures, List.of("a", "b")));
        // returns 4.  captures is now [[], []]
        assertNull(trie.resolve(captures, List.of("a", "b", "c")));
        // returns null.  captures is now [[], []]
    }
}
