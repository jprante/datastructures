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
package test;

public class _UserServiceCreateUserArgumentsMeta {

	public static final org.xbib.datastructures.validation.meta.ObjectConstraintMeta<org.xbib.datastructures.validation.arguments.Arguments3<test.UserService, java.lang.String, java.lang.String>, test.UserService> USERSERVICE = new org.xbib.datastructures.validation.meta.ObjectConstraintMeta<org.xbib.datastructures.validation.arguments.Arguments3<test.UserService, java.lang.String, java.lang.String>, test.UserService>() {

		@Override
		public String name() {
			return "userService";
		}

		@Override
		public java.util.function.Function<org.xbib.datastructures.validation.arguments.Arguments3<test.UserService, java.lang.String, java.lang.String>, test.UserService> toValue() {
			return org.xbib.datastructures.validation.arguments.Arguments1::arg1;
		}
	};

	public static final org.xbib.datastructures.validation.meta.StringConstraintMeta<org.xbib.datastructures.validation.arguments.Arguments3<test.UserService, java.lang.String, java.lang.String>> EMAIL = new org.xbib.datastructures.validation.meta.StringConstraintMeta<org.xbib.datastructures.validation.arguments.Arguments3<test.UserService, java.lang.String, java.lang.String>>() {

		@Override
		public String name() {
			return "email";
		}

		@Override
		public java.util.function.Function<org.xbib.datastructures.validation.arguments.Arguments3<test.UserService, java.lang.String, java.lang.String>, java.lang.String> toValue() {
			return org.xbib.datastructures.validation.arguments.Arguments2::arg2;
		}
	};

	public static final org.xbib.datastructures.validation.meta.StringConstraintMeta<org.xbib.datastructures.validation.arguments.Arguments3<test.UserService, java.lang.String, java.lang.String>> NAME = new org.xbib.datastructures.validation.meta.StringConstraintMeta<org.xbib.datastructures.validation.arguments.Arguments3<test.UserService, java.lang.String, java.lang.String>>() {

		@Override
		public String name() {
			return "name";
		}

		@Override
		public java.util.function.Function<org.xbib.datastructures.validation.arguments.Arguments3<test.UserService, java.lang.String, java.lang.String>, java.lang.String> toValue() {
			return org.xbib.datastructures.validation.arguments.Arguments3::arg3;
		}
	};
}