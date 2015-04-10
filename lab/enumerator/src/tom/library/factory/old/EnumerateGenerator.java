package tom.library.factory.old;

import java.lang.annotation.*;

@Target({ElementType.TYPE,ElementType.CONSTRUCTOR,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumerateGenerator {
	boolean canBeNull() default true;
}
