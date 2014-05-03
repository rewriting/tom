package tom.library.theory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import tom.library.theory.shrink.DefaultShrinkHandler;
import tom.library.theory.shrink.ShrinkHandler;

/**
 * Hooks the shrink behavior to a user defined one
 * @author nauval
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Shrink {
	Class<? extends ShrinkHandler> handler() default DefaultShrinkHandler.class; 
}
