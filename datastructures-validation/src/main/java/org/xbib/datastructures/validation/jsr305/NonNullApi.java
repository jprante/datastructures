package org.xbib.datastructures.validation.jsr305;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A common annotation (similar to Spring one) to declare that parameters and return
 * values are to be considered as non-nullable by default for a given package. Leverages
 * JSR 305 meta-annotations to indicate nullability in Java to common tools with JSR 305
 * support.
 *
 * <p>
 * Should be used at package level in association with {@link Nullable} annotations at
 * parameter and return value level.
 *
 * @see Nullable
 * @see NonNull
 */
@Target(ElementType.PACKAGE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NonNullApi {
}
