package tom.library.theory;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Mark a parameter of a {@link org.junit.contrib.theories.Theory Theory} method with this annotation to have
 * generated values supplied to it exhaustively as in SmallCheck.
 */

@Target(PARAMETER)
@Retention(RUNTIME)

public @interface ExhaustiveCheck {

	public int  minSampleSize() default 0;
	public int 	maxDepth() default 100;
	
}
