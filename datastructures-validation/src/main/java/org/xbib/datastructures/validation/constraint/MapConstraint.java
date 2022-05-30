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

import java.util.Map;
import java.util.function.ToIntFunction;

import static org.xbib.datastructures.validation.core.NullAs.VALID;
import static org.xbib.datastructures.validation.core.ViolationMessage.Default.MAP_CONTAINS_KEY;
import static org.xbib.datastructures.validation.core.ViolationMessage.Default.MAP_CONTAINS_VALUE;

import org.xbib.datastructures.validation.constraint.base.ContainerConstraintBase;
import org.xbib.datastructures.validation.core.ConstraintPredicate;

public class MapConstraint<T, K, V>
		extends ContainerConstraintBase<T, Map<K, V>, MapConstraint<T, K, V>> {

	@Override
	public MapConstraint<T, K, V> cast() {
		return this;
	}

	public MapConstraint<T, K, V> containsKey(K k) {
		this.predicates().add(ConstraintPredicate.of(x -> x.containsKey(k),
				MAP_CONTAINS_KEY, () -> new Object[] { k }, VALID));
		return this;
	}

	public MapConstraint<T, K, V> containsValue(V v) {
		this.predicates().add(ConstraintPredicate.of(x -> x.containsValue(v),
				MAP_CONTAINS_VALUE, () -> new Object[] { v }, VALID));
		return this;
	}

	@Override
	protected ToIntFunction<Map<K, V>> size() {
		return Map::size;
	}
}
