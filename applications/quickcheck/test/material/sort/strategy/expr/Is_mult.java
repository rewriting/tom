
package sort.strategy.expr;

public class Is_mult extends tom.library.sl.AbstractStrategyCombinator {
  private static final String msg = "Not an mult";

  public Is_mult() {
    initSubterm();
  }

  @SuppressWarnings("unchecked")
  public <T extends tom.library.sl.Visitable> T visit(tom.library.sl.Environment envt) throws tom.library.sl.VisitFailure {
    return (T) visit(envt,tom.library.sl.VisitableIntrospector.getInstance());
  }

  public <T extends tom.library.sl.Visitable> T visit(T any) throws tom.library.sl.VisitFailure{
    return visit(any,tom.library.sl.VisitableIntrospector.getInstance());
  }

  public <T extends tom.library.sl.Visitable> T visitLight(T any) throws tom.library.sl.VisitFailure {
    return visitLight(any,tom.library.sl.VisitableIntrospector.getInstance());
  }

  public <T> T visitLight(T any, tom.library.sl.Introspector i) throws tom.library.sl.VisitFailure {
    if(any instanceof sort.types.expr.mult) {
     return any;
    } else {
      throw new tom.library.sl.VisitFailure(msg);
    }
  }

  public int visit(tom.library.sl.Introspector i) {
    Object any = environment.getSubject();
    if(any instanceof sort.types.expr.mult) {
     return tom.library.sl.Environment.SUCCESS;
    } else {
      return tom.library.sl.Environment.FAILURE;
    }
  }
}
