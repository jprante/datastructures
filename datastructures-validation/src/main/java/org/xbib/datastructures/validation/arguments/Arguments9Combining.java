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

import org.xbib.datastructures.validation.core.ValueValidator;
import org.xbib.datastructures.validation.fn.Function9;
import org.xbib.datastructures.validation.fn.Validations;

/**
 * Generated by https://github.com/making/yavi/blob/develop/scripts/generate-args.sh
 *
 * @since 0.7.0
 */
public class Arguments9Combining<A, R1, R2, R3, R4, R5, R6, R7, R8, R9> {
	protected final ValueValidator<? super A, ? extends R1> v1;

	protected final ValueValidator<? super A, ? extends R2> v2;

	protected final ValueValidator<? super A, ? extends R3> v3;

	protected final ValueValidator<? super A, ? extends R4> v4;

	protected final ValueValidator<? super A, ? extends R5> v5;

	protected final ValueValidator<? super A, ? extends R6> v6;

	protected final ValueValidator<? super A, ? extends R7> v7;

	protected final ValueValidator<? super A, ? extends R8> v8;

	protected final ValueValidator<? super A, ? extends R9> v9;

	public Arguments9Combining(ValueValidator<? super A, ? extends R1> v1,
			ValueValidator<? super A, ? extends R2> v2,
			ValueValidator<? super A, ? extends R3> v3,
			ValueValidator<? super A, ? extends R4> v4,
			ValueValidator<? super A, ? extends R5> v5,
			ValueValidator<? super A, ? extends R6> v6,
			ValueValidator<? super A, ? extends R7> v7,
			ValueValidator<? super A, ? extends R8> v8,
			ValueValidator<? super A, ? extends R9> v9) {
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
		this.v4 = v4;
		this.v5 = v5;
		this.v6 = v6;
		this.v7 = v7;
		this.v8 = v8;
		this.v9 = v9;
	}

	public <X> Arguments1Validator<A, X> apply(
			Function9<? super R1, ? super R2, ? super R3, ? super R4, ? super R5, ? super R6, ? super R7, ? super R8, ? super R9, ? extends X> f) {
		return (a, locale, constraintContext) -> Validations.apply(f::apply,
				this.v1.validate(a, locale, constraintContext),
				this.v2.validate(a, locale, constraintContext),
				this.v3.validate(a, locale, constraintContext),
				this.v4.validate(a, locale, constraintContext),
				this.v5.validate(a, locale, constraintContext),
				this.v6.validate(a, locale, constraintContext),
				this.v7.validate(a, locale, constraintContext),
				this.v8.validate(a, locale, constraintContext),
				this.v9.validate(a, locale, constraintContext));
	}

	public <R10> Arguments10Combining<A, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10> combine(
			ValueValidator<? super A, ? extends R10> v10) {
		return new Arguments10Combining<>(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10);
	}
}
