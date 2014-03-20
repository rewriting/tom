package examples.theory;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.junit.contrib.theories.ParametersSuppliedBy;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

/**
* Mark a parameter of a {@link org.junit.contrib.theories.Theory Theory} method with this annotation to have
* random values supplied to it.
*/
@Target(PARAMETER)
@Retention(RUNTIME)
@ParametersSuppliedBy(RandomNaturalTreeValueSupplier.class)
public @interface ForAllNaturalTree {
   /**
    * @return the number of generated values to give the annotated theory parameter
    */
   int sampleSize() default 100;

}
