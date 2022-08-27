package org.xbib.datastructures.validation.jsr305;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A common annotation (similar to Spring ones) to declare that annotated elements
 * cannot be {@code null}. Leverages JSR 305 meta-annotations to indicate nullability in
 * Java to common tools with JSR 305 support.
 *
 * <p>
 * Should be used at parameter, return value, and field level. Methods overrides should
 * repeat parent {@code @NonNull} annotations unless they behave differently.
 *
 * <p>
 * Use {@code @NonNullApi} (scope = parameters + return values) to set the default
 * behavior to non-nullable in order to avoid annotating your whole codebase with
 * {@code @NonNull}.
 *
 * @see NonNullApi
 * @see Nullable
 */
@Target({ ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NonNull {
}
