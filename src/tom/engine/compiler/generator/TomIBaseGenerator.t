package tom.engine.compiler.generator;

import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.tools.SymbolTable;

/**
 * Base interface for generators
 */
public interface TomIBaseGenerator{
	
	%include { adt/tomsignature/TomSignature.tom }
	
	public Expression generate(Expression expresion);
}