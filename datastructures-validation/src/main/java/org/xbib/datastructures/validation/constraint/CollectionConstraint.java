/*
 * Copyright (C) 2018-2022 Toshiaki Maki <makingx@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xbib.datastructures.validation.constraint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.ToIntFunction;

import org.xbib.datastructures.validation.constraint.base.ContainerConstraintBase;
import org.xbib.datastructures.validation.core.ConstraintPredicate;
import org.xbib.datastructures.validation.core.ViolatedValue;

import static org.xbib.datastructures.validation.core.NullAs.VALID;
import static org.xbib.datastructures.validation.core.ViolationMessage.Default.COLLECTION_CONTAINS;
import static org.xbib.datastructures.validation.core.ViolationMessage.Default.COLLECTION_UNIQUE;

public class CollectionConstraint<T, L extends Collection<E>, E>
		extends ContainerConstraintBase<T, L, CollectionConstraint<T, L, E>> {

	@Override
	public CollectionConstraint<T, L, E> cast() {
		return this;
	}

	public CollectionConstraint<T, L, E> contains(E s) {
		this.predicates().add(ConstraintPredicate.of(x -> x.contains(s),
				COLLECTION_CONTAINS, () -> new Object[] { s }, VALID));
		return this;
	}

	/**
	 * @since 0.8.3
	 */
	public CollectionConstraint<T, L, E> unique() {
		this.predicates().add(ConstraintPredicate.withViolatedValue(collection -> {
			final List<E> duplicates = new ArrayList<>();
			final Set<E> uniqElements = new LinkedHashSet<>(collection.size());
			for (E element : collection) {
				if (uniqElements.contains(element)) {
					duplicates.add(element);
				}
				else {
					uniqElements.add(element);
				}
			}
			if (duplicates.isEmpty()) {
				return Optional.empty();
			}
			else {
				return Optional.of(new ViolatedValue(duplicates));
			}
		}, COLLECTION_UNIQUE, () -> new Object[] {}, VALID));
		return this;
	}

	@Override
	protected ToIntFunction<L> size() {
		return Collection::size;
	}
}
