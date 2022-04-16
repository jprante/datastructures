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

import java.time.OffsetDateTime;
import java.util.function.Function;

import org.xbib.datastructures.validation.core.Validator;
import org.xbib.datastructures.validation.fn.Function1;

/**
 * @since 0.10.0
 */
public class OffsetDateTimeValidator<T>
		extends DefaultArguments1Validator<OffsetDateTime, T> {

	@Override
	public <T2> OffsetDateTimeValidator<T2> andThen(
			Function<? super T, ? extends T2> mapper) {
		return new OffsetDateTimeValidator<>(super.validator,
				s -> mapper.apply(super.mapper.apply(s)));
	}

	public OffsetDateTimeValidator(Validator<Arguments1<OffsetDateTime>> validator,
			Function1<? super OffsetDateTime, ? extends T> mapper) {
		super(validator, mapper);
	}
}
