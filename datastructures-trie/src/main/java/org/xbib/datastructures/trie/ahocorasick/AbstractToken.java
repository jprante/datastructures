package org.xbib.datastructures.trie.ahocorasick;

/***
 * This class holds a text ("the fragment") and emits some output. If
 * {@link #isMatch()} returns {@code true}, the token matched a search.
 *
 * @param <T> The Type of the emitted payloads.
 */
public abstract class AbstractToken<T> implements Token<T> {

    private final String fragment;

    public AbstractToken(String fragment) {
        this.fragment = fragment;
    }

    @Override
    public String getFragment() {
        return this.fragment;
    }

}
