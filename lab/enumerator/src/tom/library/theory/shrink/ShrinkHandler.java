package tom.library.theory.shrink;

import tom.library.theory.internal.CounterExample;

public interface ShrinkHandler {
	/**
	 * TODO write javadocs
	 * @param counterExample
	 * @throws Throwable
	 */
	public void shrink(Throwable e, CounterExample counterExample) throws Throwable;
}