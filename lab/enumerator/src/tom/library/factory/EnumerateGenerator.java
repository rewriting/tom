package tom.library.factory;

import java.lang.annotation.*;

@Target({ElementType.TYPE,ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumerateGenerator {
	boolean canBeNull() default true;
}
