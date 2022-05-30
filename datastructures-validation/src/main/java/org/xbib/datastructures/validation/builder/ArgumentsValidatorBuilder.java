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
package org.xbib.datastructures.validation.builder;

import org.xbib.datastructures.validation.arguments.Arguments1;
import org.xbib.datastructures.validation.arguments.Arguments1Validator;
import org.xbib.datastructures.validation.arguments.Arguments2;
import org.xbib.datastructures.validation.arguments.Arguments2Validator;
import org.xbib.datastructures.validation.arguments.Arguments3;
import org.xbib.datastructures.validation.arguments.Arguments3Validator;
import org.xbib.datastructures.validation.arguments.Arguments4;
import org.xbib.datastructures.validation.arguments.Arguments4Validator;
import org.xbib.datastructures.validation.arguments.Arguments5;
import org.xbib.datastructures.validation.arguments.Arguments5Validator;
import org.xbib.datastructures.validation.arguments.Arguments6;
import org.xbib.datastructures.validation.arguments.Arguments6Validator;
import org.xbib.datastructures.validation.arguments.Arguments7;
import org.xbib.datastructures.validation.arguments.Arguments7Validator;
import org.xbib.datastructures.validation.arguments.Arguments8;
import org.xbib.datastructures.validation.arguments.Arguments8Validator;
import org.xbib.datastructures.validation.arguments.Arguments9;
import org.xbib.datastructures.validation.arguments.Arguments9Validator;
import org.xbib.datastructures.validation.arguments.Arguments10;
import org.xbib.datastructures.validation.arguments.Arguments10Validator;
import org.xbib.datastructures.validation.arguments.Arguments11;
import org.xbib.datastructures.validation.arguments.Arguments11Validator;
import org.xbib.datastructures.validation.arguments.Arguments12;
import org.xbib.datastructures.validation.arguments.Arguments12Validator;
import org.xbib.datastructures.validation.arguments.Arguments13;
import org.xbib.datastructures.validation.arguments.Arguments13Validator;
import org.xbib.datastructures.validation.arguments.Arguments14;
import org.xbib.datastructures.validation.arguments.Arguments14Validator;
import org.xbib.datastructures.validation.arguments.Arguments15;
import org.xbib.datastructures.validation.arguments.Arguments15Validator;
import org.xbib.datastructures.validation.arguments.Arguments16;
import org.xbib.datastructures.validation.arguments.Arguments16Validator;
import org.xbib.datastructures.validation.arguments.DefaultArguments1Validator;
import org.xbib.datastructures.validation.arguments.DefaultArguments2Validator;
import org.xbib.datastructures.validation.arguments.DefaultArguments3Validator;
import org.xbib.datastructures.validation.arguments.DefaultArguments4Validator;
import org.xbib.datastructures.validation.arguments.DefaultArguments5Validator;
import org.xbib.datastructures.validation.arguments.DefaultArguments6Validator;
import org.xbib.datastructures.validation.arguments.DefaultArguments7Validator;
import org.xbib.datastructures.validation.arguments.DefaultArguments8Validator;
import org.xbib.datastructures.validation.arguments.DefaultArguments9Validator;
import org.xbib.datastructures.validation.arguments.DefaultArguments10Validator;
import org.xbib.datastructures.validation.arguments.DefaultArguments11Validator;
import org.xbib.datastructures.validation.arguments.DefaultArguments12Validator;
import org.xbib.datastructures.validation.arguments.DefaultArguments13Validator;
import org.xbib.datastructures.validation.arguments.DefaultArguments14Validator;
import org.xbib.datastructures.validation.arguments.DefaultArguments15Validator;
import org.xbib.datastructures.validation.arguments.DefaultArguments16Validator;
import org.xbib.datastructures.validation.fn.Function1;
import org.xbib.datastructures.validation.fn.Function2;
import org.xbib.datastructures.validation.fn.Function3;
import org.xbib.datastructures.validation.fn.Function4;
import org.xbib.datastructures.validation.fn.Function5;
import org.xbib.datastructures.validation.fn.Function6;
import org.xbib.datastructures.validation.fn.Function7;
import org.xbib.datastructures.validation.fn.Function8;
import org.xbib.datastructures.validation.fn.Function9;
import org.xbib.datastructures.validation.fn.Function10;
import org.xbib.datastructures.validation.fn.Function11;
import org.xbib.datastructures.validation.fn.Function12;
import org.xbib.datastructures.validation.fn.Function13;
import org.xbib.datastructures.validation.fn.Function14;
import org.xbib.datastructures.validation.fn.Function15;
import org.xbib.datastructures.validation.fn.Function16;

import java.util.Objects;
import java.util.function.Function;

/**
 * Generated by https://github.com/making/yavi/blob/develop/scripts/generate-args.sh
 *
 * @since 0.3.0
 */
public final class ArgumentsValidatorBuilder {
	public static <A1, X> Arguments1ValidatorBuilder<A1, X> of(
			Function1<? super A1, ? extends X> mapper) {
		return new Arguments1ValidatorBuilder<>(mapper);
	}

	public static <A1, A2, X> Arguments2ValidatorBuilder<A1, A2, X> of(
			Function2<? super A1, ? super A2, ? extends X> mapper) {
		return new Arguments2ValidatorBuilder<>(mapper);
	}

	public static <A1, A2, A3, X> Arguments3ValidatorBuilder<A1, A2, A3, X> of(
			Function3<? super A1, ? super A2, ? super A3, ? extends X> mapper) {
		return new Arguments3ValidatorBuilder<>(mapper);
	}

	public static <A1, A2, A3, A4, X> Arguments4ValidatorBuilder<A1, A2, A3, A4, X> of(
			Function4<? super A1, ? super A2, ? super A3, ? super A4, ? extends X> mapper) {
		return new Arguments4ValidatorBuilder<>(mapper);
	}

	public static <A1, A2, A3, A4, A5, X> Arguments5ValidatorBuilder<A1, A2, A3, A4, A5, X> of(
			Function5<? super A1, ? super A2, ? super A3, ? super A4, ? super A5, ? extends X> mapper) {
		return new Arguments5ValidatorBuilder<>(mapper);
	}

	public static <A1, A2, A3, A4, A5, A6, X> Arguments6ValidatorBuilder<A1, A2, A3, A4, A5, A6, X> of(
			Function6<? super A1, ? super A2, ? super A3, ? super A4, ? super A5, ? super A6, ? extends X> mapper) {
		return new Arguments6ValidatorBuilder<>(mapper);
	}

	public static <A1, A2, A3, A4, A5, A6, A7, X> Arguments7ValidatorBuilder<A1, A2, A3, A4, A5, A6, A7, X> of(
			Function7<? super A1, ? super A2, ? super A3, ? super A4, ? super A5, ? super A6, ? super A7, ? extends X> mapper) {
		return new Arguments7ValidatorBuilder<>(mapper);
	}

	public static <A1, A2, A3, A4, A5, A6, A7, A8, X> Arguments8ValidatorBuilder<A1, A2, A3, A4, A5, A6, A7, A8, X> of(
			Function8<? super A1, ? super A2, ? super A3, ? super A4, ? super A5, ? super A6, ? super A7, ? super A8, ? extends X> mapper) {
		return new Arguments8ValidatorBuilder<>(mapper);
	}

	public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, X> Arguments9ValidatorBuilder<A1, A2, A3, A4, A5, A6, A7, A8, A9, X> of(
			Function9<? super A1, ? super A2, ? super A3, ? super A4, ? super A5, ? super A6, ? super A7, ? super A8, ? super A9, ? extends X> mapper) {
		return new Arguments9ValidatorBuilder<>(mapper);
	}

	public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, X> Arguments10ValidatorBuilder<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, X> of(
			Function10<? super A1, ? super A2, ? super A3, ? super A4, ? super A5, ? super A6, ? super A7, ? super A8, ? super A9, ? super A10, ? extends X> mapper) {
		return new Arguments10ValidatorBuilder<>(mapper);
	}

	public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, X> Arguments11ValidatorBuilder<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, X> of(
			Function11<? super A1, ? super A2, ? super A3, ? super A4, ? super A5, ? super A6, ? super A7, ? super A8, ? super A9, ? super A10, ? super A11, ? extends X> mapper) {
		return new Arguments11ValidatorBuilder<>(mapper);
	}

	public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, X> Arguments12ValidatorBuilder<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, X> of(
			Function12<? super A1, ? super A2, ? super A3, ? super A4, ? super A5, ? super A6, ? super A7, ? super A8, ? super A9, ? super A10, ? super A11, ? super A12, ? extends X> mapper) {
		return new Arguments12ValidatorBuilder<>(mapper);
	}

	public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, X> Arguments13ValidatorBuilder<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, X> of(
			Function13<? super A1, ? super A2, ? super A3, ? super A4, ? super A5, ? super A6, ? super A7, ? super A8, ? super A9, ? super A10, ? super A11, ? super A12, ? super A13, ? extends X> mapper) {
		return new Arguments13ValidatorBuilder<>(mapper);
	}

	public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, X> Arguments14ValidatorBuilder<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, X> of(
			Function14<? super A1, ? super A2, ? super A3, ? super A4, ? super A5, ? super A6, ? super A7, ? super A8, ? super A9, ? super A10, ? super A11, ? super A12, ? super A13, ? super A14, ? extends X> mapper) {
		return new Arguments14ValidatorBuilder<>(mapper);
	}

	public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, X> Arguments15ValidatorBuilder<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, X> of(
			Function15<? super A1, ? super A2, ? super A3, ? super A4, ? super A5, ? super A6, ? super A7, ? super A8, ? super A9, ? super A10, ? super A11, ? super A12, ? super A13, ? super A14, ? super A15, ? extends X> mapper) {
		return new Arguments15ValidatorBuilder<>(mapper);
	}

	public static <A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, X> Arguments16ValidatorBuilder<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, X> of(
			Function16<? super A1, ? super A2, ? super A3, ? super A4, ? super A5, ? super A6, ? super A7, ? super A8, ? super A9, ? super A10, ? super A11, ? super A12, ? super A13, ? super A14, ? super A15, ? super A16, ? extends X> mapper) {
		return new Arguments16ValidatorBuilder<>(mapper);
	}

	/**
	 * @since 0.3.0
	 */
	public static final class Arguments1ValidatorBuilder<A1, X> {
		private final Function1<? super A1, ? extends X> mapper;
		private ValidatorBuilder<Arguments1<A1>> builder;

		public Arguments1ValidatorBuilder(Function1<? super A1, ? extends X> mapper) {
			this.mapper = Objects.requireNonNull(mapper, "'mapper' must not be null.");
		}

		public Arguments1ValidatorBuilder<A1, X> builder(
				Function<? super ValidatorBuilder<Arguments1<A1>>, ? extends ValidatorBuilder<Arguments1<A1>>> definition) {
			this.builder = definition.apply(ValidatorBuilder.of());
			return this;
		}

		public Arguments1Validator<A1, X> build() {
			return new DefaultArguments1Validator<>(this.builder.build(), this.mapper);
		}
	}

	/**
	 * @since 0.3.0
	 */
	public static final class Arguments2ValidatorBuilder<A1, A2, X> {
		private final Function2<? super A1, ? super A2, ? extends X> mapper;
		private ValidatorBuilder<Arguments2<A1, A2>> builder;

		public Arguments2ValidatorBuilder(
				Function2<? super A1, ? super A2, ? extends X> mapper) {
			this.mapper = Objects.requireNonNull(mapper, "'mapper' must not be null.");
		}

		public Arguments2ValidatorBuilder<A1, A2, X> builder(
				Function<? super ValidatorBuilder<Arguments2<A1, A2>>, ? extends ValidatorBuilder<Arguments2<A1, A2>>> definition) {
			this.builder = definition.apply(ValidatorBuilder.of());
			return this;
		}

		public Arguments2Validator<A1, A2, X> build() {
			return new DefaultArguments2Validator<>(this.builder.build(), this.mapper);
		}
	}

	/**
	 * @since 0.3.0
	 */
	public static final class Arguments3ValidatorBuilder<A1, A2, A3, X> {
		private final Function3<? super A1, ? super A2, ? super A3, ? extends X> mapper;
		private ValidatorBuilder<Arguments3<A1, A2, A3>> builder;

		public Arguments3ValidatorBuilder(
				Function3<? super A1, ? super A2, ? super A3, ? extends X> mapper) {
			this.mapper = Objects.requireNonNull(mapper, "'mapper' must not be null.");
		}

		public Arguments3ValidatorBuilder<A1, A2, A3, X> builder(
				Function<? super ValidatorBuilder<Arguments3<A1, A2, A3>>, ? extends ValidatorBuilder<Arguments3<A1, A2, A3>>> definition) {
			this.builder = definition.apply(ValidatorBuilder.of());
			return this;
		}

		public Arguments3Validator<A1, A2, A3, X> build() {
			return new DefaultArguments3Validator<>(this.builder.build(), this.mapper);
		}
	}

	/**
	 * @since 0.3.0
	 */
	public static final class Arguments4ValidatorBuilder<A1, A2, A3, A4, X> {
		private final Function4<? super A1, ? super A2, ? super A3, ? super A4, ? extends X> mapper;
		private ValidatorBuilder<Arguments4<A1, A2, A3, A4>> builder;

		public Arguments4ValidatorBuilder(
				Function4<? super A1, ? super A2, ? super A3, ? super A4, ? extends X> mapper) {
			this.mapper = Objects.requireNonNull(mapper, "'mapper' must not be null.");
		}

		public Arguments4ValidatorBuilder<A1, A2, A3, A4, X> builder(
				Function<? super ValidatorBuilder<Arguments4<A1, A2, A3, A4>>, ? extends ValidatorBuilder<Arguments4<A1, A2, A3, A4>>> definition) {
			this.builder = definition.apply(ValidatorBuilder.of());
			return this;
		}

		public Arguments4Validator<A1, A2, A3, A4, X> build() {
			return new DefaultArguments4Validator<>(this.builder.build(), this.mapper);
		}
	}

	/**
	 * @since 0.3.0
	 */
	public static final class Arguments5ValidatorBuilder<A1, A2, A3, A4, A5, X> {
		private final Function5<? super A1, ? super A2, ? super A3, ? super A4, ? super A5, ? extends X> mapper;
		private ValidatorBuilder<Arguments5<A1, A2, A3, A4, A5>> builder;

		public Arguments5ValidatorBuilder(
				Function5<? super A1, ? super A2, ? super A3, ? super A4, ? super A5, ? extends X> mapper) {
			this.mapper = Objects.requireNonNull(mapper, "'mapper' must not be null.");
		}

		public Arguments5ValidatorBuilder<A1, A2, A3, A4, A5, X> builder(
				Function<? super ValidatorBuilder<Arguments5<A1, A2, A3, A4, A5>>, ? extends ValidatorBuilder<Arguments5<A1, A2, A3, A4, A5>>> definition) {
			this.builder = definition.apply(ValidatorBuilder.of());
			return this;
		}

		public Arguments5Validator<A1, A2, A3, A4, A5, X> build() {
			return new DefaultArguments5Validator<>(this.builder.build(), this.mapper);
		}
	}

	/**
	 * @since 0.3.0
	 */
	public static final class Arguments6ValidatorBuilder<A1, A2, A3, A4, A5, A6, X> {
		private final Function6<? super A1, ? super A2, ? super A3, ? super A4, ? super A5, ? super A6, ? extends X> mapper;
		private ValidatorBuilder<Arguments6<A1, A2, A3, A4, A5, A6>> builder;

		public Arguments6ValidatorBuilder(
				Function6<? super A1, ? super A2, ? super A3, ? super A4, ? super A5, ? super A6, ? extends X> mapper) {
			this.mapper = Objects.requireNonNull(mapper, "'mapper' must not be null.");
		}

		public Arguments6ValidatorBuilder<A1, A2, A3, A4, A5, A6, X> builder(
				Function<? super ValidatorBuilder<Arguments6<A1, A2, A3, A4, A5, A6>>, ? extends ValidatorBuilder<Arguments6<A1, A2, A3, A4, A5, A6>>> definition) {
			this.builder = definition.apply(ValidatorBuilder.of());
			return this;
		}

		public Arguments6Validator<A1, A2, A3, A4, A5, A6, X> build() {
			return new DefaultArguments6Validator<>(this.builder.build(), this.mapper);
		}
	}

	/**
	 * @since 0.3.0
	 */
	public static final class Arguments7ValidatorBuilder<A1, A2, A3, A4, A5, A6, A7, X> {
		private final Function7<? super A1, ? super A2, ? super A3, ? super A4, ? super A5, ? super A6, ? super A7, ? extends X> mapper;
		private ValidatorBuilder<Arguments7<A1, A2, A3, A4, A5, A6, A7>> builder;

		public Arguments7ValidatorBuilder(
				Function7<? super A1, ? super A2, ? super A3, ? super A4, ? super A5, ? super A6, ? super A7, ? extends X> mapper) {
			this.mapper = Objects.requireNonNull(mapper, "'mapper' must not be null.");
		}

		public Arguments7ValidatorBuilder<A1, A2, A3, A4, A5, A6, A7, X> builder(
				Function<? super ValidatorBuilder<Arguments7<A1, A2, A3, A4, A5, A6, A7>>, ? extends ValidatorBuilder<Arguments7<A1, A2, A3, A4, A5, A6, A7>>> definition) {
			this.builder = definition.apply(ValidatorBuilder.of());
			return this;
		}

		public Arguments7Validator<A1, A2, A3, A4, A5, A6, A7, X> build() {
			return new DefaultArguments7Validator<>(this.builder.build(), this.mapper);
		}
	}

	/**
	 * @since 0.3.0
	 */
	public static final class Arguments8ValidatorBuilder<A1, A2, A3, A4, A5, A6, A7, A8, X> {
		private final Function8<? super A1, ? super A2, ? super A3, ? super A4, ? super A5, ? super A6, ? super A7, ? super A8, ? extends X> mapper;
		private ValidatorBuilder<Arguments8<A1, A2, A3, A4, A5, A6, A7, A8>> builder;

		public Arguments8ValidatorBuilder(
				Function8<? super A1, ? super A2, ? super A3, ? super A4, ? super A5, ? super A6, ? super A7, ? super A8, ? extends X> mapper) {
			this.mapper = Objects.requireNonNull(mapper, "'mapper' must not be null.");
		}

		public Arguments8ValidatorBuilder<A1, A2, A3, A4, A5, A6, A7, A8, X> builder(
				Function<? super ValidatorBuilder<Arguments8<A1, A2, A3, A4, A5, A6, A7, A8>>, ? extends ValidatorBuilder<Arguments8<A1, A2, A3, A4, A5, A6, A7, A8>>> definition) {
			this.builder = definition.apply(ValidatorBuilder.of());
			return this;
		}

		public Arguments8Validator<A1, A2, A3, A4, A5, A6, A7, A8, X> build() {
			return new DefaultArguments8Validator<>(this.builder.build(), this.mapper);
		}
	}

	/**
	 * @since 0.3.0
	 */
	public static final class Arguments9ValidatorBuilder<A1, A2, A3, A4, A5, A6, A7, A8, A9, X> {
		private final Function9<? super A1, ? super A2, ? super A3, ? super A4, ? super A5, ? super A6, ? super A7, ? super A8, ? super A9, ? extends X> mapper;
		private ValidatorBuilder<Arguments9<A1, A2, A3, A4, A5, A6, A7, A8, A9>> builder;

		public Arguments9ValidatorBuilder(
				Function9<? super A1, ? super A2, ? super A3, ? super A4, ? super A5, ? super A6, ? super A7, ? super A8, ? super A9, ? extends X> mapper) {
			this.mapper = Objects.requireNonNull(mapper, "'mapper' must not be null.");
		}

		public Arguments9ValidatorBuilder<A1, A2, A3, A4, A5, A6, A7, A8, A9, X> builder(
				Function<? super ValidatorBuilder<Arguments9<A1, A2, A3, A4, A5, A6, A7, A8, A9>>, ? extends ValidatorBuilder<Arguments9<A1, A2, A3, A4, A5, A6, A7, A8, A9>>> definition) {
			this.builder = definition.apply(ValidatorBuilder.of());
			return this;
		}

		public Arguments9Validator<A1, A2, A3, A4, A5, A6, A7, A8, A9, X> build() {
			return new DefaultArguments9Validator<>(this.builder.build(), this.mapper);
		}
	}

	/**
	 * @since 0.3.0
	 */
	public static final class Arguments10ValidatorBuilder<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, X> {
		private final Function10<? super A1, ? super A2, ? super A3, ? super A4, ? super A5, ? super A6, ? super A7, ? super A8, ? super A9, ? super A10, ? extends X> mapper;
		private ValidatorBuilder<Arguments10<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10>> builder;

		public Arguments10ValidatorBuilder(
				Function10<? super A1, ? super A2, ? super A3, ? super A4, ? super A5, ? super A6, ? super A7, ? super A8, ? super A9, ? super A10, ? extends X> mapper) {
			this.mapper = Objects.requireNonNull(mapper, "'mapper' must not be null.");
		}

		public Arguments10ValidatorBuilder<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, X> builder(
				Function<? super ValidatorBuilder<Arguments10<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10>>, ? extends ValidatorBuilder<Arguments10<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10>>> definition) {
			this.builder = definition.apply(ValidatorBuilder.of());
			return this;
		}

		public Arguments10Validator<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, X> build() {
			return new DefaultArguments10Validator<>(this.builder.build(), this.mapper);
		}
	}

	/**
	 * @since 0.3.0
	 */
	public static final class Arguments11ValidatorBuilder<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, X> {
		private final Function11<? super A1, ? super A2, ? super A3, ? super A4, ? super A5, ? super A6, ? super A7, ? super A8, ? super A9, ? super A10, ? super A11, ? extends X> mapper;
		private ValidatorBuilder<Arguments11<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11>> builder;

		public Arguments11ValidatorBuilder(
				Function11<? super A1, ? super A2, ? super A3, ? super A4, ? super A5, ? super A6, ? super A7, ? super A8, ? super A9, ? super A10, ? super A11, ? extends X> mapper) {
			this.mapper = Objects.requireNonNull(mapper, "'mapper' must not be null.");
		}

		public Arguments11ValidatorBuilder<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, X> builder(
				Function<? super ValidatorBuilder<Arguments11<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11>>, ? extends ValidatorBuilder<Arguments11<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11>>> definition) {
			this.builder = definition.apply(ValidatorBuilder.of());
			return this;
		}

		public Arguments11Validator<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, X> build() {
			return new DefaultArguments11Validator<>(this.builder.build(), this.mapper);
		}
	}

	/**
	 * @since 0.3.0
	 */
	public static final class Arguments12ValidatorBuilder<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, X> {
		private final Function12<? super A1, ? super A2, ? super A3, ? super A4, ? super A5, ? super A6, ? super A7, ? super A8, ? super A9, ? super A10, ? super A11, ? super A12, ? extends X> mapper;
		private ValidatorBuilder<Arguments12<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12>> builder;

		public Arguments12ValidatorBuilder(
				Function12<? super A1, ? super A2, ? super A3, ? super A4, ? super A5, ? super A6, ? super A7, ? super A8, ? super A9, ? super A10, ? super A11, ? super A12, ? extends X> mapper) {
			this.mapper = Objects.requireNonNull(mapper, "'mapper' must not be null.");
		}

		public Arguments12ValidatorBuilder<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, X> builder(
				Function<? super ValidatorBuilder<Arguments12<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12>>, ? extends ValidatorBuilder<Arguments12<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12>>> definition) {
			this.builder = definition.apply(ValidatorBuilder.of());
			return this;
		}

		public Arguments12Validator<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, X> build() {
			return new DefaultArguments12Validator<>(this.builder.build(), this.mapper);
		}
	}

	/**
	 * @since 0.3.0
	 */
	public static final class Arguments13ValidatorBuilder<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, X> {
		private final Function13<? super A1, ? super A2, ? super A3, ? super A4, ? super A5, ? super A6, ? super A7, ? super A8, ? super A9, ? super A10, ? super A11, ? super A12, ? super A13, ? extends X> mapper;
		private ValidatorBuilder<Arguments13<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13>> builder;

		public Arguments13ValidatorBuilder(
				Function13<? super A1, ? super A2, ? super A3, ? super A4, ? super A5, ? super A6, ? super A7, ? super A8, ? super A9, ? super A10, ? super A11, ? super A12, ? super A13, ? extends X> mapper) {
			this.mapper = Objects.requireNonNull(mapper, "'mapper' must not be null.");
		}

		public Arguments13ValidatorBuilder<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, X> builder(
				Function<? super ValidatorBuilder<Arguments13<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13>>, ? extends ValidatorBuilder<Arguments13<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13>>> definition) {
			this.builder = definition.apply(ValidatorBuilder.of());
			return this;
		}

		public Arguments13Validator<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, X> build() {
			return new DefaultArguments13Validator<>(this.builder.build(), this.mapper);
		}
	}

	/**
	 * @since 0.3.0
	 */
	public static final class Arguments14ValidatorBuilder<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, X> {
		private final Function14<? super A1, ? super A2, ? super A3, ? super A4, ? super A5, ? super A6, ? super A7, ? super A8, ? super A9, ? super A10, ? super A11, ? super A12, ? super A13, ? super A14, ? extends X> mapper;
		private ValidatorBuilder<Arguments14<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14>> builder;

		public Arguments14ValidatorBuilder(
				Function14<? super A1, ? super A2, ? super A3, ? super A4, ? super A5, ? super A6, ? super A7, ? super A8, ? super A9, ? super A10, ? super A11, ? super A12, ? super A13, ? super A14, ? extends X> mapper) {
			this.mapper = Objects.requireNonNull(mapper, "'mapper' must not be null.");
		}

		public Arguments14ValidatorBuilder<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, X> builder(
				Function<? super ValidatorBuilder<Arguments14<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14>>, ? extends ValidatorBuilder<Arguments14<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14>>> definition) {
			this.builder = definition.apply(ValidatorBuilder.of());
			return this;
		}

		public Arguments14Validator<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, X> build() {
			return new DefaultArguments14Validator<>(this.builder.build(), this.mapper);
		}
	}

	/**
	 * @since 0.3.0
	 */
	public static final class Arguments15ValidatorBuilder<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, X> {
		private final Function15<? super A1, ? super A2, ? super A3, ? super A4, ? super A5, ? super A6, ? super A7, ? super A8, ? super A9, ? super A10, ? super A11, ? super A12, ? super A13, ? super A14, ? super A15, ? extends X> mapper;
		private ValidatorBuilder<Arguments15<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15>> builder;

		public Arguments15ValidatorBuilder(
				Function15<? super A1, ? super A2, ? super A3, ? super A4, ? super A5, ? super A6, ? super A7, ? super A8, ? super A9, ? super A10, ? super A11, ? super A12, ? super A13, ? super A14, ? super A15, ? extends X> mapper) {
			this.mapper = Objects.requireNonNull(mapper, "'mapper' must not be null.");
		}

		public Arguments15ValidatorBuilder<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, X> builder(
				Function<? super ValidatorBuilder<Arguments15<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15>>, ? extends ValidatorBuilder<Arguments15<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15>>> definition) {
			this.builder = definition.apply(ValidatorBuilder.of());
			return this;
		}

		public Arguments15Validator<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, X> build() {
			return new DefaultArguments15Validator<>(this.builder.build(), this.mapper);
		}
	}

	/**
	 * @since 0.3.0
	 */
	public static final class Arguments16ValidatorBuilder<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, X> {
		private final Function16<? super A1, ? super A2, ? super A3, ? super A4, ? super A5, ? super A6, ? super A7, ? super A8, ? super A9, ? super A10, ? super A11, ? super A12, ? super A13, ? super A14, ? super A15, ? super A16, ? extends X> mapper;
		private ValidatorBuilder<Arguments16<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16>> builder;

		public Arguments16ValidatorBuilder(
				Function16<? super A1, ? super A2, ? super A3, ? super A4, ? super A5, ? super A6, ? super A7, ? super A8, ? super A9, ? super A10, ? super A11, ? super A12, ? super A13, ? super A14, ? super A15, ? super A16, ? extends X> mapper) {
			this.mapper = Objects.requireNonNull(mapper, "'mapper' must not be null.");
		}

		public Arguments16ValidatorBuilder<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, X> builder(
				Function<? super ValidatorBuilder<Arguments16<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16>>, ? extends ValidatorBuilder<Arguments16<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16>>> definition) {
			this.builder = definition.apply(ValidatorBuilder.of());
			return this;
		}

		public Arguments16Validator<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, X> build() {
			return new DefaultArguments16Validator<>(this.builder.build(), this.mapper);
		}
	}
}
