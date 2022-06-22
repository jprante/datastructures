package org.xbib.datastructures.trie.ahocorasick;

import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TrieTest {

    private static final SecureRandom random = new SecureRandom();

    private final static String[] ALPHABET = new String[] { "abc", "bcd", "cde" };
    private final static String[] ALPHABET_PAYLOAD = new String[] { "alpha:abc", "alpha:bcd", "alpha:cde" };

    private final static List<Entry<String>> ALPHABET_WITH_ENTRIES = Arrays.asList(
            new Entry<>( ALPHABET[ 0 ], ALPHABET_PAYLOAD[ 0 ] ),
            new Entry<>( ALPHABET[ 1 ], ALPHABET_PAYLOAD[ 1 ] ),
            new Entry<>( ALPHABET[ 2 ], ALPHABET_PAYLOAD[ 2 ] ));

    private final static String[] PRONOUNS = new String[] { "hers", "his", "she", "he" };
    private final static int[] PRONOUNS_PAYLOAD_ID = new int[] { 9, 12, 4, 20 };

    private final static List<Entry<Integer>> PRONOUNS_WITH_ENTRIES = Arrays.asList(
        new Entry<>( PRONOUNS[ 0 ], PRONOUNS_PAYLOAD_ID[ 0 ] ),
        new Entry<>( PRONOUNS[ 1 ], PRONOUNS_PAYLOAD_ID[ 1 ] ),
        new Entry<>( PRONOUNS[ 2 ], PRONOUNS_PAYLOAD_ID[ 2 ] ),
        new Entry<>( PRONOUNS[ 3 ], PRONOUNS_PAYLOAD_ID[ 3 ] )
    );

    private final static String[] FOOD = new String[] { "veal", "cauliflower", "broccoli", "tomatoes" };
    private final static Food[] FOOD_PAYLOAD = new Food[] { new Food("veal"), new Food("cauliflower"), new Food("broccoli"),
            new Food("tomatoes") };

    private final static List<Entry<Food>> FOOD_WITH_ENTRIES = Arrays.asList(
        new Entry<>( FOOD[ 0 ], FOOD_PAYLOAD[ 0 ] ),
        new Entry<>( FOOD[ 1 ], FOOD_PAYLOAD[ 1 ] ),
        new Entry<>( FOOD[ 2 ], FOOD_PAYLOAD[ 2 ] ),
        new Entry<>( FOOD[ 3 ], FOOD_PAYLOAD[ 3 ] )
    );

    private final static String[] GREEK_LETTERS = new String[] { "Alpha", "Beta", "Gamma" };
    private final static String[] GREEK_LETTERS_PAYLOAD = new String[] { "greek:Alpha", "greek:Beta", "greek:Gamma" };

    private final static List<Entry<String>> GREEK_LETTERS_WITH_ENTRIES = Arrays.asList(
        new Entry<>( GREEK_LETTERS[ 0 ], GREEK_LETTERS_PAYLOAD[ 0 ] ),
        new Entry<>( GREEK_LETTERS[ 1 ], GREEK_LETTERS_PAYLOAD[ 1 ] ),
        new Entry<>( GREEK_LETTERS[ 2 ], GREEK_LETTERS_PAYLOAD[ 2 ] ));

    private final static String[] UNICODE = new String[] { "turning", "once", "again", "börkü" };
    private final static String[] UNICODE_PAYLOAD = new String[] { "uni:turning", "uni:once", "uni:again", "uni:börkü" };

    private final static List<Entry<String>> UNICODE_WITH_ENTRIES = Arrays.asList(
        new Entry<>( UNICODE[ 0 ], UNICODE_PAYLOAD[ 0 ] ),
        new Entry<>( UNICODE[ 1 ], UNICODE_PAYLOAD[ 1 ] ),
        new Entry<>( UNICODE[ 2 ], UNICODE_PAYLOAD[ 2 ] ),
        new Entry<>( UNICODE[ 3 ], UNICODE_PAYLOAD[ 3 ] ));

    public static class Food {
        private final String name;

        public Food(String name) {
            this.name = name;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            return result;
        }

        @Override
        public boolean equals( Object obj ) {
            if( this == obj ) {
                return true;
            }
            if( obj == null ) {
                return false;
            }
            if( getClass() != obj.getClass() ) {
                return false;
            }
            Food other = (Food) obj;
            if( name == null ) {
                return other.name == null;
            }
            else {
                return name.equals( other.name );
            }
        }
    }

    @Test
    public void keyAndTextAreTheSame() {
        Trie<String> trie = Trie.<String>builder()
                .add(ALPHABET[0], ALPHABET_PAYLOAD[0])
                .build();
        Collection<EntryOutput<String>> outputs = trie.parse(ALPHABET[0]);
        Iterator<EntryOutput<String>> iterator = outputs.iterator();
        checkOutput(iterator.next(), 0, 2, ALPHABET[0], ALPHABET_PAYLOAD[0]);
    }

    @Test
    public void keyAndTextAreTheSameFirstMatch() {
        Trie<String> trie = Trie.<String>builder()
                .add(ALPHABET[0], ALPHABET_PAYLOAD[0])
                .build();
        EntryOutput<String> firstMatch = trie.firstMatch(ALPHABET[0]);
        checkOutput(firstMatch, 0, 2, ALPHABET[0], ALPHABET_PAYLOAD[0]);
    }

    @Test
    public void textIsLongerThanKey() {
        Trie<String> trie = Trie.<String>builder()
                .add(ALPHABET[0], ALPHABET_PAYLOAD[0])
                .build();
        Collection<EntryOutput<String>> emits = trie.parse(" " + ALPHABET[0]);
        Iterator<EntryOutput<String>> iterator = emits.iterator();
        checkOutput(iterator.next(), 1, 3, ALPHABET[0], ALPHABET_PAYLOAD[0]);
    }

    @Test
    public void textIsLongerThanKeyFirstMatch() {
        Trie<String> trie = Trie.<String>builder()
                .add(ALPHABET[0], ALPHABET_PAYLOAD[0])
                .build();
        EntryOutput<String> firstMatch = trie.firstMatch(" " + ALPHABET[0]);
        checkOutput(firstMatch, 1, 3, ALPHABET[0], ALPHABET_PAYLOAD[0]);
    }

    @Test
    public void variousKeysOneMatch() {
        Trie<String> trie = Trie.<String>builder()
                .add(ALPHABET_WITH_ENTRIES)
                .build();
        Collection<EntryOutput<String>> outputs = trie.parse("bcd");
        Iterator<EntryOutput<String>> iterator = outputs.iterator();
        checkOutput(iterator.next(), 0, 2, "bcd", "alpha:bcd");
    }

    @Test
    public void variousKeysFirstMatch() {
        Trie<String> trie = Trie.<String>builder().add(ALPHABET_WITH_ENTRIES).build();
        EntryOutput<String> firstMatch = trie.firstMatch("bcd");
        checkOutput(firstMatch, 0, 2, "bcd", "alpha:bcd");
    }

    @Test
    public void ushersTestAndStopOnHit() {
        Trie<Integer> trie = Trie.<Integer>builder().add(PRONOUNS_WITH_ENTRIES).stopOnHit().build();
        Collection<EntryOutput<Integer>> emits = trie.parse("ushers");
        assertEquals(1, emits.size()); // she @ 3, he @ 3, hers @ 5
        Iterator<EntryOutput<Integer>> iterator = emits.iterator();
        checkOutput(iterator.next(), 2, 3, "he", 20);
    }

    @Test
    public void ushersTestStopOnHitSkipOne() {
        Trie<Integer> trie = Trie.<Integer>builder().add(PRONOUNS_WITH_ENTRIES).stopOnHit().build();

        CollectingOutputHandler<Integer> testEmitHandler = new AbstractCollectingOutputHandler<>() {
            boolean first = true;

            @Override
            public boolean output(final EntryOutput<Integer> emit) {
                if (first) {
                    // return false for the first element
                    first = false;
                    return false;
                }
                add(emit);
                return true;
            }

        };

        trie.parse("ushers", testEmitHandler);
        Collection<EntryOutput<Integer>> emits = testEmitHandler.getOutputs();
        assertEquals(1, emits.size()); // she @ 3, he @ 3, hers @ 5
        Iterator<EntryOutput<Integer>> iterator = emits.iterator();
        checkOutput(iterator.next(), 1, 3, "she", 4);
    }

    @Test
    public void ushersTest() {
        Trie<Integer> trie = Trie.<Integer>builder().add(PRONOUNS_WITH_ENTRIES).build();
        Collection<EntryOutput<Integer>> emits = trie.parse("ushers");
        assertEquals(3, emits.size()); // she @ 3, he @ 3, hers @ 5
        Iterator<EntryOutput<Integer>> iterator = emits.iterator();

        checkOutput(iterator.next(), 2, 3, "he", 20);
        checkOutput(iterator.next(), 1, 3, "she", 4);
        checkOutput(iterator.next(), 2, 5, "hers", 9);
    }

    @Test
    public void ushersTestWithCapitalKeywords() {
        Trie<String> trie = Trie.<String>builder().ignoreCase().add("HERS", "hers").add("HIS", "his")
                .add("SHE", "she").add("HE", "he").build();
        Collection<EntryOutput<String>> emits = trie.parse("ushers");
        assertEquals(3, emits.size()); // she @ 3, he @ 3, hers @ 5
        Iterator<EntryOutput<String>> iterator = emits.iterator();
        checkOutput(iterator.next(), 2, 3, "HE", "he");
        checkOutput(iterator.next(), 1, 3, "SHE", "she");
        checkOutput(iterator.next(), 2, 5, "HERS", "hers");
    }

    @Test
    public void ushersTestFirstMatch() {
        Trie<Integer> trie = Trie.<Integer>builder().add(PRONOUNS_WITH_ENTRIES).build();
        EntryOutput<Integer> firstMatch = trie.firstMatch("ushers");
        checkOutput(firstMatch, 2, 3, "he", 20);
    }

    @Test
    public void ushersTestByCallback() {
        Trie<Integer> trie = Trie.<Integer>builder().add(PRONOUNS_WITH_ENTRIES).build();

        final List<EntryOutput<Integer>> emits = new LinkedList<>();
        OutputHandler<Integer> emitHandler = emit -> {
            emits.add(emit);
            return true;
        };
        trie.parse("ushers", emitHandler);
        assertEquals(3, emits.size()); // she @ 3, he @ 3, hers @ 5
        Iterator<EntryOutput<Integer>> iterator = emits.iterator();

        checkOutput(iterator.next(), 2, 3, "he", 20);
        checkOutput(iterator.next(), 1, 3, "she", 4);
        checkOutput(iterator.next(), 2, 5, "hers", 9);
    }

    @Test
    public void misleadingTest() {
        Trie<String> trie = Trie.<String>builder().add("hers", "pronon:hers").build();
        Collection<EntryOutput<String>> emits = trie.parse("h he her hers");
        Iterator<EntryOutput<String>> iterator = emits.iterator();
        checkOutput(iterator.next(), 9, 12, "hers", "pronon:hers");
    }

    @Test
    public void misleadingTestFirstMatch() {
        Trie<String> trie = Trie.<String>builder().add("hers", "pronon:hers").build();
        EntryOutput<String> firstMatch = trie.firstMatch("h he her hers");
        checkOutput(firstMatch, 9, 12, "hers", "pronon:hers");
    }

    @Test
    public void recipes() {
        Trie<Food> trie = Trie.<Food>builder().add(FOOD_WITH_ENTRIES).build();
        Collection<EntryOutput<Food>> emits = trie.parse("2 cauliflowers, 3 tomatoes, 4 slices of veal, 100g broccoli");
        Iterator<EntryOutput<Food>> iterator = emits.iterator();
        checkOutput(iterator.next(), 2, 12, "cauliflower", new Food("cauliflower"));
        checkOutput(iterator.next(), 18, 25, "tomatoes", new Food("tomatoes"));
        checkOutput(iterator.next(), 40, 43, "veal", new Food("veal"));
        checkOutput(iterator.next(), 51, 58, "broccoli", new Food("broccoli"));
    }

    @Test
    public void recipesFirstMatch() {
        Trie<Food> trie = Trie.<Food>builder().add(FOOD_WITH_ENTRIES).build();
        EntryOutput<Food> firstMatch = trie.firstMatch("2 cauliflowers, 3 tomatoes, 4 slices of veal, 100g broccoli");
        checkOutput(firstMatch, 2, 12, "cauliflower", new Food("cauliflower"));
    }

    @Test
    public void longAndShortOverlappingMatch() {
        Trie<String> trie = Trie.<String>builder().add("he", "pronon:he").add("hehehehe", "garbage")
                .build();
        Collection<EntryOutput<String>> emits = trie.parse("hehehehehe");
        Iterator<EntryOutput<String>> iterator = emits.iterator();
        checkOutput(iterator.next(), 0, 1, "he", "pronon:he");
        checkOutput(iterator.next(), 2, 3, "he", "pronon:he");
        checkOutput(iterator.next(), 4, 5, "he", "pronon:he");
        checkOutput(iterator.next(), 6, 7, "he", "pronon:he");
        checkOutput(iterator.next(), 0, 7, "hehehehe", "garbage");
        checkOutput(iterator.next(), 8, 9, "he", "pronon:he");
        checkOutput(iterator.next(), 2, 9, "hehehehe", "garbage");
    }

    @Test
    public void nonOverlapping() {
        Trie<String> trie = Trie.<String>builder().ignoreOverlaps().add("ab", "alpha:ab")
                .add("cba", "alpha:cba").add("ababc", "alpha:ababc").build();
        Collection<EntryOutput<String>> emits = trie.parse("ababcbab");
        assertEquals(2, emits.size());
        Iterator<EntryOutput<String>> iterator = emits.iterator();
        // With overlaps: ab@1, ab@3, ababc@4, cba@6, ab@7
        checkOutput(iterator.next(), 0, 4, "ababc", "alpha:ababc");
        checkOutput(iterator.next(), 6, 7, "ab", "alpha:ab");
    }

    @Test
    public void nonOverlappingFirstMatch() {
        Trie<String> trie = Trie.<String>builder().ignoreOverlaps().add("ab", "alpha:ab")
                .add("cba", "alpha:cba").add("ababc", "alpha:ababc").build();
        EntryOutput<String> firstMatch = trie.firstMatch("ababcbab");

        checkOutput(firstMatch, 0, 4, "ababc", "alpha:ababc");
    }

    @Test
    public void containsMatch() {
        Trie<String> trie = Trie.<String>builder().ignoreOverlaps().add("ab", "alpha:ab")
                .add("cba", "alpha:cba").add("ababc", "alpha:ababc").build();
        assertTrue(trie.match("ababcbab"));
    }

    @Test
    public void startOfChurchillSpeech() {
        Trie<String> trie = Trie.<String>builder().ignoreOverlaps().add("T").add("u").add("ur")
                .add("r").add("urn").add("ni").add("i").add("in").add("n")
                .add("urning").build();
        Collection<EntryOutput<String>> emits = trie.parse("Turning");
        assertEquals(2, emits.size());
    }

    @Test
    public void partialMatch() {
        Trie<String> trie = Trie.<String>builder().onlyWholeWords().add("sugar", "food:sugar").build();
        Collection<EntryOutput<String>> emits = trie.parse("sugarcane sugarcane sugar canesugar"); // left, middle, right test
        assertEquals(1, emits.size()); // Match must not be made
        checkOutput(emits.iterator().next(), 20, 24, "sugar", "food:sugar");
    }

    @Test
    public void partialMatchFirstMatch() {
        Trie<String> trie = Trie.<String>builder().onlyWholeWords().add("sugar", "food:sugar").build();
        EntryOutput<String> firstMatch = trie.firstMatch("sugarcane sugarcane sugar canesugar"); // left, middle, right test

        checkOutput(firstMatch, 20, 24, "sugar", "food:sugar");
    }

    @Test
    public void tokenizeFullSentence() {
        Trie<String> trie = Trie.<String>builder().add(GREEK_LETTERS_WITH_ENTRIES).build();
        Collection<Token<String>> tokens = trie.tokenize("Hear: Alpha team first, Beta from the rear, Gamma in reserve");
        assertEquals(7, tokens.size());
        Iterator<Token<String>> tokensIt = tokens.iterator();
        assertEquals("Hear: ", tokensIt.next().getFragment());
        assertEquals("Alpha", tokensIt.next().getFragment());
        assertEquals(" team first, ", tokensIt.next().getFragment());
        assertEquals("Beta", tokensIt.next().getFragment());
        assertEquals(" from the rear, ", tokensIt.next().getFragment());
        assertEquals("Gamma", tokensIt.next().getFragment());
        assertEquals(" in reserve", tokensIt.next().getFragment());
    }

    // @see https://github.com/robert-bor/aho-corasick/issues/5
    @Test
    public void testStringIndexOutOfBoundsException() {
        Trie<String> trie = Trie.<String>builder().ignoreCase().onlyWholeWords().add(UNICODE_WITH_ENTRIES)
                .build();
        Collection<EntryOutput<String>> emits = trie.parse("TurninG OnCe AgAiN BÖRKÜ");
        assertEquals(4, emits.size()); // Match must not be made
        Iterator<EntryOutput<String>> it = emits.iterator();

        checkOutput(it.next(), 0, 6, "turning", "uni:turning");
        checkOutput(it.next(), 8, 11, "once", "uni:once");
        checkOutput(it.next(), 13, 17, "again", "uni:again");
        checkOutput(it.next(), 19, 23, "börkü", "uni:börkü");
    }

    @Test
    public void testIgnoreCase() {
        Trie<String> trie = Trie.<String>builder().ignoreCase().add(UNICODE_WITH_ENTRIES).build();
        Collection<EntryOutput<String>> emits = trie.parse("TurninG OnCe AgAiN BÖRKÜ");
        assertEquals(4, emits.size()); // Match must not be made
        Iterator<EntryOutput<String>> it = emits.iterator();

        checkOutput(it.next(), 0, 6, "turning", "uni:turning");
        checkOutput(it.next(), 8, 11, "once", "uni:once");
        checkOutput(it.next(), 13, 17, "again", "uni:again");
        checkOutput(it.next(), 19, 23, "börkü", "uni:börkü");
    }

    @Test
    public void testIgnoreCaseFirstMatch() {
        Trie<String> trie = Trie.<String>builder().ignoreCase().add(UNICODE_WITH_ENTRIES).build();
        EntryOutput<String> firstMatch = trie.firstMatch("TurninG OnCe AgAiN BÖRKÜ");

        checkOutput(firstMatch, 0, 6, "turning", "uni:turning");
    }

    @Test
    public void tokenizeTokensInSequence() {
        Trie<String> trie = Trie.<String>builder().add(GREEK_LETTERS_WITH_ENTRIES).build();
        Collection<Token<String>> tokens = trie.tokenize("Alpha Beta Gamma");
        assertEquals(5, tokens.size());
    }

    // @see https://github.com/robert-bor/aho-corasick/issues/7
    @Test
    public void testZeroLength() {
        Trie<String> trie = Trie.<String>builder().ignoreOverlaps().onlyWholeWords().ignoreCase().add("")
                .build();
        trie.tokenize(
                "Try a natural lip and subtle bronzer to keep all the focus on those big bright eyes with NARS Eyeshadow Duo in Rated R And the winner is... Boots No7 Advanced Renewal Anti-ageing Glycolic Peel Kit ($25 amazon.com) won most-appealing peel.");
    }

    // @see https://github.com/robert-bor/aho-corasick/issues/8
    @Test
    public void testUnicode1() {
        String target = "LİKE THIS"; // The second character ('İ') is Unicode, which was read by AC as a 2-byte char
        assertEquals("THIS", target.substring(5, 9)); // Java does it the right way
        Trie<String> trie = Trie.<String>builder().ignoreCase().onlyWholeWords().add("this", "pronon:this")
                .build();
        Collection<EntryOutput<String>> emits = trie.parse(target);
        assertEquals(1, emits.size());
        Iterator<EntryOutput<String>> it = emits.iterator();
        checkOutput(it.next(), 5, 8, "this", "pronon:this");
    }

    // @see https://github.com/robert-bor/aho-corasick/issues/8
    @Test
    public void testUnicode2() {
        String target = "LİKE THIS"; // The second character ('İ') is Unicode, which was read by AC as a 2-byte char
        Trie<String> trie = Trie.<String>builder().ignoreCase().onlyWholeWords().add("this", "pronon:this")
                .build();
        assertEquals("THIS", target.substring(5, 9)); // Java does it the right way
        EntryOutput<String> firstMatch = trie.firstMatch(target);
        checkOutput(firstMatch, 5, 8, "this", "pronon:this");
    }

    @Test
    public void testPartialMatchWhiteSpaces() {
        Trie<String> trie = Trie.<String>builder().onlyWholeWordsWhiteSpaceSeparated()
                .add("#sugar-123", "sugar").build();
        Collection<EntryOutput<String>> emits = trie.parse("#sugar-123 #sugar-1234"); // left, middle, right test
        assertEquals(1, emits.size()); // Match must not be made
        checkOutput(emits.iterator().next(), 0, 9, "#sugar-123", "sugar");
    }

    @Test
    public void testLargeString() {
        final int interval = 100;
        final int textSize = 1000000;
        final String keyword = FOOD[1];
        final Food payload = FOOD_PAYLOAD[1];
        final StringBuilder text = randomNumbers(textSize);

        injectKeyword(text, keyword, interval);

        Trie<Food> trie = Trie.<Food>builder().onlyWholeWords().add(keyword, payload).build();

        final Collection<EntryOutput<Food>> emits = trie.parse(text);

        assertEquals(textSize / interval, emits.size());
    }

    @Test
    public void test_containsMatchWithCaseInsensitive() {
        Trie<String> trie = Trie.<String>builder().ignoreCase().add("foo", "bar").build();

        assertTrue(trie.match("FOOBAR"));
        assertFalse(trie.match("FO!?AR"));
    }

    // @see https://github.com/robert-bor/aho-corasick/issues/85
    @Test
    public void test_wholeWords() {
        Trie<String> trie = Trie.<String>builder().add("foo", "bar").onlyWholeWords().build();
        // access via PayloadTrie.parseText(CharSequence)
        Collection<EntryOutput<String>> result1 = trie.parse("foobar");
        // access via PayloadTrie.parseText(CharSequence, PayloadEmitHandler<String>)
        Collection<EntryOutput<String>> result2 = new LinkedList<>();
        trie.parse("foobar", result2::add);

        assertTrue(result1.isEmpty());
        assertEquals(result1, result2);
    }

    // @see https://github.com/robert-bor/aho-corasick/issues/85
    @Test
    public void test_wholeWordsWhiteSpaceSeparated() {
        Trie<String> trie = Trie.<String>builder().add("foo", "bar").onlyWholeWordsWhiteSpaceSeparated().build();
        // access via PayloadTrie.parseText(CharSequence)
        Collection<EntryOutput<String>> result1 = trie.parse("foo#bar");
        // access via PayloadTrie.parseText(CharSequence, PayloadEmitHandler<String>)
        Collection<EntryOutput<String>> result2 = new LinkedList<>();
        trie.parse("foo#bar", result2::add);

        assertTrue(result1.isEmpty());
        assertEquals(result1, result2);
    }

    /**
     * Generates a random sequence of ASCII numbers.
     *
     * @param count The number of numbers to generate.
     * @return A character sequence filled with random digits.
     */
    private StringBuilder randomNumbers(int count) {
        final StringBuilder sb = new StringBuilder(count);
        while (--count > 0) {
            sb.append(randomInt(10));
        }
        return sb;
    }

    /**
     * Injects keywords into a string builder.
     *
     * @param source   Should contain a bunch of random data that cannot match any
     *                 keyword.
     * @param keyword  A keyword to inject repeatedly in the text.
     * @param interval How often to inject the keyword.
     */
    private void injectKeyword(final StringBuilder source, final String keyword, final int interval) {
        final int length = source.length();
        for (int i = 0; i < length; i += interval) {
            source.replace(i, i + keyword.length(), keyword);
        }
    }

    private int randomInt(final int bound) {
        return random.nextInt(bound);
    }

    private void checkOutput(EntryOutput<Food> next, int expectedStart, int expectedEnd, String expectedKeyword,
                             Food expectedPayload) {
        assertEquals(expectedStart, next.getStart(), "Start of emit should have been " + expectedStart);
        assertEquals(expectedEnd, next.getEnd(), "End of emit should have been " + expectedEnd);
        assertEquals(expectedKeyword, next.getKey(), "Keyword of emit shoud be " + expectedKeyword);
        assertEquals(expectedPayload, next.getValue(), "Payload of emit shoud be " + expectedPayload);
    }

    private void checkOutput(EntryOutput<Integer> next, int expectedStart, int expectedEnd, String expectedKeyword,
                             Integer expectedPayload) {
        assertEquals(expectedStart, next.getStart(), "Start of emit should have been " + expectedStart);
        assertEquals(expectedEnd, next.getEnd(), "End of emit should have been " + expectedEnd);
        assertEquals(expectedKeyword, next.getKey(), "Keyword of emit shoud be " + expectedKeyword);
        assertEquals(expectedPayload, next.getValue(), "Payload of emit shoud be " + expectedPayload);
    }

    private void checkOutput(EntryOutput<String> next, int expectedStart, int expectedEnd, String expectedKeyword,
                             String expectedPayload) {
        assertEquals(expectedStart, next.getStart(), "Start of emit should have been " + expectedStart);
        assertEquals(expectedEnd, next.getEnd(), "End of emit should have been " + expectedEnd);
        assertEquals(expectedKeyword, next.getKey(), "Keyword of emit shoud be " + expectedKeyword);
        assertEquals(expectedPayload, next.getValue(), "Payload of emit shoud be " + expectedPayload);
    }

    static abstract class AbstractCollectingOutputHandler<T> implements CollectingOutputHandler<T> {

        private final List<EntryOutput<T>> outputs = new ArrayList<>();

        public void add(final EntryOutput<T> emit) {
            outputs.add(emit);
        }

        @Override
        public List<EntryOutput<T>> getOutputs() {
            return this.outputs;
        }
    }
}
