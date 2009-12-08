package debugger;
import tom.library.sl.*;

public class MuVarDecorator extends ADecorator {

  %include { sl.tom }

  public MuVarDecorator(Strategy s) {
    %match(s) {
      !MuVar(_) -> { throw new RuntimeException(); }
      MuVar(x) -> { name = `x; }
    }
    visitors = new Strategy[] {s};
  }

  public Visitable setChildren(Visitable[] cdn) {
    return supersetChildren(cdn);
  }

  public Visitable setChildAt(int i, Visitable child) {
    return supersetChildAt(i,child);
  }
  
  public final Object visitLight(Object any, Introspector m) throws VisitFailure {
      System.out.println(this);
      return visitors[0].visitLight(any,m);
  }

  public int visit(Introspector m) {
    printState();
    return visitors[0].visit(m);
  }

  public String toString() {
    return name;
  }
}
