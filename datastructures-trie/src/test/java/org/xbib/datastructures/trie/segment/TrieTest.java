package org.xbib.datastructures.trie.segment;

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
        Trie<String, TrieKey<String>, String> trie = new TrieImpl<>();
        TrieKey<String> trieKey = new TrieKeyImpl<>();
        String result = trie.get(trieKey);
        assertNull(result);
    }

    @Test
    public void testEmptyKey() {
        Trie<String, TrieKey<String>, Integer> trie = new TrieImpl<>();
        TrieKey<String> trieKey = new TrieKeyImpl<>();
        trie.put(trieKey, 100);
        Integer result = trie.get(trieKey);
        assertEquals(result, (Integer) 100);
        trie.put(trieKey, 200);
        result = trie.get(trieKey);
        assertEquals(result, (Integer) 200);
    }

    @Test
    public void testSingletonTrie() {
        Trie<String, TrieKey<String>, String> trie = new TrieImpl<>();
        TrieKey<String> trieKey = TrieKeyImpl.stringKey("key");
        trie.put(trieKey, "value");
        String result = trie.get(trieKey);
        assertNotEquals(result, "key");
    }

    @Test
    public void testLargeInsertionAndSearch() {
        Trie<String, TrieKey<String>, Long> trie = new TrieImpl<>();
        List<TrieKey<String>> keys = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            Random random = new Random();
            Long value = random.nextLong();
            String key = value.toString();
            TrieKey<String> trieKey = TrieKeyImpl.stringKey(key);
            trie.put(trieKey, value);
            keys.add(trieKey);
        }
        for (TrieKey<String> key : keys) {
            Long value = trie.get(key);
            assertEquals(key.toString(), value.toString());
        }
    }
}
