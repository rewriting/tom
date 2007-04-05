package tom.engine.compiler.generator;

import tom.engine.adt.tomexpression.types.*;

/**
 * Base interface for generators
 */
public interface TomIBaseGenerator{
	public Expression generate(Expression expresion);
}
