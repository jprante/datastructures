package org.xbib.datastructures.validation.arguments;

import org.xbib.datastructures.validation.core.ValueValidator;
import org.xbib.datastructures.validation.fn.Function4;
import org.xbib.datastructures.validation.fn.Validations;

public class Arguments4Splitting<A1, A2, A3, A4, R1, R2, R3, R4> {
	protected final ValueValidator<? super A1, ? extends R1> v1;

	protected final ValueValidator<? super A2, ? extends R2> v2;

	protected final ValueValidator<? super A3, ? extends R3> v3;

	protected final ValueValidator<? super A4, ? extends R4> v4;

	public Arguments4Splitting(ValueValidator<? super A1, ? extends R1> v1,
			ValueValidator<? super A2, ? extends R2> v2,
			ValueValidator<? super A3, ? extends R3> v3,
			ValueValidator<? super A4, ? extends R4> v4) {
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
		this.v4 = v4;
	}

	public <X> Arguments4Validator<A1, A2, A3, A4, X> apply(
			Function4<? super R1, ? super R2, ? super R3, ? super R4, ? extends X> f) {
		return (a1, a2, a3, a4, locale, constraintContext) -> Validations.apply(f::apply,
				this.v1.validate(a1, locale, constraintContext),
				this.v2.validate(a2, locale, constraintContext),
				this.v3.validate(a3, locale, constraintContext),
				this.v4.validate(a4, locale, constraintContext));
	}

	public <A5, R5> Arguments5Splitting<A1, A2, A3, A4, A5, R1, R2, R3, R4, R5> split(
			ValueValidator<? super A5, ? extends R5> v5) {
		return new Arguments5Splitting<>(v1, v2, v3, v4, v5);
	}
}
