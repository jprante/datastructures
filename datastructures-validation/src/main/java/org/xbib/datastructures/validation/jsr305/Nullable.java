package org.xbib.datastructures.validation.jsr305;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A common annotation (similar to Spring ones) to declare that annotated elements
 * can be {@code null} under some circumstance. Leverages JSR 305 meta-annotations to
 * indicate nullability in Java to common tools with JSR 305 support.
 *
 * <p>
 * Should be used at parameter, return value, and field level. Methods overrides should
 * repeat parent {@code @Nullable} annotations unless they behave differently.
 *
 * <p>
 * Can be used in association with {@code NonNullApi} to override the default non-nullable
 * semantic to nullable.
 *
 * @see NonNullApi
 * @see NonNull
 */
@Target({ ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Nullable {
}
