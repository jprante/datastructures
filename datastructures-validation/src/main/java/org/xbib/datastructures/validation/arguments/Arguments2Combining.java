package org.xbib.datastructures.validation.arguments;

import org.xbib.datastructures.validation.core.ValueValidator;
import org.xbib.datastructures.validation.fn.Function2;
import org.xbib.datastructures.validation.fn.Validations;

public class Arguments2Combining<A, R1, R2> {
	protected final ValueValidator<? super A, ? extends R1> v1;

	protected final ValueValidator<? super A, ? extends R2> v2;

	public Arguments2Combining(ValueValidator<? super A, ? extends R1> v1,
			ValueValidator<? super A, ? extends R2> v2) {
		this.v1 = v1;
		this.v2 = v2;
	}

	public <X> Arguments1Validator<A, X> apply(
			Function2<? super R1, ? super R2, ? extends X> f) {
		return (a, locale, constraintContext) -> Validations.apply(f::apply,
				this.v1.validate(a, locale, constraintContext),
				this.v2.validate(a, locale, constraintContext));
	}

	public <R3> Arguments3Combining<A, R1, R2, R3> combine(
			ValueValidator<? super A, ? extends R3> v3) {
		return new Arguments3Combining<>(v1, v2, v3);
	}
}
