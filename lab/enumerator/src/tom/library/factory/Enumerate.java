package tom.library.factory;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Enumerate {
	int maxSize() default 8;
	boolean memberCanBeNull() default false;
}

