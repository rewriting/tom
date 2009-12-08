package sl;
import jjtraveler.*;

public class SequenceIdJJ implements Visitor {
    
  public Visitor first;
  public Visitor then;
    
  public SequenceIdJJ(Visitor first, Visitor then) {
    this.first = first;
    this.then  = then;
  }

  public Visitable visit(Visitable any) throws VisitFailure {
    Visitable t = first.visit(any);
    if(t.equals(any)) return t;
    return then.visit(t);
  }

  protected void setArgumentAt(int i, Visitor v) {
    switch (i) {
    case 1: first = v; return;
    case 2: then = v; return;
    default: throw new RuntimeException("Argument out of bounds: "+i);
    }
  }
}
