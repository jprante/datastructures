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
package org.xbib.datastructures.validation.arguments;

import java.util.Locale;
import java.util.function.Supplier;

import org.xbib.datastructures.validation.core.ConstraintContext;
import org.xbib.datastructures.validation.core.Validated;
import org.xbib.datastructures.validation.core.Validator;
import org.xbib.datastructures.validation.fn.Function7;
import org.xbib.datastructures.validation.jsr305.Nullable;

/**
 * Generated by https://github.com/making/yavi/blob/develop/scripts/generate-args.sh
 *
 * @since 0.7.0
 */
public class DefaultArguments7Validator<A1, A2, A3, A4, A5, A6, A7, X>
		implements Arguments7Validator<A1, A2, A3, A4, A5, A6, A7, X> {
	protected final Validator<Arguments7<A1, A2, A3, A4, A5, A6, A7>> validator;
	protected final Function7<? super A1, ? super A2, ? super A3, ? super A4, ? super A5, ? super A6, ? super A7, ? extends X> mapper;

	public DefaultArguments7Validator(
			Validator<Arguments7<A1, A2, A3, A4, A5, A6, A7>> validator,
			Function7<? super A1, ? super A2, ? super A3, ? super A4, ? super A5, ? super A6, ? super A7, ? extends X> mapper) {
		this.validator = validator;
		this.mapper = mapper;
	}

	/**
	 * @since 0.10.0
	 */
	@Override
	public DefaultArguments7Validator<A1, A2, A3, A4, A5, A6, A7, Supplier<X>> lazy() {
		return new DefaultArguments7Validator<>(this.validator, (a1, a2, a3, a4, a5, a6,
				a7) -> () -> this.mapper.apply(a1, a2, a3, a4, a5, a6, a7));
	}

	@Override
	public Validated<X> validate(@Nullable A1 a1, @Nullable A2 a2, @Nullable A3 a3,
			@Nullable A4 a4, @Nullable A5 a5, @Nullable A6 a6, @Nullable A7 a7,
			Locale locale, ConstraintContext constraintContext) {
		return this.validator.applicative()
				.validate(Arguments.of(a1, a2, a3, a4, a5, a6, a7), locale,
						constraintContext)
				.map(values -> values.map(this.mapper));
	}
}
