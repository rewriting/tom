package ted;

import java.io.*;
import aterm.*;
import tom.library.strategy.mutraveler.*;
import aterm.pure.PureFactory;
import jjtraveler.VisitFailure;
import jjtraveler.reflective.*;

public class GrepVisitor extends aterm.ATermFwdVoid implements jjtraveler.reflective.VisitableVisitor {

  private static ATermFactory atermFactory = new PureFactory();
  ATerm tomatch;
  ATermList res = atermFactory.makeList();

  public GrepVisitor(ATerm tomatch) {
    super();
    this.tomatch = tomatch;
  }

  public void voidVisitATerm(ATerm arg) throws jjtraveler.VisitFailure {
    if(Ted.match(tomatch, arg) != null) {
      res = res.append(arg);
      throw new VisitFailure();
      //TODO throw this instance and use mu x.(try(sequence(s,all(x)))
    }
  }

  public ATermList getList() {
    return res;
  }

  /* for the visitablevisitor compatibility */
  public  jjtraveler.Visitable setChildAt(int index, jjtraveler.Visitable v) { throw new IndexOutOfBoundsException(); }
  public  jjtraveler.Visitable getChildAt(int index) { throw new IndexOutOfBoundsException(); }
  public int getChildCount() { return 0; }
}

