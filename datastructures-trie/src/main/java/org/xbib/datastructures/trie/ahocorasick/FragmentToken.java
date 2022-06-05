package org.xbib.datastructures.trie.ahocorasick;

/***
 * Class for a token ("the fragment") that can emit an entry.
 * This token indicates a matching search term was not found, so
 * {@link #isMatch()} always returns {@code false}.
 *
 * @param <T> The Type of the emitted payloads.
 */
public class FragmentToken<T> extends AbstractToken<T> {

    public FragmentToken(String fragment) {
        super(fragment);
    }

    @Override
    public boolean isMatch() {
        return false;
    }

    @Override
    public EntryOutput<T> getOutput() {
        return null;
    }
}
