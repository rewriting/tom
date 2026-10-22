
























package tom.engine.compiler.propagator;



import tom.engine.adt.tomconstraint.types.*;
import tom.library.sl.*;






public interface IBasePropagator {
  public Constraint propagate(Constraint constraint) throws VisitFailure;
}
