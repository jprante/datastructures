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
package org.xbib.datastructures.validation.meta;

public class _AddressMeta {

	public static final ObjectConstraintMeta<Address, Address.Country> COUNTRY = new ObjectConstraintMeta<Address, Address.Country>() {

		@Override
		public String name() {
			return "country";
		}

		@Override
		public java.util.function.Function<Address, Address.Country> toValue() {
			return Address::country;
		}
	};

	public static final StringConstraintMeta<Address> STREET = new StringConstraintMeta<Address>() {

		@Override
		public String name() {
			return "street";
		}

		@Override
		public java.util.function.Function<Address, String> toValue() {
			return Address::street;
		}
	};

	public static final ObjectConstraintMeta<Address, Address.PhoneNumber> PHONENUMBER = new ObjectConstraintMeta<Address, Address.PhoneNumber>() {

		@Override
		public String name() {
			return "phoneNumber";
		}

		@Override
		public java.util.function.Function<Address, Address.PhoneNumber> toValue() {
			return Address::phoneNumber;
		}
	};
}
