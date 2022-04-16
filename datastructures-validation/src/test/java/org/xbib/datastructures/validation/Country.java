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
package org.xbib.datastructures.validation;

import org.xbib.datastructures.validation.builder.ValidatorBuilder;
import org.xbib.datastructures.validation.core.ApplicativeValidator;
import org.xbib.datastructures.validation.core.Validated;
import org.xbib.datastructures.validation.core.Validator;

public class Country {
	private final String name;

	public Country(String name) {
		this.name = name;
	}

	static final ApplicativeValidator<Country> applicativeValidator = validator()
			.prefixed("country").applicative();

	public static Validator<Country> validator() {
		return ValidatorBuilder.<Country> of()
				.constraint(Country::name, "name", c -> c.notBlank() //
						.greaterThanOrEqual(2))
				.build();
	}

	public String name() {
		return this.name;
	}

	public static Validated<Country> of(String name) {
		return applicativeValidator.validate(new Country(name));
	}
}
