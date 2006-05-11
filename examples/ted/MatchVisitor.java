package ted;

import java.io.*;
import aterm.*;
import tom.library.strategy.mutraveler.*;


public class MatchVisitor extends aterm.ATermFwdVoid {

  ATerm tomatch;

  public MatchVisitor(ATerm tomatch) {
    super();
    this.tomatch = tomatch;
  }

  public void voidVisitATerm(ATerm arg) {
    if(Ted.match(tomatch, arg) != null) {
      System.out.println("matched !");
    }
  }
}

