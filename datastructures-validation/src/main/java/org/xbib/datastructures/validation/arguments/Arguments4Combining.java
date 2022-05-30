package org.xbib.datastructures.validation.arguments;

import org.xbib.datastructures.validation.core.ValueValidator;
import org.xbib.datastructures.validation.fn.Function4;
import org.xbib.datastructures.validation.fn.Validations;

public class Arguments4Combining<A, R1, R2, R3, R4> {
	protected final ValueValidator<? super A, ? extends R1> v1;

	protected final ValueValidator<? super A, ? extends R2> v2;

	protected final ValueValidator<? super A, ? extends R3> v3;

	protected final ValueValidator<? super A, ? extends R4> v4;

	public Arguments4Combining(ValueValidator<? super A, ? extends R1> v1,
			ValueValidator<? super A, ? extends R2> v2,
			ValueValidator<? super A, ? extends R3> v3,
			ValueValidator<? super A, ? extends R4> v4) {
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
		this.v4 = v4;
	}

	public <X> Arguments1Validator<A, X> apply(
			Function4<? super R1, ? super R2, ? super R3, ? super R4, ? extends X> f) {
		return (a, locale, constraintContext) -> Validations.apply(f::apply,
				this.v1.validate(a, locale, constraintContext),
				this.v2.validate(a, locale, constraintContext),
				this.v3.validate(a, locale, constraintContext),
				this.v4.validate(a, locale, constraintContext));
	}

	public <R5> Arguments5Combining<A, R1, R2, R3, R4, R5> combine(
			ValueValidator<? super A, ? extends R5> v5) {
		return new Arguments5Combining<>(v1, v2, v3, v4, v5);
	}
}
