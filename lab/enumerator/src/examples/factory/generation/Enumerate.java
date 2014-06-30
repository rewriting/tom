package examples.factory.generation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

//@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Enumerate {
	int maxSize() default 8;
	boolean memberCanBeNull() default false;
}

