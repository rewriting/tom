package tom.library.factory;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Enumerate {
	int maxSize() default 8;
	boolean canBeNull() default false;
	int numberOfSamples() default -1;
}

