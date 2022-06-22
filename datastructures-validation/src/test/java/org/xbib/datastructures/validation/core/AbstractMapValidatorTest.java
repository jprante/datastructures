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
package org.xbib.datastructures.validation.core;

import java.util.LinkedHashMap;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import org.xbib.datastructures.validation.Address;
import org.xbib.datastructures.validation.Country;
import org.xbib.datastructures.validation.FormWithMap;
import org.xbib.datastructures.validation.PhoneNumber;

abstract class AbstractMapValidatorTest {
	@Test
	void allInvalid() throws Exception {
		Validator<FormWithMap> validator = validator();
		FormWithMap form = new FormWithMap(new LinkedHashMap<String, Address>() {
			{
				put("tokyo", new Address(new Country(null), null, new PhoneNumber("")));
				put("osaka", new Address(new Country(null), null, new PhoneNumber("")));
			}
		});
		ConstraintViolations violations = validator.validate(form);
		assertThat(violations.isValid()).isFalse();
		assertThat(violations.size()).isEqualTo(8);
		assertThat(violations.get(0).message())
				.isEqualTo("\"addresses[0].street\" must not be blank");
		assertThat(violations.get(0).messageKey()).isEqualTo("charSequence.notBlank");
		assertThat(violations.get(1).message())
				.isEqualTo("\"addresses[0].country.name\" must not be blank");
		assertThat(violations.get(1).messageKey()).isEqualTo("charSequence.notBlank");
		assertThat(violations.get(2).message())
				.isEqualTo("\"addresses[0].phoneNumber.value\" must not be blank");
		assertThat(violations.get(2).messageKey()).isEqualTo("charSequence.notBlank");
		assertThat(violations.get(3).message()).isEqualTo(
				"The size of \"addresses[0].phoneNumber.value\" must be greater than or equal to 8. The given size is 0");
		assertThat(violations.get(3).messageKey())
				.isEqualTo("container.greaterThanOrEqual");
		assertThat(violations.get(4).message())
				.isEqualTo("\"addresses[1].street\" must not be blank");
		assertThat(violations.get(4).messageKey()).isEqualTo("charSequence.notBlank");
		assertThat(violations.get(5).message())
				.isEqualTo("\"addresses[1].country.name\" must not be blank");
		assertThat(violations.get(5).messageKey()).isEqualTo("charSequence.notBlank");
		assertThat(violations.get(6).message())
				.isEqualTo("\"addresses[1].phoneNumber.value\" must not be blank");
		assertThat(violations.get(6).messageKey()).isEqualTo("charSequence.notBlank");
		assertThat(violations.get(7).message()).isEqualTo(
				"The size of \"addresses[1].phoneNumber.value\" must be greater than or equal to 8. The given size is 0");
		assertThat(violations.get(7).messageKey())
				.isEqualTo("container.greaterThanOrEqual");
	}

	@Test
	void inValidOne() throws Exception {
		Validator<FormWithMap> validator = validator();
		FormWithMap form = new FormWithMap(new LinkedHashMap<String, Address>() {
			{
				put("tokyo", new Address(new Country("JP"), "tokyo",
						new PhoneNumber("0123456789")));
				put("osaka", new Address(new Country(null), null, new PhoneNumber("")));
			}
		});
		ConstraintViolations violations = validator.validate(form);
		assertThat(violations.isValid()).isFalse();
		assertThat(violations.size()).isEqualTo(4);
		assertThat(violations.get(0).message())
				.isEqualTo("\"addresses[1].street\" must not be blank");
		assertThat(violations.get(0).messageKey()).isEqualTo("charSequence.notBlank");
		assertThat(violations.get(1).message())
				.isEqualTo("\"addresses[1].country.name\" must not be blank");
		assertThat(violations.get(1).messageKey()).isEqualTo("charSequence.notBlank");
		assertThat(violations.get(2).message())
				.isEqualTo("\"addresses[1].phoneNumber.value\" must not be blank");
		assertThat(violations.get(2).messageKey()).isEqualTo("charSequence.notBlank");
		assertThat(violations.get(3).message()).isEqualTo(
				"The size of \"addresses[1].phoneNumber.value\" must be greater than or equal to 8. The given size is 0");
		assertThat(violations.get(3).messageKey())
				.isEqualTo("container.greaterThanOrEqual");
	}

	@Test
	void nullCollectionInValid() throws Exception {
		Validator<FormWithMap> validator = validator();

		FormWithMap form = new FormWithMap(null);
		ConstraintViolations violations = validator.validate(form);
		assertThat(violations.isValid()).isFalse();
		assertThat(violations.size()).isEqualTo(1);
		assertThat(violations.get(0).message())
				.isEqualTo("\"addresses\" must not be null");
		assertThat(violations.get(0).messageKey()).isEqualTo("object.notNull");
	}

	@Test
	void nullElement() throws Exception {
		Validator<FormWithMap> validator = validator();
		FormWithMap form = new FormWithMap(new LinkedHashMap<String, Address>() {
			{
				put("tokyo", new Address(new Country("JP"), "tokyo",
						new PhoneNumber("0123456789")));
				put("osaka", null);
			}
		});
		// FIXME: does not check null element
		ConstraintViolations violations = validator.validate(form);
		assertThat(violations.isValid()).isTrue();
	}

	@Test
	void valid() throws Exception {
		Validator<FormWithMap> validator = validator();
		FormWithMap form = new FormWithMap(new LinkedHashMap<String, Address>() {
			{
				put("tokyo", new Address(new Country("JP"), "tokyo",
						new PhoneNumber("0123456789")));
				put("osaka", new Address(new Country("JP"), "osaka",
						new PhoneNumber("0123456788")));
			}
		});
		ConstraintViolations violations = validator.validate(form);
		assertThat(violations.isValid()).isTrue();
	}

	protected abstract Validator<FormWithMap> validator();
}