import junit.framework.TestCase;
import junit.framework.TestSuite;

import tom.library.strategy.mutraveler.MuStrategy;
import tom.library.strategy.mutraveler.MuTraveler;
import tom.library.strategy.mutraveler.Identity;
import tom.library.strategy.mutraveler.Position;
import jjtraveler.VisitFailure;

import teststrategy.term.types.*;

import java.util.*;

public class TestStrategy extends TestCase {

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
    
    MuStrategy rule0 = `S0();
    MuStrategy rule1 = `S1(bool);
    MuStrategy rule2 = `S2(along,string);
    MuStrategy rule3 = `S3(i);
    MuStrategy rule4 = `S4(hashtable, linkedList);
    MuStrategy rule5 = `S5();
    MuStrategy rule6 = `S6();
    MuStrategy rule7 = `S7();
    MuStrategy rule8 = `S8(bool);
    MuStrategy rule9 = `S9(i);
    MuStrategy rule10 = `S10();
    MuStrategy rule11 = `S11();
    MuStrategy rule12 = `S12();
    MuStrategy rule13 = `S13("test");
    MuStrategy rule14 = `S14("test", S10());
   
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
      /*assertSame(MuTraveler.init(`rule10).visit(`S6()), `S8(true));
      assertSame(MuTraveler.init(`rule10).visit(`All(All(Fail()))), `All(Fail()));
      assertSame(MuTraveler.init(`rule10).visit(`All(Identity())), `All(Identity()));
      assertSame(MuTraveler.init(`rule11).visit(`S8(true)), `S9(42));
      assertSame(MuTraveler.init(`rule11).visit(`S8(false)), `S9(56));
      assertSame(MuTraveler.init(`rule11).visit(`S9(3)), `S8(true));
      assertSame(MuTraveler.init(`rule12).visit(`S2(4, "test")), `S2(5, "testtest"));
      assertSame(MuTraveler.init(`rule13).visit(`S6()), `("testtest"));
      assertSame(MuTraveler.init(`rule14).visit(`S6()), `S14("testtest", S8(true)));
      assertSame(MuTraveler.init(`rule14).visit(`S8(true)), `S14("true", S10()));*/
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
      MuTraveler.init(`BottomUp(getPos1)).visit(t);
      MuTraveler.init(`BottomUp(getPos2)).visit(t);
      MuTraveler.init(`BottomUp(getPos3)).visit(tBis);
    } catch (VisitFailure e){
      System.out.println("VisitFailure");
    }

    Position p1 = (Position)positions.get("p1");
    Position p2 = (Position)positions.get("p2");
    Position p3 = (Position)positions.get("p3");
    assertTrue("equality on Position", p1.equals(p2));
    assertFalse("inequality on Position", p1.equals(p3));
  }

  %strategy GetPositionA(positions:Hashtable,posName:String) extends `Identity(){
    visit Term {
      a() -> {positions.put(posName,getPosition());}
    }
  }

  %strategy S0() extends `Identity() {
    visit Term {
      g(x,x)            -> { return `x; }
    }
  }

  //with parameter
  %strategy S1(b:boolean) extends `Identity() {
    visit Term {
      a()          -> { return `b(); }
    }
  }

  //with 2 parameters
  %strategy S2(l:long,s:String) extends `Identity() {
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

  %strategy S4(hashtable:Hashtable,linkedList:LinkedList) extends `Identity() {
    visit Term {
      g(x,y)            -> { linkedList.add("ok"); return `g(y,x);}
    }
  }

   //with many visits
  %strategy S5() extends `Identity() {
    visit Term {
      g(x,x)            -> { return `x; }
    }
    visit Term1 {
      e()            -> { return `e(); }
    }
  }
  //with visits in reverse order
  %strategy S6() extends `Identity() {
    visit Term1 {
      e()            -> { return `e(); }
    }

    visit Term {
      g(x,x)            -> { return `x; }
    }
  }

  %strategy S7() extends `S8(bool0) {
    visit Term {
      g(x,x)            -> { return `x; }
    }
  }
  %strategy S8(b:boolean) extends `S9(3) {
    visit Term {
      g(x,x)            -> { return `x; }
    }
  }

  %strategy S9(i:int) extends `Identity() {
    visit Term {
      g(x,x)            -> { return `x; }
    }
  }

  // with strategy visit
  %strategy S10() extends `Identity() {
    visit Strategy {
      S6() -> { return `S8(true); }
      All(All(x)) -> { return `All(x); }
    }
  }
  %strategy S11() extends `Identity() {
    visit Strategy {
      S8(true) -> { return `S9(42); }
      S8(false) -> { return `S9(56); }
      S9(i) -> { return `S8(true); }
    }
  }
  %strategy S12() extends `Identity() {
    visit Strategy {
      S2(l,s) -> { return `S2(l+1,s+s); }
    }
  }
  %strategy S13(str:String) extends `Identity() {
    visit Strategy {
      S6() -> { return `S13(str+str); }
    }
  }
  %strategy S14(str:String,s:Strategy) extends `Identity() {
    visit Strategy {
      S6() -> { return `S14(str+str,S8(true)); }
      S8(true) -> { return `S14("true",s); }
    }
  }


  //with no visit
  //%strategy S4() extends `Identity() {
  //}


  //with empty visit

  //with underscore only

}
