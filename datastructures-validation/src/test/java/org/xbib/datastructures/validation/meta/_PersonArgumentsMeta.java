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

// Generated at 2020-02-10T11:56:30.484098+09:00
public class _PersonArgumentsMeta {

	public static final org.xbib.datastructures.validation.meta.StringConstraintMeta<org.xbib.datastructures.validation.arguments.Arguments3<String, String, Integer>> FIRSTNAME = new org.xbib.datastructures.validation.meta.StringConstraintMeta<org.xbib.datastructures.validation.arguments.Arguments3<String, String, Integer>>() {

		@Override
		public String name() {
			return "firstName";
		}

		@Override
		public java.util.function.Function<org.xbib.datastructures.validation.arguments.Arguments3<String, String, Integer>, String> toValue() {
			return org.xbib.datastructures.validation.arguments.Arguments1::arg1;
		}
	};

	public static final org.xbib.datastructures.validation.meta.StringConstraintMeta<org.xbib.datastructures.validation.arguments.Arguments3<String, String, Integer>> LASTNAME = new org.xbib.datastructures.validation.meta.StringConstraintMeta<org.xbib.datastructures.validation.arguments.Arguments3<String, String, Integer>>() {

		@Override
		public String name() {
			return "lastName";
		}

		@Override
		public java.util.function.Function<org.xbib.datastructures.validation.arguments.Arguments3<String, String, Integer>, String> toValue() {
			return org.xbib.datastructures.validation.arguments.Arguments2::arg2;
		}
	};

	public static final org.xbib.datastructures.validation.meta.IntegerConstraintMeta<org.xbib.datastructures.validation.arguments.Arguments3<String, String, Integer>> AGE = new org.xbib.datastructures.validation.meta.IntegerConstraintMeta<org.xbib.datastructures.validation.arguments.Arguments3<String, String, Integer>>() {

		@Override
		public String name() {
			return "age";
		}

		@Override
		public java.util.function.Function<org.xbib.datastructures.validation.arguments.Arguments3<String, String, Integer>, Integer> toValue() {
			return org.xbib.datastructures.validation.arguments.Arguments3::arg3;
		}
	};
}
