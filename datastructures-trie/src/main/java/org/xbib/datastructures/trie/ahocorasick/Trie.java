package org.xbib.datastructures.trie.ahocorasick;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * A trie implementation.
 *
 * @param <T> The type of the supplied of the payload.
 */
public class Trie<T> {

    private final TrieConfig trieConfig;

    private final State<T> rootState;

    protected Trie(TrieConfig trieConfig) {
        this.trieConfig = trieConfig;
        this.rootState = new State<>();
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public Collection<Token<T>> tokenize(String text) {
        Collection<Token<T>> tokens = new LinkedList<>();
        Collection<EntryOutput<T>> outputs = parse(text);
        int lastCollectedPosition = -1;
        for (EntryOutput<T> output : outputs) {
            if (output.getStart() - lastCollectedPosition > 1) {
                tokens.add(createFragment(output, text, lastCollectedPosition));
            }
            tokens.add(createMatch(output, text));
            lastCollectedPosition = output.getEnd();
        }
        if (text.length() - lastCollectedPosition > 1) {
            tokens.add(createFragment(null, text, lastCollectedPosition));
        }
        return tokens;
    }

    public Collection<EntryOutput<T>> parse(CharSequence text) {
        return parse(text, new DefaultOutputHandler<>());
    }

    @SuppressWarnings("unchecked")
    public Collection<EntryOutput<T>> parse(CharSequence text, CollectingOutputHandler<T> handler) {
        parse(text, (OutputHandler<T>) handler);
        List<EntryOutput<T>> outputs = handler.getOutputs();
        if (!trieConfig.isAllowOverlaps()) {
            IntervalTree intervalTree = new IntervalTree((List<Interval>) (List<?>) outputs);
            intervalTree.removeOverlaps((List<Interval>) (List<?>) outputs);
        }
        return outputs;
    }

    public void parse(CharSequence text, OutputHandler<T> outputHandler) {
        State<T> currentState = getRootState();
        for (int position = 0; position < text.length(); position++) {
            char character = text.charAt(position);
            if (trieConfig.isCaseInsensitive()) {
                character = Character.toLowerCase(character);
            }
            currentState = getState(currentState, character);
            Collection<Entry<T>> entries = currentState.entries();
            if (processOutputs(text, position, entries, outputHandler) && trieConfig.isStopOnHit()) {
                return;
            }
        }
    }

    public boolean match(CharSequence text) {
        return firstMatch(text) != null;
    }

    public EntryOutput<T> firstMatch(CharSequence text) {
        if (!trieConfig.isAllowOverlaps()) {
            Collection<EntryOutput<T>> parseText = parse(text);
            if (parseText != null && !parseText.isEmpty()) {
                return parseText.iterator().next();
            }
        } else {
            State<T> currentState = getRootState();
            for (int i = 0; i < text.length(); i++) {
                char character = text.charAt(i);
                if (trieConfig.isCaseInsensitive()) {
                    character = Character.toLowerCase(character);
                }
                currentState = getState(currentState, character);
                Collection<Entry<T>> entries = currentState.entries();
                if (entries != null && !entries.isEmpty()) {
                    for (Entry<T> entry : entries) {
                        EntryOutput<T> output =
                                new EntryOutput<>(i - entry.getKey().length() + 1, i, entry.getKey(), entry.getValue());
                        if (trieConfig.isOnlyWholeWords()) {
                            if (!isPartialMatch(text, output)) {
                                return output;
                            }
                        } else {
                            return output;
                        }
                    }
                }
            }
        }
        return null;
    }

    private Token<T> createFragment(EntryOutput<T> output, String text, int lastCollectedPosition) {
        return new FragmentToken<>(text.substring(lastCollectedPosition + 1, output == null ? text.length() : output.getStart()));
    }

    private Token<T> createMatch(EntryOutput<T> output, String text) {
        return new MatchToken<>(text.substring(output.getStart(), output.getEnd() + 1), output);
    }

    private State<T> addState(String key) {
        State<T> state = getRootState();
        for (Character character : key.toCharArray()) {
            Character adjustedChar = trieConfig.isCaseInsensitive() ? Character.toLowerCase(character) : character;
            state = state.addState(adjustedChar);
        }
        return state;
    }

    private boolean isPartialMatch(CharSequence searchText, EntryOutput<T> output) {
        return (output.getStart() != 0 && Character.isAlphabetic(searchText.charAt(output.getStart() - 1)))
                || (output.getEnd() + 1 != searchText.length() && Character.isAlphabetic(searchText.charAt(output.getEnd() + 1)));
    }

    private boolean isPartialMatchWhiteSpaceSeparated(CharSequence searchText, EntryOutput<T> output) {
        long size = searchText.length();
        return (output.getStart() != 0 && !Character.isWhitespace(searchText.charAt(output.getStart() - 1)))
                || (output.getEnd() + 1 != size && !Character.isWhitespace(searchText.charAt(output.getEnd() + 1)));
    }

    private State<T> getState(State<T> currentState, Character character) {
        State<T> newCurrentState = currentState.nextState(character);
        while (newCurrentState == null) {
            currentState = currentState.failure();
            newCurrentState = currentState.nextState(character);
        }
        return newCurrentState;
    }

    private void constructFailureStates() {
        Queue<State<T>> queue = new LinkedList<>();
        State<T> startState = getRootState();
        for (State<T> depthOneState : startState.getStates()) {
            depthOneState.setFailure(startState);
            queue.add(depthOneState);
        }
        while (!queue.isEmpty()) {
            State<T> currentState = queue.remove();
            for (Character transition : currentState.getTransitions()) {
                State<T> targetState = currentState.nextState(transition);
                queue.add(targetState);
                State<T> traceFailureState = currentState.failure();
                while (traceFailureState.nextState(transition) == null) {
                    traceFailureState = traceFailureState.failure();
                }
                State<T> newFailureState = traceFailureState.nextState(transition);
                targetState.setFailure(newFailureState);
                targetState.add(newFailureState.entries());
            }
        }
    }

    private boolean processOutputs(CharSequence text,
                                   int position,
                                   Collection<Entry<T>> entries,
                                   OutputHandler<T> outputHandler) {
        boolean output = false;
        for (Entry<T> entry : entries) {
            EntryOutput<T> entryOutput =
                    new EntryOutput<>(position - entry.getKey().length() + 1, position, entry.getKey(), entry.getValue());
            if (!(trieConfig.isOnlyWholeWords() && isPartialMatch(text, entryOutput)) &&
                    !(trieConfig.isOnlyWholeWordsWhiteSpaceSeparated() &&
                            isPartialMatchWhiteSpaceSeparated(text, entryOutput))) {
                output = outputHandler.output(entryOutput) || output;
                if (output && trieConfig.isStopOnHit()) {
                    break;
                }
            }
        }
        return output;
    }

    private State<T> getRootState() {
        return rootState;
    }

    public static class Builder<T> {

        private final TrieConfig trieConfig;

        private final Trie<T> trie;

        private Builder() {
            trieConfig = new TrieConfig();
            trie = new Trie<>(trieConfig);
        }

        public Builder<T> ignoreCase() {
            trieConfig.setCaseInsensitive(true);
            return this;
        }

        public Builder<T> ignoreOverlaps() {
            trieConfig.setAllowOverlaps(false);
            return this;
        }

        public Builder<T> onlyWholeWords() {
            trieConfig.setOnlyWholeWords(true);
            return this;
        }

        public Builder<T> onlyWholeWordsWhiteSpaceSeparated() {
            trieConfig.setOnlyWholeWordsWhiteSpaceSeparated(true);
            return this;
        }

        public Builder<T> stopOnHit() {
            trie.trieConfig.setStopOnHit(true);
            return this;
        }

        public Builder<T> add(String key) {
            add(key, null);
            return this;
        }

        public Builder<T> add(String key, T value) {
            if (key == null || key.isEmpty()) {
                return this;
            }
            trie.addState(key).add(new Entry<>(key, value));
            return this;
        }

        public Builder<T> add(Collection<Entry<T>> keys) {
            for (Entry<T> entry : keys) {
                add(entry.getKey(), entry.getValue());
            }
            return this;
        }

        public Trie<T> build() {
            trie.constructFailureStates();
            return this.trie;
        }
    }
}
