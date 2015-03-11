package tom.library.factory;

import java.lang.annotation.*;

@Target({ElementType.TYPE,ElementType.CONSTRUCTOR,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumerateGenerator {
	boolean canBeNull() default true;
}
