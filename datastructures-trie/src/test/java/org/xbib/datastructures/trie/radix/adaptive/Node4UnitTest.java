package org.xbib.datastructures.trie.radix.adaptive;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class Node4UnitTest extends InnerNodeUnitTest {

	Node4UnitTest() {
		super(2);
	}

	@Test
	public void testGetOnlyChild() {
		// remove until only one child
		while (node.size() != 1) {
			node.removeChild(node.first().uplinkKey());
		}

		byte[] keys = existingKeys();
		sortDescending(keys);
		assertEquals(keys[0], ((Node4) node).getOnlyChildKey());
	}

	@Override
	@Test
	public void testShrink() {
		Assertions.assertThrows(UnsupportedOperationException.class, () -> node.shrink());
	}

	@Test
	public void testShouldShrinkAlwaysFalse() {
		// remove all
		while (node.size() != 0) {
			node.removeChild(node.first().uplinkKey());
		}
		assertFalse(node.shouldShrink());
	}
}
