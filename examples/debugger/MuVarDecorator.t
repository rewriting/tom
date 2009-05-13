package debugger;
import tom.library.sl.*;

public class MuVarDecorator extends ADecorator {

  %include { sl.tom }

  public MuVarDecorator(Strategy s) {
    %match(s) {
      !MuVar(_) -> { throw new RuntimeException(); }
      MuVar(x) -> { name = `x; }
    }
    initSubterm(new Strategy[] {s});
  }

  public Visitable setChildren(Visitable[] cdn) {
    return supersetChildren(cdn);
  }

  public Visitable setChildAt(int i, Visitable child) {
    return supersetChildAt(i,child);
  }
  
  public final Object visitLight(Object any, Introspector m) throws VisitFailure {
      System.out.println(this);
      return getVisitor(0).visitLight(any,m);
  }

  public int visit(Introspector m) {
      String repr = this.toString();
      StringBuffer spaces = new StringBuffer();
      for(int i=0; i<60-repr.length(); i++) spaces.append(' ');
      System.out.println(repr + spaces + getEnvironment().getSubject());
      return getVisitor(0).visit(m);
  }

  public String toString() {
    return name;
  }
}
