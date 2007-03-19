package tom.engine.compiler.propagator;

import tom.engine.adt.tomconstraint.types.*;


/**
 * Base interface for propagators
 */
public interface TomIBasePropagator{
//[pem] do we really need TomSignature.tom ?	
	%include { adt/tomsignature/TomSignature.tom }
	
	public Constraint propagate(Constraint constraint);
}
