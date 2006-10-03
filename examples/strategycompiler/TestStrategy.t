package strategycompiler;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import tom.library.strategy.mutraveler.MuStrategy;
import tom.library.strategy.mutraveler.MuTraveler;
import tom.library.strategy.mutraveler.Identity;
import tom.library.strategy.mutraveler.Position;
import jjtraveler.VisitFailure;

import strategycompiler.teststrategy.term.types.*;

import java.util.*;

public class TestStrategy extends TestCase {

  %include { long.tom }
  %include { boolean.tom }
  %include { mustrategy.tom }
  %include { java/util/LinkedList.tom }

  %typeterm Hashtable{
  implement {Hashtable}  
  }
  
%gom{
    module Term

      abstract syntax
      Term = a() | b() | c() | d()
      | f(s1:Term)
      | ff(s1:Term)
      | g(s1:Term,s2:Term)
      | l(s3:L)
      | h(s1:Term,s2:Term)
      Term1 = e()
      L = concTerm(Term*)
  } 

  %typeterm Position {
    implement { tom.library.strategy.mutraveler.Position }
  }

  static boolean bool0 = true;

  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestStrategy.class));
  }

  public void setUp() {
  }

  public void testS(){
    
    Hashtable hashtable = new Hashtable();
    LinkedList linkedList = new LinkedList();
    boolean bool = true;
    long along = 1;
    String string = new String();
    int i = 0;
    
    MuStrategy rule0 = StrategyCompiler.compile(`S0(), "s0");
    MuStrategy rule1 = StrategyCompiler.compile(`S1(bool), "s1");
    MuStrategy rule2 = StrategyCompiler.compile(`S2(along,string), "s2");
    MuStrategy rule3 = StrategyCompiler.compile(`S3(i), "s3");
    MuStrategy rule4 = StrategyCompiler.compile(`S4(hashtable, linkedList), "s4");
    MuStrategy rule5 = StrategyCompiler.compile(`S5(), "s5");
    MuStrategy rule6 = StrategyCompiler.compile(`S6(), "s6");
    MuStrategy rule7 = StrategyCompiler.compile(`S7(), "s7");
    MuStrategy rule8 = StrategyCompiler.compile(`S8(bool), "s8");
    MuStrategy rule9 = StrategyCompiler.compile(`S9(i), "s9");
   
    try{
      assertSame("g(a,a) return a", MuTraveler.init(`rule0).visit(`g(a(),a())), `a());
      assertSame("a return b", MuTraveler.init(`rule1).visit(`a()), `b());
      assertSame("g(a,a) return a", MuTraveler.init(`rule2).visit(`g(a(),a())), `a());
      assertSame("g(a,a) return a", MuTraveler.init(`rule3).visit(`g(a(),a())), `a());
      assertSame("g(a,b) return g(b,a)", MuTraveler.init(`rule4).visit(`g(a(),b())), `g(b(),a()));
      assertSame("g(a,a) return a", MuTraveler.init(`rule5).visit(`g(a(),a())), `a());
      assertSame("g(a,a) return a", MuTraveler.init(`rule6).visit(`g(a(),a())), `a());
      assertSame("g(a,a) return a", MuTraveler.init(`rule7).visit(`g(a(),a())), `a());
      assertSame("g(a,a) return a", MuTraveler.init(`rule8).visit(`g(a(),a())), `a());
      assertSame("g(a,a) return a", MuTraveler.init(`rule9).visit(`g(a(),a())), `a());
    } catch (VisitFailure e){
      System.out.println("VisitFailure");
    }
  }
  public void testPosition() {
    //3 positions for now
    Hashtable positions = new Hashtable(3);
    Term t = `f(a());
    Term tBis = `f(b());
    MuStrategy getPos1 = `GetPositionA(positions,"p1");
    MuStrategy getPos2 = `GetPositionA(positions,"p2");
    MuStrategy getPos3 = `GetPositionA(positions,"p3");

    try{
      MuTraveler.init(StrategyCompiler.compile(`BottomUp(getPos1), "pos1")).visit(t);
      MuTraveler.init(StrategyCompiler.compile(`BottomUp(getPos2), "pos2")).visit(t);
      MuTraveler.init(StrategyCompiler.compile(`BottomUp(getPos3), "pos3")).visit(tBis);
    } catch (VisitFailure e){
      System.out.println("VisitFailure");
    }
/*

    Position p1 = (Position)positions.get("p1");
    Position p2 = (Position)positions.get("p2");
    Position p3 = (Position)positions.get("p3");
    assertTrue("equality on Position", p1.equals(p2));
    assertFalse("inequality on Position", p1.equals(p3));
*/
  }

  %strategy GetPositionA(positions:Hashtable,posName:String) extends Identity(){
    visit Term {
      a() -> {positions.put(posName,getPosition());}
    }
  }

  %strategy S0() extends Identity() {
    visit Term {
      g(x,x)            -> { return `x; }
    }
  }

  //with parameter
  %strategy S1(b:boolean) extends Identity() {
    visit Term {
      a()          -> { return `b(); }
    }
  }

  //with 2 parameters
  %strategy S2(l:long,s:String) extends Identity() {
    visit Term {
      g(x,x)            -> { return `x; }
    }
  }

  //with builtin parameter
  %strategy S3(i:int) extends Identity() {
    visit Term {
      g(x,x)            -> { return `x; }
    }
  }

  %strategy S4(hashtable:Hashtable,linkedList:LinkedList) extends Identity() {
    visit Term {
      g(x,y)            -> { linkedList.add("ok"); return `g(y,x);}
    }
  }

   //with many visits
  %strategy S5() extends Identity() {
    visit Term {
      g(x,x)            -> { return `x; }
    }
    visit Term1 {
      e()            -> { return `e(); }
    }
  }
  //with visits in reverse order
  %strategy S6() extends Identity() {
    visit Term1 {
      e()            -> { return `e(); }
    }

    visit Term {
      g(x,x)            -> { return `x; }
    }
  }

  %strategy S7() extends S8(bool0) {
    visit Term {
      g(x,x)            -> { return `x; }
    }
  }
  %strategy S8(b:boolean) extends  `S9(3) {
    visit Term {
      g(x,x)            -> { return `x; }
    }
  }

  %strategy S9(i:int) extends  Identity() {
    visit Term {
      g(x,x)            -> { return `x; }
    }
  }


  //with no visit
  //%strategy S4() extends Identity() {
  //}


  //with empty visit

  //with underscore only

}
