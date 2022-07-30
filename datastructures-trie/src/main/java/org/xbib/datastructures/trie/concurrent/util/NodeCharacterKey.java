package org.xbib.datastructures.trie.concurrent.util;

/**
 * A lightweight object which simply wraps a {@link Character} and implements {@link NodeCharacterProvider}, which
 * can be used as a key to locate a node having the same edge first character in a list of nodes using binary search.
 *
 */
public class NodeCharacterKey implements NodeCharacterProvider {

    private final Character character;

    public NodeCharacterKey(Character character) {
        this.character = character;
    }

    @Override
    public Character getIncomingEdgeFirstCharacter() {
        return character;
    }
}
