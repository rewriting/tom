package tom.engine.compiler.propagator;

import tom.engine.adt.tomconstraint.types.*;

/**
 * Base interface for propagators
 */
public interface TomIBasePropagator {
  public Constraint propagate(Constraint constraint);
}
