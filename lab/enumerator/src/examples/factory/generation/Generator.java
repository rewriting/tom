package examples.factory.generation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

//@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Generator {
	boolean canBeNull() default true;
}
