import static org.junit.Assert.*;
import org.junit.Test;

import tom.library.sl.*;

import teststrategy.term.types.*;

import java.util.*;

public class TestStrategy {

  %include { sl.tom }
  %include { boolean.tom }
  %include { long.tom }
  %include { java/util/LinkedList.tom }

  %typeterm Hashtable {
    implement { Hashtable }
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

      Pf = Unary() | Variadic(Pf*)
  } 

  static boolean bool0 = true;

  public static void main(String[] args) throws VisitFailure{
    org.junit.runner.JUnitCore.main(TestStrategy.class.getName());
  }

  public void setUp() {
  }

  @Test
  public void testS() {
    
    Hashtable hashtable = new Hashtable();
    LinkedList linkedList = new LinkedList();
    boolean bool = true;
    long along = 1;
    String string = new String();
    int i = 0;
    
    Strategy rule0 = `S0();
    Strategy rule1 = `S1(bool);
    Strategy rule2 = `S2(along,string);
    Strategy rule3 = `S3(i);
    Strategy rule4 = `S4(hashtable, linkedList);
    Strategy rule5 = `S5();
    Strategy rule6 = `S6();
    Strategy rule7 = `S7();
    Strategy rule8 = `S8(bool);
    Strategy rule9 = `S9(i);
   
    try {
      assertSame("g(a,a) return a", `rule0.visitLight(`g(a(),a())), `a());
      assertSame("a return b", `rule1.visitLight(`a()), `b());
      assertSame("g(a,a) return a", `rule2.visitLight(`g(a(),a())), `a());
      assertSame("g(a,a) return a", `rule3.visitLight(`g(a(),a())), `a());
      assertSame("g(a,b) return g(b,a)", `rule4.visitLight(`g(a(),b())), `g(b(),a()));
      assertSame("g(a,a) return a", `rule5.visitLight(`g(a(),a())), `a());
      assertSame("g(a,a) return a", `rule6.visitLight(`g(a(),a())), `a());
      assertSame("g(a,a) return a", `rule7.visitLight(`g(a(),a())), `a());
      assertSame("g(a,a) return a", `rule8.visitLight(`g(a(),a())), `a());
      assertSame("g(a,a) return a", `rule9.visitLight(`g(a(),a())), `a());
    } catch (tom.library.sl.VisitFailure e){
      System.out.println("tom.library.sl.VisitFailure()");
    }
  }

  @Test
  public void testPosition() throws VisitFailure {
    //3 positions for now
    Hashtable positions = new Hashtable(3);
    Term t = `f(a());
    Term tBis = `f(b());
    Strategy getPos1 = `GetPositionA(positions,"p1");
    Strategy getPos2 = `GetPositionA(positions,"p2");
    Strategy getPos3 = `GetPositionA(positions,"p3");
    
    `BottomUp(getPos1).visit(t);
    `BottomUp(getPos2).visit(t);
    `BottomUp(getPos3).visit(tBis);

    Position p1 = (Position)positions.get("p1");
    Position p2 = (Position)positions.get("p2");
    Position p3 = (Position)positions.get("p3");
    assertTrue("equality on Position", p1.equals(p2));
    assertFalse("inequality on Position", p1.equals(p3));
  }

  %strategy GetPositionA(positions:Hashtable,posName:String) extends Identity(){
    visit Term {
      a() -> {positions.put(posName,getEnvironment().getPosition());}
    }
  }

  %strategy S0() extends Identity() {
    visit Term {
      g(x,x) -> { return `x; }
    }
  }

  //with parameter
  %strategy S1(b:boolean) extends Identity() {
    visit Term {
      a() -> { return `b(); }
    }
  }

  //with 2 parameters
  %strategy S2(l:long,s:String) extends Identity() {
    visit Term {
      g(x,x) -> { return `x; }
    }
  }

  //with builtin parameter
  %strategy S3(i:int) extends Identity() {
    visit Term {
      g(x,x) -> { return `x; }
    }
  }

  %strategy S4(hashtable:Hashtable,linkedList:LinkedList) extends Identity() {
    visit Term {
      g(x,y) -> { linkedList.add("ok"); return `g(y,x);}
    }
  }

   //with many visits
  %strategy S5() extends Identity() {
    visit Term {
      g(x,x) -> { return `x; }
    }
    visit Term1 {
      e() -> { return `e(); }
    }
  }
  //with visits in reverse order
  %strategy S6() extends Identity() {
    visit Term1 {
      e() -> { return `e(); }
    }

    visit Term {
      g(x,x) -> { return `x; }
    }
  }

  %strategy S7() extends S8(bool0) {
    visit Term {
      g(x,x) -> { return `x; }
    }
  }
  %strategy S8(b:boolean) extends `S9(3) {
    visit Term {
      g(x,x) -> { return `x; }
    }
  }

  %strategy S9(i:int) extends  Identity() {
    visit Term {
      g(x,x) -> { return `x; }
    }
  }

  %strategy fnc() extends Identity() {
    visit Pf {
      Unary() -> { return `Variadic(Variadic(Unary()));}
    }
  }

  @Test
  public void testFnc() {
    try {
      assertEquals(`fnc().visitLight(`Unary()), `Variadic(Variadic(Unary())));
    } catch (tom.library.sl.VisitFailure e) {
      fail("tom.library.sl.VisitFailure()");
    }
  }

  //with no visit
  //%strategy S4() extends Identity() {
  //}


  //with empty visit

  //with underscore only

}
