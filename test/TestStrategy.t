import aterm.*;
import aterm.pure.*;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import tom.library.strategy.mutraveler.MuTraveler;
import tom.library.strategy.mutraveler.Position;
import tom.library.strategy.mutraveler.Identity;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.Visitable;
import jjtraveler.VisitFailure;

import teststrategy.term.types.*;

public class TestStrategy extends TestCase {

  %include { mutraveler.tom }

  %vas{
    module Term
      imports
      public
      sorts Term L

      abstract syntax
      a -> Term
      b -> Term
      c -> Term
      d -> Term
      f(s1:Term) -> Term
      ff(s1:Term) -> Term
      g(s1:Term,s2:Term) -> Term
      l(s3:L) -> Term
      h(s1:Term,s2:Term) -> Term
      k(s2:Term,s1:Term) -> Term
      concTerm(Term*) -> L

  } 

  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestStrategy.class));
  }

  public void setUp() {
  }

  public void testS1(){
    
    Term subject = `g(a,a);
    VisitableVisitor rule = new S1();
    
    try{
      assertSame("g(a,a) return a",
          MuTraveler.init(`rule).visit(subject),
          `a);
    } catch (VisitFailure e){
      System.out.println("VisitFailure");
    }
  }

  %strategy S1() extends `Identity() {
    visit Term {
      g(x,x)            -> { return `x; }
    }
  }
}
