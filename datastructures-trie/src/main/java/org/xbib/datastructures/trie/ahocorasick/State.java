package org.xbib.datastructures.trie.ahocorasick;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * A state has various important tasks it must attend to:
 * <ul>
 * <li>success; when a character points to another state, it must return that
 * state</li>
 * <li>failure; when a character has no matching state, the algorithm must be
 * able to fall back on a state with less depth</li>
 * <li>emits; when this state is passed and keys have been matched, the
 * matches and their values must be output so they can be used later
 * on.</li>
 * </ul>
 * The root state is special in the sense that it has no failure state; it
 * cannot fail. If it 'fails' it will still parse the next character and start
 * from the root node. This ensures that the algorithm always runs. All other
 * states always have a fail state.
 */
public class State<T> {

    private final int depth;

    private final State<T> rootState;

    private final Map<Character, State<T>> success;

    private final Set<Entry<T>> entries;

    private State<T> failure;

    public State() {
        this(0);
    }

    public State(final int depth) {
        this.depth = depth;
        rootState = depth == 0 ? this : null;
        success = new HashMap<>();
        entries = new TreeSet<>();
    }

    private State<T> nextState(final Character character, final boolean ignoreRootState) {
        State<T> nextState = this.success.get(character);

        if (!ignoreRootState && nextState == null && this.rootState != null) {
            nextState = this.rootState;
        }

        return nextState;
    }

    public State<T> nextState(final Character character) {
        return nextState(character, false);
    }

    public State<T> nextStateIgnoreRootState(Character character) {
        return nextState(character, true);
    }

    public State<T> addState(Character character) {
        State<T> nextState = nextStateIgnoreRootState(character);
        if (nextState == null) {
            nextState = new State<>(this.depth + 1);
            this.success.put(character, nextState);
        }
        return nextState;
    }

    public int getDepth() {
        return this.depth;
    }

    public void add(Entry<T> entry) {
        entries.add(entry);
    }

    public void add(Collection<Entry<T>> emits) {
        for (Entry<T> emit : emits) {
            add(emit);
        }
    }

    public Collection<Entry<T>> entries() {
        return entries;
    }

    public State<T> failure() {
        return this.failure;
    }

    public void setFailure(State<T> failState) {
        this.failure = failState;
    }

    public Collection<State<T>> getStates() {
        return this.success.values();
    }

    public Collection<Character> getTransitions() {
        return this.success.keySet();
    }
}