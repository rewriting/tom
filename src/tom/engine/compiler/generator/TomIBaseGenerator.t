package tom.engine.compiler.generator;

import tom.engine.adt.tominstruction.types.*;

/**
 * Base interface for generators
 */
public interface TomIBaseGenerator{
	
	%include { adt/tomsignature/TomSignature.tom }
	
	public Instruction generate(Instruction expresion);
}