
























package tom.engine.compiler.generator;



import tom.engine.adt.tomexpression.types.*;
import tom.library.sl.*;






public interface IBaseGenerator {
  public Expression generate(Expression expresion) throws VisitFailure;
}
