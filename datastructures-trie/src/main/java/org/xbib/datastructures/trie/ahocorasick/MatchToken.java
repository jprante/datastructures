package org.xbib.datastructures.trie.ahocorasick;

/**
 * Class for a token ("the fragment") that can emit an entry.
 * This token indicates a match, so {@link #isMatch()}
 * always returns {@code true}.
 *
 * @param <T> The type of the emitted entry value.
 */
public class MatchToken<T> extends AbstractToken<T> {

    private final EntryOutput<T> output;

    public MatchToken(String fragment, EntryOutput<T> output) {
        super(fragment);
        this.output = output;
    }

    @Override
    public boolean isMatch() {
        return true;
    }

    @Override
    public EntryOutput<T> getOutput() {
        return output;
    }
}
