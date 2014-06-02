package tom.library.theory.shrink;

import tom.library.theory.internal.CounterExample;

/**
 * Interface to create a shrink handler.
 * 
 * @author nauval
 *
 */
public interface ShrinkHandler {
	/**
	 * Performs shrink mechanism from the given counter-example.
	 * 
	 * @param counterExample
	 * @throws Throwable
	 */
	public void shrink(Throwable e, CounterExample counterExample) throws Throwable;
}