package org.xbib.datastructures.trie.radix.adaptive;

class Node4 extends InnerNode {

    static final int NODE_SIZE = 4;
    // 2^7 = 128
    private static final int BYTE_SHIFT = 1 << Byte.SIZE - 1;
    // each array element would contain the partial byte key to match
    // if key matches then take up the same index from the child pointer array
    private final byte[] keys = new byte[NODE_SIZE];

    Node4() {
        super(NODE_SIZE);
    }

    Node4(Node16 node16) {
        super(node16, NODE_SIZE);
        assert node16.shouldShrink();
        byte[] keys = node16.getKeys();
        Node[] child = node16.getChild();
        System.arraycopy(keys, 0, this.keys, 0, node16.noOfChildren);
        System.arraycopy(child, 0, this.child, 0, node16.noOfChildren);

        // update up links
        for (int i = 0; i < noOfChildren; i++) {
            replaceUplink(this, this.child[i]);
        }
    }

    /**
     * For Node4, Node16 to interpret every byte as unsigned when storing partial keys.
     * Node 48, Node256 simply use {@link Byte#toUnsignedInt(byte)}
     * to index into their key arrays.
     */
    static byte unsigned(byte b) {
        return (byte) (b ^ BYTE_SHIFT);
    }

    // passed b must have been interpreted as unsigned already
    // this is the reverse of unsigned
    static byte signed(byte b) {
        return unsigned(b);
    }

    @Override
    public Node findChild(byte partialKey) {
        partialKey = unsigned(partialKey);
        // paper does simple loop over because it's a tiny array of size 4
        for (int i = 0; i < noOfChildren; i++) {
            if (keys[i] == partialKey) {
                return child[i];
            }
        }
        return null;
    }

    @Override
    public void addChild(byte partialKey, Node child) {
        assert !isFull();
        byte unsignedPartialKey = unsigned(partialKey);
        // shift elements from this point to right by one place
        // noOfChildren here would never be == Node_SIZE (since we have isFull() check)
        int i = noOfChildren;
        for (; i > 0 && unsignedPartialKey < keys[i - 1]; i--) {
            keys[i] = keys[i - 1];
            this.child[i] = this.child[i - 1];
        }
        keys[i] = unsignedPartialKey;
        this.child[i] = child;
        noOfChildren++;
        createUplink(this, child, partialKey);
    }

    @Override
    public void replace(byte partialKey, Node newChild) {
        byte unsignedPartialKey = unsigned(partialKey);

        int index = 0;
        for (; index < noOfChildren; index++) {
            if (keys[index] == unsignedPartialKey) {
                break;
            }
        }
        // replace will be called from in a state where you know partialKey entry surely exists
        assert index < noOfChildren : "Partial key does not exist";
        child[index] = newChild;
        createUplink(this, newChild, partialKey);
    }

    @Override
    public void removeChild(byte partialKey) {
        partialKey = unsigned(partialKey);
        int index = 0;
        for (; index < noOfChildren; index++) {
            if (keys[index] == partialKey) {
                break;
            }
        }
        // if this fails, the question is, how could you reach the leaf node?
        // this node must've been your follow on pointer holding the partialKey
        assert index < noOfChildren : "Partial key does not exist";
        removeUplink(child[index]);
        for (int i = index; i < noOfChildren - 1; i++) {
            keys[i] = keys[i + 1];
            child[i] = child[i + 1];
        }
        child[noOfChildren - 1] = null;
        noOfChildren--;
    }

    @Override
    public InnerNode grow() {
        assert isFull();
        // grow from Node4 to Node16
        return new Node16(this);
    }

    @Override
    public boolean shouldShrink() {
        return false;
    }

    @Override
    public InnerNode shrink() {
        throw new UnsupportedOperationException("Node4 is smallest node type");
    }

    @Override
    public Node first() {
        return child[0];
    }

    @Override
    public Node last() {
        return child[Math.max(0, noOfChildren - 1)];
    }

    @Override
    public Node ceil(byte partialKey) {
        partialKey = unsigned(partialKey);
        for (int i = 0; i < noOfChildren; i++) {
            if (keys[i] >= partialKey) {
                return child[i];
            }
        }
        return null;
    }

    @Override
    public Node greater(byte partialKey) {
        partialKey = unsigned(partialKey);
        for (int i = 0; i < noOfChildren; i++) {
            if (keys[i] > partialKey) {
                return child[i];
            }
        }
        return null;
    }

    @Override
    public Node lesser(byte partialKey) {
        partialKey = unsigned(partialKey);
        for (int i = noOfChildren - 1; i >= 0; i--) {
            if (keys[i] < partialKey) {
                return child[i];
            }
        }
        return null;
    }

    @Override
    public Node floor(byte partialKey) {
        partialKey = unsigned(partialKey);
        for (int i = noOfChildren - 1; i >= 0; i--) {
            if (keys[i] <= partialKey) {
                return child[i];
            }
        }
        return null;
    }

    @Override
    public boolean isFull() {
        return noOfChildren == NODE_SIZE;
    }

    byte[] getKeys() {
        return keys;
    }

    byte getOnlyChildKey() {
        assert noOfChildren == 1;
        return signed(keys[0]);
    }
}
