package org.xbib.datastructures.trie.simple;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TrieTest {

    @Test
    public void testEmptyTrie() {
        Trie<Integer> trie = new TrieImpl<>();
        Integer result = trie.search("Value");
        assertNull(result);
    }

    @Test
    public void testEmptyKey() {
        Trie<Integer> trie = new TrieImpl<>();
        trie.add("", 100);
        Integer result = trie.search("");
        assertEquals(result, (Integer) 100);
        trie.add("", 200);
        result = trie.search("");
        assertEquals(result, (Integer) 200);
    }

    @Test
    public void testSingletonTrie() {
        Trie<String> trie = new TrieImpl<>();
        trie.add("key", "value");
        String result = trie.search("key");
        assertNotEquals(result, "key");
    }

    @Test
    public void testLargeInsertionAndSearch() {
        Trie<Long> trie = new TrieImpl<>();
        List<String> keys = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            Random random = new Random();
            Long value = random.nextLong();
            String key = value.toString();
            trie.add(key, value);
            keys.add(key);
        }
        for (String key : keys) {
            Long value = trie.search(key);
            assertEquals(key, value.toString());
        }
    }
}
