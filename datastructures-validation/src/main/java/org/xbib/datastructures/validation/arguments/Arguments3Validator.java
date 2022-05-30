package org.xbib.datastructures.validation.arguments;

import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;

import org.xbib.datastructures.validation.core.ConstraintContext;
import org.xbib.datastructures.validation.core.ConstraintGroup;
import org.xbib.datastructures.validation.core.ConstraintViolationsException;
import org.xbib.datastructures.validation.core.Validated;
import org.xbib.datastructures.validation.core.ValueValidator;
import org.xbib.datastructures.validation.jsr305.Nullable;

@FunctionalInterface
public interface Arguments3Validator<A1, A2, A3, X> {

	Validated<X> validate(@Nullable A1 a1, @Nullable A2 a2, @Nullable A3 a3,
			Locale locale, ConstraintContext constraintContext);

	default <X2> Arguments3Validator<A1, A2, A3, X2> andThen(
			Function<? super X, ? extends X2> mapper) {
		return (a1, a2, a3, locale, constraintContext) -> Arguments3Validator.this
				.validate(a1, a2, a3, locale, constraintContext).map(mapper);
	}

	default <X2> Arguments3Validator<A1, A2, A3, X2> andThen(
			ValueValidator<? super X, X2> validator) {
		return (a1, a2, a3, locale, constraintContext) -> Arguments3Validator.this
				.validate(a1, a2, a3, locale, constraintContext)
				.flatMap(v -> validator.validate(v, locale, constraintContext));
	}

	default <A> Arguments1Validator<A, X> compose(
			Function<? super A, ? extends Arguments3<? extends A1, ? extends A2, ? extends A3>> mapper) {
		return (a, locale, constraintContext) -> {
			final Arguments3<? extends A1, ? extends A2, ? extends A3> args = mapper
					.apply(a);
			return Arguments3Validator.this.validate(args.arg1(), args.arg2(),
					args.arg3(), locale, constraintContext);
		};
	}

	default Arguments3Validator<A1, A2, A3, Supplier<X>> lazy() {
		// WARNING:: The default implementation is not really lazy!
		return this.andThen(x -> () -> x);
	}

	default Validated<X> validate(@Nullable A1 a1, @Nullable A2 a2, @Nullable A3 a3) {
		return this.validate(a1, a2, a3, Locale.getDefault(), ConstraintGroup.DEFAULT);
	}

	default Validated<X> validate(@Nullable A1 a1, @Nullable A2 a2, @Nullable A3 a3,
			ConstraintContext constraintContext) {
		return this.validate(a1, a2, a3, Locale.getDefault(), constraintContext);
	}

	default Validated<X> validate(@Nullable A1 a1, @Nullable A2 a2, @Nullable A3 a3,
			Locale locale) {
		return this.validate(a1, a2, a3, locale, ConstraintGroup.DEFAULT);
	}

	default X validated(@Nullable A1 a1, @Nullable A2 a2, @Nullable A3 a3)
			throws ConstraintViolationsException {
		return this.validate(a1, a2, a3).orElseThrow(ConstraintViolationsException::new);
	}

	default X validated(@Nullable A1 a1, @Nullable A2 a2, @Nullable A3 a3,
			ConstraintContext constraintContext) throws ConstraintViolationsException {
		return this.validate(a1, a2, a3, constraintContext)
				.orElseThrow(ConstraintViolationsException::new);
	}

	default X validated(@Nullable A1 a1, @Nullable A2 a2, @Nullable A3 a3, Locale locale)
			throws ConstraintViolationsException {
		return this.validate(a1, a2, a3, locale)
				.orElseThrow(ConstraintViolationsException::new);
	}

	default X validated(@Nullable A1 a1, @Nullable A2 a2, @Nullable A3 a3, Locale locale,
			ConstraintContext constraintContext) throws ConstraintViolationsException {
		return this.validate(a1, a2, a3, locale, constraintContext)
				.orElseThrow(ConstraintViolationsException::new);
	}

}
