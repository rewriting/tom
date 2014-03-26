package tom.library.theory;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.junit.contrib.theories.ParametersSuppliedBy;

/**
 * Mark a parameter of a {@link org.junit.contrib.theories.Theory Theory} method with this annotation to have
 * generated values supplied to it exhaustively as in SmallCheck.
 */

@ParametersSuppliedBy(ExhaustiveValueSupplier.class)
@Target(PARAMETER)
@Retention(RUNTIME)

public @interface ExhaustiveForAll {

	public int sampleSize() default 100;
	
}
