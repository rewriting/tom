package ted;

import java.io.*;
import aterm.*;
import tom.library.strategy.mutraveler.*;
import aterm.pure.PureFactory;
import jjtraveler.VisitFailure;

public class GrepVisitor extends aterm.ATermFwdVoid {

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
      //throw new VisitFailure();
      //TODO throw this instance and use mu x.(try(sequence(s,all(x)))
    }
  }

  public ATermList getList() {
    return res;
  }
}

