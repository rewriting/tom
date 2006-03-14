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

import java.util.*;

public class TestStrategy extends TestCase {

  %include { mutraveler.tom }

  %vas{
    module Term
      imports
      public
      sorts Term Term1 L

      abstract syntax
      a -> Term
      b -> Term
      c -> Term
      d -> Term
      e -> Term1
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
    
    ArrayList arrayList = new ArrayList();
    LinkedList linkedList = new LinkedList();
    String string = new String();
    int i = 0;
    
    VisitableVisitor rule0 = new S0();
    VisitableVisitor rule1 = new S1(linkedList);
    VisitableVisitor rule2 = new S2(arrayList,string);
    VisitableVisitor rule3 = new S3(i);
    //VisitableVisitor rule4 = new S4();
    VisitableVisitor rule5 = new S5();
    //VisitableVisitor rule6 = new S6();
    //VisitableVisitor rule7 = new S7();
    
    try{
      assertSame("g(a,a) return a", MuTraveler.init(`rule0).visit(`g(a,a)), `a);
      assertSame("a return b", MuTraveler.init(`rule1).visit(`a), `b);
      assertSame("g(a,a) return a", MuTraveler.init(`rule2).visit(`g(a,a)), `a);
      assertSame("g(a,a) return a", MuTraveler.init(`rule3).visit(`g(a,a)), `a);
      assertSame("g(a,a) return a", MuTraveler.init(`rule5).visit(`g(a,a)), `a);
    } catch (VisitFailure e){
      System.out.println("VisitFailure");
    }
  }

  %strategy S0() extends `Identity() {
    visit Term {
      g(x,x)            -> { return `x; }
    }
  }

  //with parameter
  %strategy S1(l:LinkedList) extends `Identity() {
    visit Term {
      a()          -> { return `b; }
    }
  }

  //with 2 parameters
  %strategy S2(a:ArrayList,v:String) extends `Identity() {
    visit Term {
      g(x,x)            -> { return `x; }
    }
  }

  //with builtin parameter
  %strategy S3(i:int) extends `Identity() {
    visit Term {
      g(x,x)            -> { return `x; }
    }
  }

  //with no visit
  //%strategy S4() extends `Identity() {
  //}

  //with many visits
  %strategy S5() extends `Identity() {
    visit Term {
      g(x,x)            -> { return `x; }
    }
    visit Term1 {
      e()            -> { return `e; }
    }
  }

//with empty visit

//with underscore only
}
