import java.io.*;
import aterm.*;
import tom.library.strategy.mutraveler.*;


public class MatchVisitor extends aterm.ATermFwdVoid {

  Ted ted = new Ted();
  ATerm tomatch;

  public MatchVisitor(ATerm tomatch) {
    super();
    this.tomatch = tomatch;
  }

  public void voidVisitATerm(ATerm arg) {
    if(ted.match(tomatch, arg)) {
      System.out.println("matched !");
    }
  }
}

