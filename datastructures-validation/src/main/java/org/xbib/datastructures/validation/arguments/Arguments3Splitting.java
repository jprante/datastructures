package org.xbib.datastructures.validation.arguments;

import org.xbib.datastructures.validation.core.ValueValidator;
import org.xbib.datastructures.validation.fn.Function3;
import org.xbib.datastructures.validation.fn.Validations;

public class Arguments3Splitting<A1, A2, A3, R1, R2, R3> {
	protected final ValueValidator<? super A1, ? extends R1> v1;

	protected final ValueValidator<? super A2, ? extends R2> v2;

	protected final ValueValidator<? super A3, ? extends R3> v3;

	public Arguments3Splitting(ValueValidator<? super A1, ? extends R1> v1,
			ValueValidator<? super A2, ? extends R2> v2,
			ValueValidator<? super A3, ? extends R3> v3) {
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
	}

	public <X> Arguments3Validator<A1, A2, A3, X> apply(
			Function3<? super R1, ? super R2, ? super R3, ? extends X> f) {
		return (a1, a2, a3, locale, constraintContext) -> Validations.apply(f::apply,
				this.v1.validate(a1, locale, constraintContext),
				this.v2.validate(a2, locale, constraintContext),
				this.v3.validate(a3, locale, constraintContext));
	}

	public <A4, R4> Arguments4Splitting<A1, A2, A3, A4, R1, R2, R3, R4> split(
			ValueValidator<? super A4, ? extends R4> v4) {
		return new Arguments4Splitting<>(v1, v2, v3, v4);
	}
}
