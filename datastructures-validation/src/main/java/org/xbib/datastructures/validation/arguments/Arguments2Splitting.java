package org.xbib.datastructures.validation.arguments;

import org.xbib.datastructures.validation.core.ValueValidator;
import org.xbib.datastructures.validation.fn.Function2;
import org.xbib.datastructures.validation.fn.Validations;

public class Arguments2Splitting<A1, A2, R1, R2> {
	protected final ValueValidator<? super A1, ? extends R1> v1;

	protected final ValueValidator<? super A2, ? extends R2> v2;

	public Arguments2Splitting(ValueValidator<? super A1, ? extends R1> v1,
			ValueValidator<? super A2, ? extends R2> v2) {
		this.v1 = v1;
		this.v2 = v2;
	}

	public <X> Arguments2Validator<A1, A2, X> apply(
			Function2<? super R1, ? super R2, ? extends X> f) {
		return (a1, a2, locale, constraintContext) -> Validations.apply(f::apply,
				this.v1.validate(a1, locale, constraintContext),
				this.v2.validate(a2, locale, constraintContext));
	}

	public <A3, R3> Arguments3Splitting<A1, A2, A3, R1, R2, R3> split(
			ValueValidator<? super A3, ? extends R3> v3) {
		return new Arguments3Splitting<>(v1, v2, v3);
	}
}
