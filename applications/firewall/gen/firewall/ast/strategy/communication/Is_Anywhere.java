
package firewall.ast.strategy.communication;

public class Is_Anywhere extends tom.library.sl.AbstractStrategy {
  private static final String msg = "Not an Anywhere";

  public Is_Anywhere() {
    initSubterm();
  }

  public tom.library.sl.Visitable visit(tom.library.sl.Environment envt) throws tom.library.sl.VisitFailure {
    return (tom.library.sl.Visitable) visit(envt,tom.library.sl.VisitableIntrospector.getInstance());
  }

  public tom.library.sl.Visitable visit(tom.library.sl.Visitable any) throws tom.library.sl.VisitFailure{
    return (tom.library.sl.Visitable) visit(any,tom.library.sl.VisitableIntrospector.getInstance());
  }

  public tom.library.sl.Visitable visitLight(tom.library.sl.Visitable any) throws tom.library.sl.VisitFailure {
    return (tom.library.sl.Visitable) visitLight(any,tom.library.sl.VisitableIntrospector.getInstance());
  }


  public Object visitLight(Object any, tom.library.sl.Introspector i) throws tom.library.sl.VisitFailure {
    if(any instanceof firewall.ast.types.communication.Anywhere) {
     return any;
    } else {
      throw new tom.library.sl.VisitFailure(msg);
    }
  }

  public int visit(tom.library.sl.Introspector i) {
    Object any = environment.getSubject();
    if(any instanceof firewall.ast.types.communication.Anywhere) {
     return tom.library.sl.Environment.SUCCESS;
    } else {
      return tom.library.sl.Environment.FAILURE;
    }
  }
}
