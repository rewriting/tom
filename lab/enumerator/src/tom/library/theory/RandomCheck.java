package tom.library.theory;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Mark a parameter of a {@link org.junit.contrib.theories.Theory Theory} method with this annotation to have
 * generated values supplied to it randomly as in QuickCheck.
 */

@Target(PARAMETER)
@Retention(RUNTIME)
public @interface RandomCheck {

	public int sampleSize() default 10;
	public int minSampleSize() default 0;
	public int numberSamples() default 1;
		
}
