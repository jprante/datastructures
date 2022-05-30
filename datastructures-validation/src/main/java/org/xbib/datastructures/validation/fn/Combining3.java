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
package org.xbib.datastructures.validation.fn;

/**
 * Generated by
 * https://github.com/making/yavi/blob/develop/scripts/generate-applicative.sh
 *
 * @since 0.6.0
 */
public class Combining3<E, T1, T2, T3> {
	protected final Validation<E, T1> v1;

	protected final Validation<E, T2> v2;

	protected final Validation<E, T3> v3;

	public Combining3(Validation<E, T1> v1, Validation<E, T2> v2, Validation<E, T3> v3) {
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
	}

	public <R, V extends Validation<E, R>> V apply(Function3<T1, T2, T3, R> f) {
		final Validation<E, Function1<T2, Function1<T3, R>>> apply1 = v1
				.apply(Validation.success(Functions.curry(f)));
		final Validation<E, Function1<T3, R>> apply2 = v2.apply(apply1);
		return v3.apply(apply2);
	}

	public <T4> Combining4<E, T1, T2, T3, T4> combine(Validation<E, T4> v4) {
		return new Combining4<>(v1, v2, v3, v4);
	}
}
