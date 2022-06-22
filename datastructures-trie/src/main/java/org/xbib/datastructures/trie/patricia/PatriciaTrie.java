package org.xbib.datastructures.trie.patricia;

import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

/**
 * <h3>PATRICIA {@link PrefixTree}</h3>
 *
 * <i>Practical Algorithm to Retrieve Information Coded in Alphanumeric</i>
 *
 * <p>A PATRICIA {@link PrefixTree} is a compressed {@link PrefixTree}. Instead of storing
 * all data at the edges of the {@link PrefixTree} (and having empty internal nodes),
 * PATRICIA stores data in every node. This allows for very efficient traversal,
 * insert, delete, predecessor, successor, prefix, range, and {@link #select(Object)}
 * operations. All operations are performed at worst in O(K) time, where K
 * is the number of bits in the largest item in the tree. In practice,
 * operations actually take O(A(K)) time, where A(K) is the average number of
 * bits of all items in the tree.
 *
 * <p>Most importantly, PATRICIA requires very few comparisons to keys while
 * doing any operation. While performing a lookup, each comparison (at most
 * K of them, described above) will perform a single bit comparison against
 * the given key, instead of comparing the entire key to another key.
 *
 * <p>The {@link PrefixTree} can return operations in lexicographical order using the
 * {@link #traverse(Cursor)}, 'prefix', 'submap', or 'iterator' methods. The
 * {@link PrefixTree} can also scan for items that are 'bitwise' (using an XOR
 * metric) by the 'select' method. Bitwise closeness is determined by the
 * {@link KeyAnalyzer} returning true or false for a bit being set or not in
 * a given key.
 *
 * <p>Any methods here that take an {@link Object} argument may throw a
 * {@link ClassCastException} if the method is expecting an instance of K
 * and it isn't K.
 *
 * @see <a href="http://en.wikipedia.org/wiki/Radix_tree">Radix Tree</a>
 * @see <a href="http://www.csse.monash.edu.au/~lloyd/tildeAlgDS/Tree/PATRICIA">PATRICIA</a>
 * @see <a href="http://www.imperialviolet.org/binary/critbit.pdf">Crit-Bit Tree</a>
 */
public class PatriciaTrie<K, V> extends AbstractTrie<K, V> {

    public PatriciaTrie() {
        super();
    }

    public PatriciaTrie(Map<? extends K, ? extends V> m) {
        super();
        putAll(m);
    }

    public PatriciaTrie(KeyAnalyzer<? super K> keyAnalyzer) {
        super(keyAnalyzer);
    }

    public PatriciaTrie(KeyAnalyzer<? super K> keyAnalyzer,
                        Map<? extends K, ? extends V> m) {
        super(keyAnalyzer);
        putAll(m);
    }

    @Override
    public Comparator<? super K> comparator() {
        return keyAnalyzer;
    }

    @Override
    public SortedMap<K, V> prefixMap(K prefix) {
        int lengthInBits = lengthInBits(prefix);
        if (lengthInBits == 0) {
            return this;
        }

        return new PrefixRangeMap(prefix);
    }

    @Override
    public K firstKey() {
        return firstEntry().getKey();
    }

    @Override
    public K lastKey() {
        TrieEntry<K, V> entry = lastEntry();
        if (entry != null) {
            return entry.getKey();
        }
        return null;
    }

    @Override
    public SortedMap<K, V> headMap(K toKey) {
        return new RangeEntryMap(null, toKey);
    }

    @Override
    public SortedMap<K, V> subMap(K fromKey, K toKey) {
        return new RangeEntryMap(fromKey, toKey);
    }

    @Override
    public SortedMap<K, V> tailMap(K fromKey) {
        return new RangeEntryMap(fromKey, null);
    }

    /**
     * The root node of the {@link PrefixTree}.
     */
    final TrieEntry<K, V> root = new TrieEntry<>(null, null, -1);
    /**
     * The number of times this {@link PrefixTree} has been modified.
     * It's used to detect concurrent modifications and fail-fast
     * the {@link Iterator}s.
     */
    transient int modCount = 0;
    /**
     * Each of these fields are initialized to contain an instance of the
     * appropriate view the first time this view is requested. The views are
     * stateless, so there's no reason to create more than one of each.
     */
    private transient volatile Set<K> keySet;

    private transient volatile Collection<V> values;

    private transient volatile Set<Map.Entry<K, V>> entrySet;

    /**
     * The current size of the {@link PrefixTree}
     */
    private int size = 0;

    /**
     * Returns true if 'next' is a valid uplink coming from 'from'.
     */
    static boolean isValidUplink(TrieEntry<?, ?> next, TrieEntry<?, ?> from) {
        return next != null && next.bitIndex <= from.bitIndex && !next.isEmpty();
    }

    @Override
    public void clear() {
        root.key = null;
        root.bitIndex = -1;
        root.value = null;
        root.parent = null;
        root.left = root;
        root.right = null;
        root.predecessor = root;
        size = 0;
        incrementModCount();
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * A helper method to increment the {@link PrefixTree} size
     * and the modification counter.
     */
    void incrementSize() {
        size++;
        incrementModCount();
    }

    /**
     * A helper method to decrement the {@link PrefixTree} size
     * and increment the modification counter.
     */
    void decrementSize() {
        size--;
        incrementModCount();
    }

    /**
     * A helper method to increment the modification counter.
     */
    private void incrementModCount() {
        ++modCount;
    }

    @Override
    public V put(K key, V value) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }
        int lengthInBits = lengthInBits(key);
        // The only place to store a key with a length
        // of zero bits is the root node
        if (lengthInBits == 0) {
            if (root.isEmpty()) {
                incrementSize();
            } else {
                incrementModCount();
            }
            return root.setKeyValue(key, value);
        }
        TrieEntry<K, V> found = getNearestEntryForKey(key);
        if (compareKeys(key, found.key)) {
            if (found.isEmpty()) { // <- must be the root
                incrementSize();
            } else {
                incrementModCount();
            }
            return found.setKeyValue(key, value);
        }

        int bitIndex = bitIndex(key, found.key);
        if (bitIndex != KeyAnalyzer.OUT_OF_BOUNDS_BIT_KEY) {
            if (0 <= bitIndex) { // in 99.999...9% the case
                /* NEW KEY+VALUE TUPLE */
                TrieEntry<K, V> t = new TrieEntry<>(key, value, bitIndex);
                addEntry(t);
                incrementSize();
                return null;
            } else if (bitIndex == KeyAnalyzer.NULL_BIT_KEY) {
                // A bits of the Key are zero. The only place to
                // store such a Key is the root Node!
                /* NULL BIT KEY */
                if (root.isEmpty()) {
                    incrementSize();
                } else {
                    incrementModCount();
                }
                return root.setKeyValue(key, value);
            } else if (bitIndex == KeyAnalyzer.EQUAL_BIT_KEY) {
                // This is a very special and rare case.
                /* REPLACE OLD KEY+VALUE */
                if (found != root) {
                    incrementModCount();
                    return found.setKeyValue(key, value);
                }
            }
        }
        throw new IndexOutOfBoundsException("Failed to put: " + key + " -> " + value + ", " + bitIndex);
    }

    /**
     * Adds the given {@link TrieEntry} to the {@link PrefixTree}
     */
    TrieEntry<K, V> addEntry(TrieEntry<K, V> entry) {
        TrieEntry<K, V> current = root.left;
        TrieEntry<K, V> path = root;
        while (true) {
            if (current.bitIndex >= entry.bitIndex
                    || current.bitIndex <= path.bitIndex) {
                entry.predecessor = entry;

                if (!isBitSet(entry.key, entry.bitIndex)) {
                    entry.left = entry;
                    entry.right = current;
                } else {
                    entry.left = current;
                    entry.right = entry;
                }

                entry.parent = path;
                if (current.bitIndex >= entry.bitIndex) {
                    current.parent = entry;
                }

                // if we inserted an uplink, set the predecessor on it
                if (current.bitIndex <= path.bitIndex) {
                    current.predecessor = entry;
                }

                if (path == root || !isBitSet(entry.key, path.bitIndex)) {
                    path.left = entry;
                } else {
                    path.right = entry;
                }

                return entry;
            }

            path = current;

            if (!isBitSet(entry.key, current.bitIndex)) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
    }

    @Override
    public V get(Object k) {
        TrieEntry<K, V> entry = getEntry(k);
        return entry != null ? entry.getValue() : null;
    }

    /**
     * Returns the entry associated with the specified key in the
     * AbstractPatriciaTrie.  Returns null if the map contains no mapping
     * for this key.
     * <p>
     * This may throw ClassCastException if the object is not of type K.
     */
    @SuppressWarnings("unchecked")
    TrieEntry<K, V> getEntry(Object k) {
        K key = (K) k;
        if (key == null) {
            return null;
        }

        TrieEntry<K, V> entry = getNearestEntryForKey(key);
        return !entry.isEmpty() && compareKeys(key, entry.key) ? entry : null;
    }

    @Override
    public Map.Entry<K, V> select(K key) {
        Reference<Map.Entry<K, V>> reference
                = new Reference<Map.Entry<K, V>>();
        if (!selectR(root.left, -1, key, reference)) {
            return reference.get();
        }
        return null;
    }

    @Override
    public Map.Entry<K, V> select(K key, Cursor<? super K, ? super V> cursor) {
        Reference<Map.Entry<K, V>> reference
                = new Reference<Map.Entry<K, V>>();
        selectR(root.left, -1, key, cursor, reference);
        return reference.get();
    }

    private boolean selectR(TrieEntry<K, V> h, int bitIndex,
                            final K key, final Reference<Map.Entry<K, V>> reference) {
        if (h.bitIndex <= bitIndex) {
            // If we hit the root Node and it is empty
            // we have to look for an alternative best
            // matching node.
            if (!h.isEmpty()) {
                reference.set(h);
                return false;
            }
            return true;
        }

        if (!isBitSet(key, h.bitIndex)) {
            if (selectR(h.left, h.bitIndex, key, reference)) {
                return selectR(h.right, h.bitIndex, key, reference);
            }
        } else {
            if (selectR(h.right, h.bitIndex, key, reference)) {
                return selectR(h.left, h.bitIndex, key, reference);
            }
        }
        return false;
    }

    /**
     *
     */
    private boolean selectR(TrieEntry<K, V> h, int bitIndex,
                            final K key, final Cursor<? super K, ? super V> cursor,
                            final Reference<Map.Entry<K, V>> reference) {

        if (h.bitIndex <= bitIndex) {
            if (!h.isEmpty()) {
                Cursor.Decision decision = cursor.select(h);
                switch (decision) {
                    case REMOVE:
                        throw new UnsupportedOperationException(
                                "Cannot remove during select");
                    case EXIT:
                        reference.set(h);
                        return false; // exit
                    case REMOVE_AND_EXIT:
                        TrieEntry<K, V> entry = new TrieEntry<K, V>(
                                h.getKey(), h.getValue(), -1);
                        reference.set(entry);
                        removeEntry(h);
                        return false;
                    case CONTINUE:
                        // fall through.
                }
            }
            return true; // continue
        }

        if (!isBitSet(key, h.bitIndex)) {
            if (selectR(h.left, h.bitIndex, key, cursor, reference)) {
                return selectR(h.right, h.bitIndex, key, cursor, reference);
            }
        } else {
            if (selectR(h.right, h.bitIndex, key, cursor, reference)) {
                return selectR(h.left, h.bitIndex, key, cursor, reference);
            }
        }

        return false;
    }

    @Override
    public Map.Entry<K, V> traverse(Cursor<? super K, ? super V> cursor) {
        TrieEntry<K, V> entry = nextEntry(null);
        while (entry != null) {
            TrieEntry<K, V> current = entry;
            Cursor.Decision decision = cursor.select(current);
            entry = nextEntry(current);
            switch (decision) {
                case EXIT:
                    return current;
                case REMOVE:
                    removeEntry(current);
                    break; // out of switch, stay in while loop
                case REMOVE_AND_EXIT:
                    Map.Entry<K, V> value = new TrieEntry<K, V>(
                            current.getKey(), current.getValue(), -1);
                    removeEntry(current);
                    return value;
                case CONTINUE: // do nothing.
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean containsKey(Object k) {
        if (k == null) {
            return false;
        }
        K key = (K) k;
        TrieEntry<K, V> entry = getNearestEntryForKey(key);
        return !entry.isEmpty() && compareKeys(key, entry.key);
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        if (entrySet == null) {
            entrySet = new EntrySet();
        }
        return entrySet;
    }

    @Override
    public Set<K> keySet() {
        if (keySet == null) {
            keySet = new KeySet();
        }
        return keySet;
    }

    @Override
    public Collection<V> values() {
        if (values == null) {
            values = new Values();
        }
        return values;
    }

    @SuppressWarnings("unchecked")
    @Override
    public V remove(Object k) {
        if (k == null) {
            return null;
        }
        K key = (K) k;
        TrieEntry<K, V> current = root.left;
        TrieEntry<K, V> path = root;
        while (true) {
            if (current.bitIndex <= path.bitIndex) {
                if (!current.isEmpty() && compareKeys(key, current.key)) {
                    return removeEntry(current);
                } else {
                    return null;
                }
            }

            path = current;

            if (!isBitSet(key, current.bitIndex)) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
    }

    /**
     * Returns the nearest entry for a given key.  This is useful
     * for finding knowing if a given key exists (and finding the value
     * for it), or for inserting the key.
     * <p>
     * The actual get implementation. This is very similar to
     * selectR but with the exception that it might return the
     * root Entry even if it's empty.
     */
    TrieEntry<K, V> getNearestEntryForKey(K key) {
        TrieEntry<K, V> current = root.left;
        TrieEntry<K, V> path = root;
        while (true) {
            if (current.bitIndex <= path.bitIndex) {
                return current;
            }

            path = current;
            if (!isBitSet(key, current.bitIndex)) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
    }

    /**
     * Removes a single entry from the {@link PrefixTree}.
     * <p>
     * If we found a Key (Entry h) then figure out if it's
     * an internal (hard to remove) or external Entry (easy
     * to remove)
     */
    V removeEntry(TrieEntry<K, V> h) {
        if (h != root) {
            if (h.isInternalNode()) {
                removeInternalEntry(h);
            } else {
                removeExternalEntry(h);
            }
        }

        decrementSize();
        return h.setKeyValue(null, null);
    }

    /**
     * Removes an external entry from the {@link PrefixTree}.
     * <p>
     * If it's an external Entry then just remove it.
     * This is very easy and straight forward.
     */
    private void removeExternalEntry(TrieEntry<K, V> h) {
        if (h == root) {
            throw new IllegalArgumentException("Cannot delete root Entry!");
        } else if (!h.isExternalNode()) {
            throw new IllegalArgumentException(h + " is not an external Entry!");
        }

        TrieEntry<K, V> parent = h.parent;
        TrieEntry<K, V> child = (h.left == h) ? h.right : h.left;

        if (parent.left == h) {
            parent.left = child;
        } else {
            parent.right = child;
        }

        // either the parent is changing, or the predecessor is changing.
        if (child.bitIndex > parent.bitIndex) {
            child.parent = parent;
        } else {
            child.predecessor = parent;
        }

    }

    /**
     * Removes an internal entry from the {@link PrefixTree}.
     * <p>
     * If it's an internal Entry then "good luck" with understanding
     * this code. The Idea is essentially that Entry p takes Entry h's
     * place in the trie which requires some re-wiring.
     */
    private void removeInternalEntry(TrieEntry<K, V> h) {
        if (h == root) {
            throw new IllegalArgumentException("Cannot delete root Entry!");
        } else if (!h.isInternalNode()) {
            throw new IllegalArgumentException(h + " is not an internal Entry!");
        }

        TrieEntry<K, V> p = h.predecessor;

        // Set P's bitIndex
        p.bitIndex = h.bitIndex;

        // Fix P's parent, predecessor and child Nodes
        {
            TrieEntry<K, V> parent = p.parent;
            TrieEntry<K, V> child = (p.left == h) ? p.right : p.left;

            // if it was looping to itself previously,
            // it will now be pointed from it's parent
            // (if we aren't removing it's parent --
            //  in that case, it remains looping to itself).
            // otherwise, it will continue to have the same
            // predecessor.
            if (p.predecessor == p && p.parent != h) {
                p.predecessor = p.parent;
            }

            if (parent.left == p) {
                parent.left = child;
            } else {
                parent.right = child;
            }

            if (child.bitIndex > parent.bitIndex) {
                child.parent = parent;
            }
        }

        // Fix H's parent and child Nodes
        {
            // If H is a parent of its left and right child
            // then change them to P
            if (h.left.parent == h) {
                h.left.parent = p;
            }

            if (h.right.parent == h) {
                h.right.parent = p;
            }

            // Change H's parent
            if (h.parent.left == h) {
                h.parent.left = p;
            } else {
                h.parent.right = p;
            }
        }

        // Copy the remaining fields from H to P
        //p.bitIndex = h.bitIndex;
        p.parent = h.parent;
        p.left = h.left;
        p.right = h.right;

        // Make sure that if h was pointing to any uplinks,
        // p now points to them.
        if (isValidUplink(p.left, p)) {
            p.left.predecessor = p;
        }

        if (isValidUplink(p.right, p)) {
            p.right.predecessor = p;
        }
    }

    /**
     * Returns the entry lexicographically after the given entry.
     * If the given entry is null, returns the first node.
     */
    TrieEntry<K, V> nextEntry(TrieEntry<K, V> node) {
        if (node == null) {
            return firstEntry();
        } else {
            return nextEntryImpl(node.predecessor, node, null);
        }
    }

    /**
     * Scans for the next node, starting at the specified point, and using 'previous'
     * as a hint that the last node we returned was 'previous' (so we know not to return
     * it again).  If 'tree' is non-null, this will limit the search to the given tree.
     * <p>
     * The basic premise is that each iteration can follow the following steps:
     * <p>
     * 1) Scan all the way to the left.
     * a) If we already started from this node last time, proceed to Step 2.
     * b) If a valid uplink is found, use it.
     * c) If the result is an empty node (root not set), break the scan.
     * d) If we already returned the left node, break the scan.
     * <p>
     * 2) Check the right.
     * a) If we already returned the right node, proceed to Step 3.
     * b) If it is a valid uplink, use it.
     * c) Do Step 1 from the right node.
     * <p>
     * 3) Back up through the parents until we encounter find a parent
     * that we're not the right child of.
     * <p>
     * 4) If there's no right child of that parent, the iteration is finished.
     * Otherwise continue to Step 5.
     * <p>
     * 5) Check to see if the right child is a valid uplink.
     * a) If we already returned that child, proceed to Step 6.
     * Otherwise, use it.
     * <p>
     * 6) If the right child of the parent is the parent itself, we've
     * already found & returned the end of the Trie, so exit.
     * <p>
     * 7) Do Step 1 on the parent's right child.
     */
    TrieEntry<K, V> nextEntryImpl(TrieEntry<K, V> start,
                                  TrieEntry<K, V> previous, TrieEntry<K, V> tree) {

        TrieEntry<K, V> current = start;

        // Only look at the left if this was a recursive or
        // the first check, otherwise we know we've already looked
        // at the left.
        if (previous == null || start != previous.predecessor) {
            while (!current.left.isEmpty()) {
                // stop traversing if we've already
                // returned the left of this node.
                if (previous == current.left) {
                    break;
                }

                if (isValidUplink(current.left, current)) {
                    return current.left;
                }

                current = current.left;
            }
        }

        // If there's no data at all, exit.
        if (current.isEmpty()) {
            return null;
        }

        // If we've already returned the left,
        // and the immediate right is null,
        // there's only one entry in the Trie
        // which is stored at the root.
        //
        //  / ("")   <-- root
        //  \_/  \
        //     null <-- 'current'
        //
        if (current.right == null) {
            return null;
        }

        // If nothing valid on the left, try the right.
        if (previous != current.right) {
            // See if it immediately is valid.
            if (isValidUplink(current.right, current)) {
                return current.right;
            }

            // Must search on the right's side if it wasn't initially valid.
            return nextEntryImpl(current.right, previous, tree);
        }

        // Neither left nor right are valid, find the first parent
        // whose child did not come from the right & traverse it.
        while (current == current.parent.right) {
            // If we're going to traverse to above the subtree, stop.
            if (current == tree) {
                return null;
            }

            current = current.parent;
        }

        // If we're on the top of the subtree, we can't go any higher.
        if (current == tree) {
            return null;
        }

        // If there's no right, the parent must be root, so we're done.
        if (current.parent.right == null) {
            return null;
        }

        // If the parent's right points to itself, we've found one.
        if (previous != current.parent.right
                && isValidUplink(current.parent.right, current.parent)) {
            return current.parent.right;
        }

        // If the parent's right is itself, there can't be any more nodes.
        if (current.parent.right == current.parent) {
            return null;
        }

        // We need to traverse down the parent's right's path.
        return nextEntryImpl(current.parent.right, previous, tree);
    }

    /**
     * Returns the first entry the {@link PrefixTree} is storing.
     * <p>
     * This is implemented by going always to the left until
     * we encounter a valid uplink. That uplink is the first key.
     */
    TrieEntry<K, V> firstEntry() {
        // if Trie is empty, no first node.
        if (isEmpty()) {
            return null;
        }

        return followLeft(root);
    }

    /**
     * Goes left through the tree until it finds a valid node.
     */
    TrieEntry<K, V> followLeft(TrieEntry<K, V> node) {
        while (true) {
            TrieEntry<K, V> child = node.left;
            // if we hit root and it didn't have a node, go right instead.
            if (child.isEmpty()) {
                child = node.right;
            }

            if (child.bitIndex <= node.bitIndex) {
                return child;
            }

            node = child;
        }
    }

    /**
     * A {@link Reference} allows us to return something through a Method's
     * argument list. An alternative would be to an Array with a length of
     * one (1) but that leads to compiler warnings. Computationally and memory
     * wise there's no difference (except for the need to load the
     * {@link Reference} Class but that happens only once).
     */
    private static class Reference<E> {

        private E item;

        public void set(E item) {
            this.item = item;
        }

        public E get() {
            return item;
        }
    }

    /**
     * A {@link PrefixTree} is a set of {@link TrieEntry} nodes
     */
    static class TrieEntry<K, V> extends BasicEntry<K, V> {

        private static final long serialVersionUID = 4596023148184140013L;

        /**
         * The index this entry is comparing.
         */
        protected int bitIndex;

        /**
         * The parent of this entry.
         */
        protected TrieEntry<K, V> parent;

        /**
         * The left child of this entry.
         */
        protected TrieEntry<K, V> left;

        /**
         * The right child of this entry.
         */
        protected TrieEntry<K, V> right;

        /**
         * The entry who uplinks to this entry.
         */
        protected TrieEntry<K, V> predecessor;

        public TrieEntry(K key, V value, int bitIndex) {
            super(key, value);

            this.bitIndex = bitIndex;

            this.parent = null;
            this.left = this;
            this.right = null;
            this.predecessor = this;
        }

        /**
         * Whether or not the entry is storing a key.
         * Only the root can potentially be empty, all other
         * nodes must have a key.
         */
        public boolean isEmpty() {
            return key == null;
        }

        /**
         * Neither the left nor right child is a loopback
         */
        public boolean isInternalNode() {
            return left != this && right != this;
        }

        /**
         * Either the left or right child is a loopback
         */
        public boolean isExternalNode() {
            return !isInternalNode();
        }

        @Override
        public String toString() {
            StringBuilder buffer = new StringBuilder();

            if (bitIndex == -1) {
                buffer.append("RootEntry(");
            } else {
                buffer.append("Entry(");
            }

            buffer.append("key=").append(getKey()).append(" [").append(bitIndex).append("], ");
            buffer.append("value=").append(getValue()).append(", ");
            //buffer.append("bitIndex=").append(bitIndex).append(", ");

            if (parent != null) {
                if (parent.bitIndex == -1) {
                    buffer.append("parent=").append("ROOT");
                } else {
                    buffer.append("parent=").append(parent.getKey()).append(" [").append(parent.bitIndex).append("]");
                }
            } else {
                buffer.append("parent=").append("null");
            }
            buffer.append(", ");

            if (left != null) {
                if (left.bitIndex == -1) {
                    buffer.append("left=").append("ROOT");
                } else {
                    buffer.append("left=").append(left.getKey()).append(" [").append(left.bitIndex).append("]");
                }
            } else {
                buffer.append("left=").append("null");
            }
            buffer.append(", ");

            if (right != null) {
                if (right.bitIndex == -1) {
                    buffer.append("right=").append("ROOT");
                } else {
                    buffer.append("right=").append(right.getKey()).append(" [").append(right.bitIndex).append("]");
                }
            } else {
                buffer.append("right=").append("null");
            }
            buffer.append(", ");

            if (predecessor != null) {
                if (predecessor.bitIndex == -1) {
                    buffer.append("predecessor=").append("ROOT");
                } else {
                    buffer.append("predecessor=").append(predecessor.getKey()).append(" [").append(predecessor.bitIndex).append("]");
                }
            }

            buffer.append(")");
            return buffer.toString();
        }
    }


    /**
     * This is a entry set view of the {@link PrefixTree} as returned
     * by {@link Map#entrySet()}
     */
    private class EntrySet extends AbstractSet<Map.Entry<K, V>> {

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        @Override
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }

            TrieEntry<K, V> candidate = getEntry(((Map.Entry<?, ?>) o).getKey());
            return candidate != null && candidate.equals(o);
        }

        @Override
        public boolean remove(Object o) {
            int size = size();
            PatriciaTrie.this.remove(o);
            return size != size();
        }

        @Override
        public int size() {
            return PatriciaTrie.this.size();
        }

        @Override
        public void clear() {
            PatriciaTrie.this.clear();
        }

        /**
         * An {@link Iterator} that returns {@link Entry} Objects
         */
        private class EntryIterator extends TrieIterator<Map.Entry<K, V>> {
            @Override
            public Map.Entry<K, V> next() {
                return nextEntry();
            }
        }
    }

    /**
     * This is a key set view of the {@link PrefixTree} as returned
     * by {@link Map#keySet()}
     */
    private class KeySet extends AbstractSet<K> {

        @Override
        public Iterator<K> iterator() {
            return new KeyIterator();
        }

        @Override
        public int size() {
            return PatriciaTrie.this.size();
        }

        @Override
        public boolean contains(Object o) {
            return containsKey(o);
        }

        @Override
        public boolean remove(Object o) {
            int size = size();
            PatriciaTrie.this.remove(o);
            return size != size();
        }

        @Override
        public void clear() {
            PatriciaTrie.this.clear();
        }

        /**
         * An {@link Iterator} that returns Key Objects
         */
        private class KeyIterator extends TrieIterator<K> {
            @Override
            public K next() {
                return nextEntry().getKey();
            }
        }
    }

    /**
     * This is a value view of the {@link PrefixTree} as returned
     * by {@link Map#values()}
     */
    private class Values extends AbstractCollection<V> {

        @Override
        public Iterator<V> iterator() {
            return new ValueIterator();
        }

        @Override
        public int size() {
            return PatriciaTrie.this.size();
        }

        @Override
        public boolean contains(Object o) {
            return containsValue(o);
        }

        @Override
        public void clear() {
            PatriciaTrie.this.clear();
        }

        @Override
        public boolean remove(Object o) {
            for (Iterator<V> it = iterator(); it.hasNext(); ) {
                V value = it.next();
                if ((value == null ? o == null : value.equals(o))) {
                    it.remove();
                    return true;
                }
            }
            return false;
        }

        /**
         * An {@link Iterator} that returns Value Objects
         */
        private class ValueIterator extends TrieIterator<V> {
            @Override
            public V next() {
                return nextEntry().getValue();
            }
        }
    }

    /**
     * An iterator for the entries.
     */
    abstract class TrieIterator<E> implements Iterator<E> {

        /**
         * For fast-fail
         */
        protected int expectedModCount = PatriciaTrie.this.modCount;

        protected TrieEntry<K, V> next; // the next node to return
        protected TrieEntry<K, V> current; // the current entry we're on

        /**
         * Starts iteration from the root
         */
        protected TrieIterator() {
            next = PatriciaTrie.this.nextEntry(null);
        }

        /**
         * Starts iteration at the given entry
         */
        protected TrieIterator(TrieEntry<K, V> firstEntry) {
            next = firstEntry;
        }

        /**
         * Returns the next {@link TrieEntry}
         */
        protected TrieEntry<K, V> nextEntry() {
            if (expectedModCount != PatriciaTrie.this.modCount) {
                throw new ConcurrentModificationException();
            }

            TrieEntry<K, V> e = next;
            if (e == null) {
                throw new NoSuchElementException();
            }

            next = findNext(e);
            current = e;
            return e;
        }

        /**
         * @see PatriciaTrie#nextEntry(TrieEntry)
         */
        protected TrieEntry<K, V> findNext(TrieEntry<K, V> prior) {
            return PatriciaTrie.this.nextEntry(prior);
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public void remove() {
            if (current == null) {
                throw new IllegalStateException();
            }

            if (expectedModCount != PatriciaTrie.this.modCount) {
                throw new ConcurrentModificationException();
            }

            TrieEntry<K, V> node = current;
            current = null;
            PatriciaTrie.this.removeEntry(node);

            expectedModCount = PatriciaTrie.this.modCount;
        }
    }
    /**
     * Returns an entry strictly higher than the given key,
     * or null if no such entry exists.
     */
    private TrieEntry<K, V> higherEntry(K key) {
        // TODO: Cleanup so that we don't actually have to add/remove from the
        //     tree.  (We do it here because there are other well-defined
        //     functions to perform the search.)
        int lengthInBits = lengthInBits(key);

        if (lengthInBits == 0) {
            if (!root.isEmpty()) {
                // If data in root, and more after -- return it.
                if (size() > 1) {
                    return nextEntry(root);
                } else { // If no more after, no higher entry.
                    return null;
                }
            } else {
                // Root is empty & we want something after empty, return first.
                return firstEntry();
            }
        }
        TrieEntry<K, V> found = getNearestEntryForKey(key);
        if (compareKeys(key, found.key)) {
            return nextEntry(found);
        }
        int bitIndex = bitIndex(key, found.key);
        if (0 <= bitIndex) {
            TrieEntry<K, V> added = new TrieEntry<K, V>(key, null, bitIndex);
            addEntry(added);
            incrementSize(); // must increment because remove will decrement
            TrieEntry<K, V> ceil = nextEntry(added);
            removeEntry(added);
            modCount -= 2; // we didn't really modify it.
            return ceil;
        } else if (bitIndex == KeyAnalyzer.NULL_BIT_KEY) {
            if (!root.isEmpty()) {
                return firstEntry();
            } else if (size() > 1) {
                return nextEntry(firstEntry());
            } else {
                return null;
            }
        } else if (bitIndex == KeyAnalyzer.EQUAL_BIT_KEY) {
            return nextEntry(found);
        }
        // we should have exited above
        throw new IllegalStateException("invalid lookup: " + key);
    }

    /**
     * Returns a key-value mapping associated with the least key greater
     * than or equal to the given key, or null if there is no such key.
     */
    TrieEntry<K, V> ceilingEntry(K key) {
        // Basically:
        // Follow the steps of adding an entry, but instead...
        //
        // - If we ever encounter a situation where we found an equal
        //   key, we return it immediately.
        //
        // - If we hit an empty root, return the first iterable item.
        //
        // - If we have to add a new item, we temporarily add it,
        //   find the successor to it, then remove the added item.
        //
        // These steps ensure that the returned value is either the
        // entry for the key itself, or the first entry directly after
        // the key.

        // TODO: Cleanup so that we don't actually have to add/remove from the
        //     tree.  (We do it here because there are other well-defined
        //     functions to perform the search.)
        int lengthInBits = lengthInBits(key);

        if (lengthInBits == 0) {
            if (!root.isEmpty()) {
                return root;
            } else {
                return firstEntry();
            }
        }
        TrieEntry<K, V> found = getNearestEntryForKey(key);
        if (compareKeys(key, found.key)) {
            return found;
        }
        int bitIndex = bitIndex(key, found.key);
        if (0 <= bitIndex) {
            TrieEntry<K, V> added = new TrieEntry<K, V>(key, null, bitIndex);
            addEntry(added);
            incrementSize(); // must increment because remove will decrement
            TrieEntry<K, V> ceil = nextEntry(added);
            removeEntry(added);
            modCount -= 2; // we didn't really modify it.
            return ceil;
        } else if (bitIndex == KeyAnalyzer.NULL_BIT_KEY) {
            if (!root.isEmpty()) {
                return root;
            } else {
                return firstEntry();
            }
        } else if (bitIndex == KeyAnalyzer.EQUAL_BIT_KEY) {
            return found;
        }
        // we should have exited above.
        throw new IllegalStateException("invalid lookup: " + key);
    }

    /**
     * Returns a key-value mapping associated with the greatest key
     * strictly less than the given key, or null if there is no such key.
     */
    TrieEntry<K, V> lowerEntry(K key) {
        // Basically:
        // Follow the steps of adding an entry, but instead...
        //
        // - If we ever encounter a situation where we found an equal
        //   key, we return it's previousEntry immediately.
        //
        // - If we hit root (empty or not), return null.
        //
        // - If we have to add a new item, we temporarily add it,
        //   find the previousEntry to it, then remove the added item.
        //
        // These steps ensure that the returned value is always just before
        // the key or null (if there was nothing before it).

        // TODO: Cleanup so that we don't actually have to add/remove from the
        //     tree.  (We do it here because there are other well-defined
        //     functions to perform the search.)
        int lengthInBits = lengthInBits(key);
        if (lengthInBits == 0) {
            return null; // there can never be anything before root.
        }
        TrieEntry<K, V> found = getNearestEntryForKey(key);
        if (compareKeys(key, found.key)) {
            return previousEntry(found);
        }
        int bitIndex = bitIndex(key, found.key);
        if (0 <= bitIndex) {
            TrieEntry<K, V> added = new TrieEntry<>(key, null, bitIndex);
            addEntry(added);
            incrementSize(); // must increment because remove will decrement
            TrieEntry<K, V> prior = previousEntry(added);
            removeEntry(added);
            modCount -= 2; // we didn't really modify it.
            return prior;
        } else if (bitIndex == KeyAnalyzer.NULL_BIT_KEY) {
            return null;
        } else if (bitIndex == KeyAnalyzer.EQUAL_BIT_KEY) {
            return previousEntry(found);
        }
        // we should have exited above.
        throw new IllegalStateException("invalid lookup: " + key);
    }

    /**
     * Returns a key-value mapping associated with the greatest key
     * less than or equal to the given key, or null if there is no such key.
     */
    TrieEntry<K, V> floorEntry(K key) {
        // TODO: Cleanup so that we don't actually have to add/remove from the
        //     tree.  (We do it here because there are other well-defined
        //     functions to perform the search.)
        int lengthInBits = lengthInBits(key);
        if (lengthInBits == 0) {
            if (!root.isEmpty()) {
                return root;
            } else {
                return null;
            }
        }
        TrieEntry<K, V> found = getNearestEntryForKey(key);
        if (compareKeys(key, found.key)) {
            return found;
        }
        int bitIndex = bitIndex(key, found.key);
        if (0 <= bitIndex) {
            TrieEntry<K, V> added = new TrieEntry<>(key, null, bitIndex);
            addEntry(added);
            incrementSize(); // must increment because remove will decrement
            TrieEntry<K, V> floor = previousEntry(added);
            removeEntry(added);
            modCount -= 2; // we didn't really modify it.
            return floor;
        } else if (bitIndex == KeyAnalyzer.NULL_BIT_KEY) {
            if (!root.isEmpty()) {
                return root;
            } else {
                return null;
            }
        } else if (bitIndex == KeyAnalyzer.EQUAL_BIT_KEY) {
            return found;
        }
        // we should have exited above.
        throw new IllegalStateException("invalid lookup: " + key);
    }

    /**
     * Finds the subtree that contains the prefix.
     * <p>
     * This is very similar to getR but with the difference that
     * we stop the lookup if h.bitIndex > lengthInBits.
     */
    private TrieEntry<K, V> subtree(K prefix) {
        int lengthInBits = lengthInBits(prefix);

        TrieEntry<K, V> current = root.left;
        TrieEntry<K, V> path = root;
        while (current.bitIndex > path.bitIndex && lengthInBits >= current.bitIndex) {
            path = current;
            if (!isBitSet(prefix, current.bitIndex)) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        // Make sure the entry is valid for a subtree.
        TrieEntry<K, V> entry = current.isEmpty() ? path : current;

        // If entry is root, it can't be empty.
        if (entry.isEmpty()) {
            return null;
        }

        // if root && length of root is less than length of lookup,
        // there's nothing.
        // (this prevents returning the whole subtree if root has an empty
        //  string and we want to lookup things with "\0")
        if (entry == root && lengthInBits(entry.getKey()) < lengthInBits) {
            return null;
        }

        // Found key's length-th bit differs from our key
        // which means it cannot be the prefix...
        if (isBitSet(prefix, lengthInBits)
                != isBitSet(entry.key, lengthInBits)) {
            return null;
        }

        // ... or there are less than 'length' equal bits
        int bitIndex = bitIndex(prefix, entry.key);
        if (bitIndex >= 0 && bitIndex < lengthInBits) {
            return null;
        }

        return entry;
    }

    /**
     * Returns the last entry the {@link PrefixTree} is storing.
     *
     * <p>This is implemented by going always to the right until
     * we encounter a valid uplink. That uplink is the last key.
     */
    private TrieEntry<K, V> lastEntry() {
        return followRight(root.left);
    }

    /**
     * Traverses down the right path until it finds an uplink.
     */
    private TrieEntry<K, V> followRight(TrieEntry<K, V> node) {
        // if Trie is empty, no last entry.
        if (node.right == null) {
            return null;
        }

        // Go as far right as possible, until we encounter an uplink.
        while (node.right.bitIndex > node.bitIndex) {
            node = node.right;
        }

        return node.right;
    }

    /**
     * Returns the node lexicographically before the given node (or null if none).
     * <p>
     * This follows four simple branches:
     * - If the uplink that returned us was a right uplink:
     * - If predecessor's left is a valid uplink from predecessor, return it.
     * - Else, follow the right path from the predecessor's left.
     * - If the uplink that returned us was a left uplink:
     * - Loop back through parents until we encounter a node where
     * node != node.parent.left.
     * - If node.parent.left is uplink from node.parent:
     * - If node.parent.left is not root, return it.
     * - If it is root & root isEmpty, return null.
     * - If it is root & root !isEmpty, return root.
     * - If node.parent.left is not uplink from node.parent:
     * - Follow right path for first right child from node.parent.left
     *
     * @param start
     */
    private TrieEntry<K, V> previousEntry(TrieEntry<K, V> start) {
        if (start.predecessor == null) {
            throw new IllegalArgumentException("must have come from somewhere!");
        }

        if (start.predecessor.right == start) {
            if (isValidUplink(start.predecessor.left, start.predecessor)) {
                return start.predecessor.left;
            } else {
                return followRight(start.predecessor.left);
            }
        } else {
            TrieEntry<K, V> node = start.predecessor;
            while (node.parent != null && node == node.parent.left) {
                node = node.parent;
            }

            if (node.parent == null) { // can be null if we're looking up root.
                return null;
            }

            if (isValidUplink(node.parent.left, node.parent)) {
                if (node.parent.left == root) {
                    if (root.isEmpty()) {
                        return null;
                    } else {
                        return root;
                    }

                } else {
                    return node.parent.left;
                }
            } else {
                return followRight(node.parent.left);
            }
        }
    }

    /**
     * Returns the entry lexicographically after the given entry.
     * If the given entry is null, returns the first node.
     * <p>
     * This will traverse only within the subtree.  If the given node
     * is not within the subtree, this will have undefined results.
     */
    private TrieEntry<K, V> nextEntryInSubtree(TrieEntry<K, V> node,
                                               TrieEntry<K, V> parentOfSubtree) {
        if (node == null) {
            return firstEntry();
        } else {
            return nextEntryImpl(node.predecessor, node, parentOfSubtree);
        }
    }

    private boolean isPrefix(K key, K prefix) {
        return keyAnalyzer.isPrefix(key, prefix);
    }

    /**
     * A range view of the {@link PrefixTree}
     */
    abstract class RangeMap extends AbstractMap<K, V>
            implements SortedMap<K, V> {

        /**
         * The {@link #entrySet()} view
         */
        private transient volatile Set<Map.Entry<K, V>> entrySet;

        /**
         * Creates and returns an {@link #entrySet()}
         * view of the {@link RangeMap}
         */
        protected abstract Set<Map.Entry<K, V>> createEntrySet();

        /**
         * Returns the FROM Key
         */
        protected abstract K getFromKey();

        /**
         * Whether or not the {@link #getFromKey()} is in the range
         */
        protected abstract boolean isFromInclusive();

        /**
         * Returns the TO Key
         */
        protected abstract K getToKey();

        /**
         * Whether or not the {@link #getToKey()} is in the range
         */
        protected abstract boolean isToInclusive();


        @Override
        public Comparator<? super K> comparator() {
            return PatriciaTrie.this.comparator();
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean containsKey(Object key) {
            if (!inRange((K) key)) {
                return false;
            }

            return PatriciaTrie.this.containsKey(key);
        }

        @SuppressWarnings("unchecked")
        @Override
        public V remove(Object key) {
            if (!inRange((K) key)) {
                return null;
            }
            return PatriciaTrie.this.remove(key);
        }

        @SuppressWarnings("unchecked")
        @Override
        public V get(Object key) {
            if (!inRange((K) key)) {
                return null;
            }
            return PatriciaTrie.this.get(key);
        }

        @Override
        public V put(K key, V value) {
            if (!inRange(key)) {
                throw new IllegalArgumentException(
                        "Key is out of range: " + key);
            }
            return PatriciaTrie.this.put(key, value);
        }

        @Override
        public Set<Map.Entry<K, V>> entrySet() {
            if (entrySet == null) {
                entrySet = createEntrySet();
            }
            return entrySet;
        }

        @Override
        public SortedMap<K, V> subMap(K fromKey, K toKey) {
            if (!inRange2(fromKey)) {
                throw new IllegalArgumentException("FromKey is out of range: " + fromKey);
            }
            if (!inRange2(toKey)) {
                throw new IllegalArgumentException("ToKey is out of range: " + toKey);
            }
            return createRangeMap(fromKey, isFromInclusive(), toKey, isToInclusive());
        }

        @Override
        public SortedMap<K, V> headMap(K toKey) {
            if (!inRange2(toKey)) {
                throw new IllegalArgumentException("ToKey is out of range: " + toKey);
            }
            return createRangeMap(getFromKey(), isFromInclusive(), toKey, isToInclusive());
        }

        @Override
        public SortedMap<K, V> tailMap(K fromKey) {
            if (!inRange2(fromKey)) {
                throw new IllegalArgumentException("FromKey is out of range: " + fromKey);
            }
            return createRangeMap(fromKey, isFromInclusive(), getToKey(), isToInclusive());
        }

        /**
         * Returns true if the provided key is greater than TO and
         * less than FROM
         */
        protected boolean inRange(K key) {
            K fromKey = getFromKey();
            K toKey = getToKey();
            return (fromKey == null || inFromRange(key, false))
                    && (toKey == null || inToRange(key, false));
        }

        /**
         * This form allows the high endpoint (as well as all legit keys)
         */
        protected boolean inRange2(K key) {
            K fromKey = getFromKey();
            K toKey = getToKey();
            return (fromKey == null || inFromRange(key, false))
                    && (toKey == null || inToRange(key, true));
        }

        /**
         * Returns true if the provided key is in the FROM range
         * of the {@link RangeMap}
         */
        protected boolean inFromRange(K key, boolean forceInclusive) {
            K fromKey = getFromKey();
            boolean fromInclusive = isFromInclusive();
            int ret = keyAnalyzer.compare(key, fromKey);
            if (fromInclusive || forceInclusive) {
                return ret >= 0;
            } else {
                return ret > 0;
            }
        }

        /**
         * Returns true if the provided key is in the TO range
         * of the {@link RangeMap}
         */
        protected boolean inToRange(K key, boolean forceInclusive) {
            K toKey = getToKey();
            boolean toInclusive = isToInclusive();
            int ret = keyAnalyzer.compare(key, toKey);
            if (toInclusive || forceInclusive) {
                return ret <= 0;
            } else {
                return ret < 0;
            }
        }

        /**
         * Creates and returns a sub-range view of the current {@link RangeMap}
         */
        protected abstract SortedMap<K, V> createRangeMap(K fromKey,
                                                          boolean fromInclusive, K toKey, boolean toInclusive);
    }

    /**
     * A {@link RangeMap} that deals with {@link Entry}s
     */
    private class RangeEntryMap extends RangeMap {

        /**
         * The key to start from, null if the beginning.
         */
        protected final K fromKey;

        /**
         * The key to end at, null if till the end.
         */
        protected final K toKey;

        /**
         * Whether or not the 'from' is inclusive.
         */
        protected final boolean fromInclusive;

        /**
         * Whether or not the 'to' is inclusive.
         */
        protected final boolean toInclusive;

        /**
         * Creates a {@link RangeEntryMap} with the fromKey included and
         * the toKey excluded from the range
         */
        protected RangeEntryMap(K fromKey, K toKey) {
            this(fromKey, true, toKey, false);
        }

        /**
         * Creates a {@link RangeEntryMap}
         */
        protected RangeEntryMap(K fromKey, boolean fromInclusive,
                                K toKey, boolean toInclusive) {
            if (fromKey == null && toKey == null) {
                throw new IllegalArgumentException("must have a from or to!");
            }
            if (fromKey != null && toKey != null
                    && keyAnalyzer.compare(fromKey, toKey) > 0) {
                throw new IllegalArgumentException("fromKey > toKey");
            }
            this.fromKey = fromKey;
            this.fromInclusive = fromInclusive;
            this.toKey = toKey;
            this.toInclusive = toInclusive;
        }


        @Override
        public K firstKey() {
            Map.Entry<K, V> e;
            if (fromKey == null) {
                e = firstEntry();
            } else {
                if (fromInclusive) {
                    e = ceilingEntry(fromKey);
                } else {
                    e = higherEntry(fromKey);
                }
            }
            K first = e != null ? e.getKey() : null;
            if (e == null || toKey != null && !inToRange(first, false)) {
                throw new NoSuchElementException();
            }
            return first;
        }


        @Override
        public K lastKey() {
            Map.Entry<K, V> e;
            if (toKey == null) {
                e = lastEntry();
            } else {
                if (toInclusive) {
                    e = floorEntry(toKey);
                } else {
                    e = lowerEntry(toKey);
                }
            }
            K last = e != null ? e.getKey() : null;
            if (e == null || fromKey != null && !inFromRange(last, false)) {
                throw new NoSuchElementException();
            }
            return last;
        }

        @Override
        protected Set<Entry<K, V>> createEntrySet() {
            return new RangeEntrySet(this);
        }

        @Override
        public K getFromKey() {
            return fromKey;
        }

        @Override
        public K getToKey() {
            return toKey;
        }

        @Override
        public boolean isFromInclusive() {
            return fromInclusive;
        }

        @Override
        public boolean isToInclusive() {
            return toInclusive;
        }

        @Override
        protected SortedMap<K, V> createRangeMap(K fromKey, boolean fromInclusive,
                                                 K toKey, boolean toInclusive) {
            return new RangeEntryMap(fromKey, fromInclusive, toKey, toInclusive);
        }
    }

    /**
     * A {@link Set} view of a {@link RangeMap}
     */
    private class RangeEntrySet extends AbstractSet<Map.Entry<K, V>> {

        private final RangeMap delegate;

        private int size = -1;

        private int expectedModCount = -1;

        /**
         * Creates a {@link RangeEntrySet}
         */
        public RangeEntrySet(RangeMap delegate) {
            if (delegate == null) {
                throw new NullPointerException("delegate");
            }

            this.delegate = delegate;
        }

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            K fromKey = delegate.getFromKey();
            K toKey = delegate.getToKey();

            TrieEntry<K, V> first = null;
            if (fromKey == null) {
                first = firstEntry();
            } else {
                first = ceilingEntry(fromKey);
            }

            TrieEntry<K, V> last = null;
            if (toKey != null) {
                last = ceilingEntry(toKey);
            }

            return new EntryIterator(first, last);
        }

        @Override
        public int size() {
            if (size == -1 || expectedModCount != PatriciaTrie.this.modCount) {
                size = 0;

                for (Iterator<?> it = iterator(); it.hasNext(); it.next()) {
                    ++size;
                }

                expectedModCount = PatriciaTrie.this.modCount;
            }
            return size;
        }

        @Override
        public boolean isEmpty() {
            return !iterator().hasNext();
        }

        @Override
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry<?, ?>)) {
                return false;
            }
            @SuppressWarnings("unchecked")
            Map.Entry<K, V> entry = (Map.Entry<K, V>) o;
            K key = entry.getKey();
            if (!delegate.inRange(key)) {
                return false;
            }
            TrieEntry<K, V> node = getEntry(key);
            return node != null && (node.getValue() == null ? entry.getValue() == null : node.getValue().equals(entry.getValue()));
        }

        @Override
        public boolean remove(Object o) {
            if (!(o instanceof Map.Entry<?, ?>)) {
                return false;
            }
            @SuppressWarnings("unchecked")
            Map.Entry<K, V> entry = (Map.Entry<K, V>) o;
            K key = entry.getKey();
            if (!delegate.inRange(key)) {
                return false;
            }
            TrieEntry<K, V> node = getEntry(key);
            if (node != null && (node.getValue() == null ? entry.getValue() == null : node.getValue().equals(entry.getValue()))) {
                removeEntry(node);
                return true;
            }
            return false;
        }

        /**
         * An {@link Iterator} for {@link RangeEntrySet}s.
         */
        private final class EntryIterator extends TrieIterator<Map.Entry<K, V>> {

            private final K excludedKey;

            /**
             * Creates a {@link EntryIterator}
             */
            private EntryIterator(TrieEntry<K, V> first, TrieEntry<K, V> last) {
                super(first);
                this.excludedKey = (last != null ? last.getKey() : null);
            }

            @Override
            public boolean hasNext() {
                return next != null && !(next.key == null ? excludedKey == null : next.key.equals(excludedKey));
            }

            @Override
            public Map.Entry<K, V> next() {
                if (next == null || (next.key == null ? excludedKey == null : next.key.equals(excludedKey))) {
                    throw new NoSuchElementException();
                }
                return nextEntry();
            }
        }
    }

    /**
     * A submap used for prefix views over the {@link PrefixTree}.
     */
    private class PrefixRangeMap extends RangeMap {

        private final K prefix;

        private K fromKey = null;

        private K toKey = null;

        private int expectedModCount = -1;

        private int size = -1;

        /**
         * Creates a {@link PrefixRangeMap}
         */
        private PrefixRangeMap(K prefix) {
            this.prefix = prefix;
        }

        /**
         * This method does two things. It determinates the FROM
         * and TO range of the {@link PrefixRangeMap} and the number
         * of elements in the range. This method must be called every
         * time the {@link PrefixTree} has changed.
         */
        private int fixup() {
            // The trie has changed since we last
            // found our toKey / fromKey
            if (size == -1 || PatriciaTrie.this.modCount != expectedModCount) {
                Iterator<Map.Entry<K, V>> it = entrySet().iterator();
                size = 0;

                Map.Entry<K, V> entry = null;
                if (it.hasNext()) {
                    entry = it.next();
                    size = 1;
                }

                fromKey = entry == null ? null : entry.getKey();
                if (fromKey != null) {
                    TrieEntry<K, V> prior = previousEntry((TrieEntry<K, V>) entry);
                    fromKey = prior == null ? null : prior.getKey();
                }

                toKey = fromKey;

                while (it.hasNext()) {
                    ++size;
                    entry = it.next();
                }

                toKey = entry == null ? null : entry.getKey();

                if (toKey != null) {
                    entry = nextEntry((TrieEntry<K, V>) entry);
                    toKey = entry == null ? null : entry.getKey();
                }
                expectedModCount = PatriciaTrie.this.modCount;
            }
            return size;
        }

        @Override
        public K firstKey() {
            fixup();
            Map.Entry<K, V> e;
            if (fromKey == null) {
                e = firstEntry();
            } else {
                e = higherEntry(fromKey);
            }
            K first = e != null ? e.getKey() : null;
            if (e == null || !isPrefix(first, prefix)) {
                throw new NoSuchElementException();
            }
            return first;
        }

        @Override
        public K lastKey() {
            fixup();
            Map.Entry<K, V> e;
            if (toKey == null) {
                e = lastEntry();
            } else {
                e = lowerEntry(toKey);
            }
            K last = e != null ? e.getKey() : null;
            if (e == null || !isPrefix(last, prefix)) {
                throw new NoSuchElementException();
            }
            return last;
        }

        /**
         * Returns true if this {@link PrefixRangeMap}'s key is a prefix
         * of the provided key.
         */
        @Override
        protected boolean inRange(K key) {
            return isPrefix(key, prefix);
        }

        /**
         * Same as {@link #inRange(Object)}
         */
        @Override
        protected boolean inRange2(K key) {
            return inRange(key);
        }

        /**
         * Returns true if the provided Key is in the FROM range
         * of the {@link PrefixRangeMap}
         */
        @Override
        protected boolean inFromRange(K key, boolean forceInclusive) {
            return isPrefix(key, prefix);
        }

        /**
         * Returns true if the provided Key is in the TO range
         * of the {@link PrefixRangeMap}
         */
        @Override
        protected boolean inToRange(K key, boolean forceInclusive) {
            return isPrefix(key, prefix);
        }

        @Override
        protected Set<Map.Entry<K, V>> createEntrySet() {
            return new PrefixRangeEntrySet(this);
        }

        @Override
        public K getFromKey() {
            return fromKey;
        }

        @Override
        public K getToKey() {
            return toKey;
        }

        @Override
        public boolean isFromInclusive() {
            return false;
        }

        @Override
        public boolean isToInclusive() {
            return false;
        }

        @Override
        protected SortedMap<K, V> createRangeMap(K fromKey, boolean fromInclusive,
                K toKey, boolean toInclusive) {
            return new RangeEntryMap(fromKey, fromInclusive, toKey, toInclusive);
        }
    }

    /**
     * A prefix {@link RangeEntrySet} view of the {@link PrefixTree}
     */
    private final class PrefixRangeEntrySet extends RangeEntrySet {

        private final PrefixRangeMap delegate;

        private TrieEntry<K, V> prefixStart;

        private int expectedModCount = -1;

        /**
         * Creates a {@link PrefixRangeEntrySet}
         */
        public PrefixRangeEntrySet(PrefixRangeMap delegate) {
            super(delegate);
            this.delegate = delegate;
        }

        @Override
        public int size() {
            return delegate.fixup();
        }

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            if (PatriciaTrie.this.modCount != expectedModCount) {
                prefixStart = subtree(delegate.prefix);
                expectedModCount = PatriciaTrie.this.modCount;
            }

            if (prefixStart == null) {
                Set<Map.Entry<K, V>> empty = Collections.emptySet();
                return empty.iterator();
            } else if (lengthInBits(delegate.prefix) >= prefixStart.bitIndex) {
                return new SingletonIterator(prefixStart);
            } else {
                return new EntryIterator(prefixStart, delegate.prefix);
            }
        }

        /**
         * An {@link Iterator} that holds a single {@link TrieEntry}.
         */
        private final class SingletonIterator implements Iterator<Map.Entry<K, V>> {

            private final TrieEntry<K, V> entry;

            private int hit = 0;

            public SingletonIterator(TrieEntry<K, V> entry) {
                this.entry = entry;
            }

            @Override
            public boolean hasNext() {
                return hit == 0;
            }

            @Override
            public Map.Entry<K, V> next() {
                if (hit != 0) {
                    throw new NoSuchElementException();
                }

                ++hit;
                return entry;
            }


            @Override
            public void remove() {
                if (hit != 1) {
                    throw new IllegalStateException();
                }

                ++hit;
                PatriciaTrie.this.removeEntry(entry);
            }
        }

        /**
         * An {@link Iterator} for iterating over a prefix search.
         */
        private final class EntryIterator extends TrieIterator<Map.Entry<K, V>> {

            // values to reset the subtree if we remove it.
            final K prefix;
            boolean lastOne;

            TrieEntry<K, V> subtree; // the subtree to search within

            /**
             * Starts iteration at the given entry & search only
             * within the given subtree.
             */
            EntryIterator(TrieEntry<K, V> startScan, K prefix) {
                subtree = startScan;
                next = PatriciaTrie.this.followLeft(startScan);
                this.prefix = prefix;
            }

            @Override
            public Map.Entry<K, V> next() {
                Map.Entry<K, V> entry = nextEntry();
                if (lastOne) {
                    next = null;
                }
                return entry;
            }

            @Override
            protected TrieEntry<K, V> findNext(TrieEntry<K, V> prior) {
                return PatriciaTrie.this.nextEntryInSubtree(prior, subtree);
            }

            @Override
            public void remove() {
                // If the current entry we're removing is the subtree
                // then we need to find a new subtree parent.
                boolean needsFixing = false;
                int bitIdx = subtree.bitIndex;
                if (current == subtree) {
                    needsFixing = true;
                }

                super.remove();

                // If the subtree changed its bitIndex or we
                // removed the old subtree, get a new one.
                if (bitIdx != subtree.bitIndex || needsFixing) {
                    subtree = subtree(prefix);
                }

                // If the subtree's bitIndex is less than the
                // length of our prefix, it's the last item
                // in the prefix tree.
                if (lengthInBits(prefix) >= subtree.bitIndex) {
                    lastOne = true;
                }
            }
        }
    }
}