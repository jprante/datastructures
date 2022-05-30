package org.xbib.datastructures.validation.arguments;

import org.xbib.datastructures.validation.core.ValueValidator;
import org.xbib.datastructures.validation.fn.Function3;
import org.xbib.datastructures.validation.fn.Validations;

public class Arguments3Combining<A, R1, R2, R3> {
	protected final ValueValidator<? super A, ? extends R1> v1;

	protected final ValueValidator<? super A, ? extends R2> v2;

	protected final ValueValidator<? super A, ? extends R3> v3;

	public Arguments3Combining(ValueValidator<? super A, ? extends R1> v1,
			ValueValidator<? super A, ? extends R2> v2,
			ValueValidator<? super A, ? extends R3> v3) {
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
	}

	public <X> Arguments1Validator<A, X> apply(
			Function3<? super R1, ? super R2, ? super R3, ? extends X> f) {
		return (a, locale, constraintContext) -> Validations.apply(f::apply,
				this.v1.validate(a, locale, constraintContext),
				this.v2.validate(a, locale, constraintContext),
				this.v3.validate(a, locale, constraintContext));
	}

	public <R4> Arguments4Combining<A, R1, R2, R3, R4> combine(
			ValueValidator<? super A, ? extends R4> v4) {
		return new Arguments4Combining<>(v1, v2, v3, v4);
	}
}
