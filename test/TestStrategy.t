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

public class TestStrategy extends TestCase {

  %include { mutraveler.tom }
 
%vas{
    module Term
      imports
      public
      sorts E L

      abstract syntax
      a -> E
      b -> E
      c -> E
      d -> E
      f(s1:E) -> E
      ff(s1:E) -> E
      g(s1:E,s2:E) -> E
      l(s3:L) -> E
      h(s1:E,s2:E) -> E
      k(s2:E,s1:E) -> E
      concE(E*) -> L

  } 

  private static teststrategy.term.types.E ok,fail;
  private static teststrategy.term.types.E pattern1,pattern2,pattern3,pattern4,pattern5;
 
  private ATermFactory factory;

	public static void main(String[] args) {
		junit.textui.TestRunner.run(new TestSuite(TestStrategy.class));
	}
	
  public void setUp() {
    factory = new PureFactory(16);
    ok   = (teststrategy.term.types.E)factory.parse("ok");
    fail = (teststrategy.term.types.E)factory.parse("fail");
    pattern1 = (teststrategy.term.types.E)factory.parse("pattern1");
    pattern2 = (teststrategy.term.types.E)factory.parse("pattern2");
    pattern3 = (teststrategy.term.types.E)factory.parse("pattern3");
    pattern4 = (teststrategy.term.types.E)factory.parse("pattern4");
    pattern5 = (teststrategy.term.types.E)factory.parse("pattern5");
  }

	static class TestData {
		String question;
		String answer;
		public TestData(String question, String answer) {
			this.question = question;
			this.answer = answer;
		}
	}

	static class TestData2 {
		String question1;
		String question2;
		String answer;
		public TestData2(String question1, String question2, String answer) {
			this.question1 = question1;
			this.question2 = question2;
			this.answer = answer;
		}
	}

  public void test1() {
		TestData[] TEST = new TestData[] {
			new TestData("g(a,a)"                     ,"pattern1"), 
			new TestData("g(a,b)"                     ,"fail    "),
			new TestData("h(f(b),f(b))"               ,"pattern2"),
			new TestData("h(f(a),f(b))"               ,"fail    "),
			new TestData("h(f(f(g(a,b))),g(b,g(a,b)))","pattern3"),
			new TestData("h(f(f(g(a,b))),g(b,g(b,b)))","fail    "),
			new TestData("h(b,g(b,b))"                ,"pattern4"),
			new TestData("h(a,g(b,b))"                ,"fail    ") 
		};

    VisitableVisitor rule = new S1();

    try{
      for (int i=0; i<TEST.length;i++) {
        assertSame(
            "TestMatch1 expected "+TEST[i].answer+" for match1("+TEST[i].question+")",
            MuTraveler.init(`BottomUp(rule)).visit(factory.parse(TEST[i].question)),
            factory.parse(TEST[i].answer)
            );
      }
    } catch (VisitFailure e){
        System.out.println("VisitFailure");
      }
    }

    %strategy S1() extends `Fail() {
      visit E {
        g(x,x)            -> { return pattern1; }
        h(f(x),f(x))      -> { return pattern2; }
        h(f(f(x)),g(y,x)) -> { return pattern3; }
        h(x,g(x,x))       -> { return pattern4; }
        _ -> {return fail;}
      }
    }
/*
  public void test2() {
		TestData2[] TEST = new TestData2[] {
			new TestData2("g(a,b)"      ,"f(a)","pattern1 "), 
			new TestData2("g(b,b)"      ,"f(a)","fail     "),
			new TestData2("g(f(a),f(a))","f(a)","pattern2 "),
			new TestData2("g(f(b),f(a))","f(a)","fail     ")
		};

		for (int i=0; i<TEST.length;i++) {
			assertSame(
				"TestMatch2 expected "+TEST[i].answer+" for match2("+TEST[i].question1+","+TEST[i].question2+")",
				match2(factory.parse(TEST[i].question1),factory.parse(TEST[i].question2)),
				factory.parse(TEST[i].answer)
				);
		}
  }

  public ATerm match2(ATerm t1, ATerm t2) {
    ATerm res = fail;
    %match(E t1, E t2) {
      g(x,y), f(x)       -> { return pattern1; }
      g(f(x),f(x)), f(x) -> { return pattern2; }
    }
    return res;
  }

  public void test3() {
		TestData[] TEST = new TestData[] {
			new TestData("h(l([a,b,a,b]),a)"               ,"pattern1"), 
			new TestData("h(l([a,b,a,b]),l([a,a,b,a]))"    ,"fail    "),
			new TestData("h(l([a,b,a,b]),l([a,a,b,a,b]))"  ,"pattern2"),
			new TestData("l([b,b,l([a]),a,l([a,c]),b,c])"  ,"fail    "),
			new TestData("l([a,b,l([a]),a,l([a,c]),b,c])"  ,"pattern3"),
			new TestData("l([a,b,l([c]),a,b,l([a,c]),b,c])","fail    "),
			new TestData("l([a,b,l([c]),a,c,l([a,c]),b,c])","pattern3")
		};

		for (int i=0; i<TEST.length;i++) {
			assertSame(
				"TestMatch3 expected "+TEST[i].answer+" for match3("+TEST[i].question+")",
				match3(factory.parse(TEST[i].question)),
				factory.parse(TEST[i].answer)
				);
		}
  }
  
  public ATerm match3(ATerm t) {
    ATerm res = fail;
    %match(E t) {
      h(l(conc(X1*,x,y,X2*)),y)                -> { return pattern1; }
      h(l(conc(X1*,x,X2*)),l(conc(Y1*,y,X2*))) -> { if(!`X2.isEmpty())
                                                      return pattern2; }
      l(conc(X1*,Y1*,X2*,l(conc(Y1*)),X3*))    -> { return pattern3; }
    }
    return res;
  }
    
	public void test4() {
		TestData[] TEST = new TestData[] {
			new TestData("h(a, l([a,b,f(b),a]))",     "pattern1"), 
			new TestData("h(a, l([a,b,f(a),b]))",     "fail    "),
			new TestData("h(b, l([a,b,f(a),a]))",     "pattern2"),
			new TestData("h(b, l([b,b,f(a),b]))",     "fail    "),
			new TestData("h(c, l([a,f(a),f(a),a]))",  "pattern3"),
			new TestData("h(c, l([a,f(a),f(b),a]))",  "fail    "),
			new TestData("h(d, l([f(a),f(b),f(a)]))", "pattern4"),
		  new TestData("h(d, l([a,f(a),f(b),a]))",  "fail    ")
		};

		for (int i=0; i<TEST.length;i++) {
			assertSame(
				"TestMatch4 expected "+TEST[i].answer+" for match4("+TEST[i].question+")",
				match4(factory.parse(TEST[i].question)),
				factory.parse(TEST[i].answer)
				);
		}
  }
  
  public ATerm match4(ATerm t) {
    ATerm res = fail;
    %match(E t) {
      h(a(), l(conc(X1*,x,f(x),X2*)))       -> { return pattern1; }
      h(b(), l(conc(X1*,x,X2*,f(x),X3*)))   -> { return pattern2; }
      h(c(), l(conc(X1*,x,x@f(_),X2*)))     -> { return pattern3; }
      h(d(), l(conc(X1*,x,X2*,x@f(_),X3*))) -> { return pattern4; }
    }
    return res;
  }
    
  public void test5() {
    assertTrue(pattern1 == match5(factory.parse("h(a, l([f(a),f(b),f(a),f(c)]))")));
    assertTrue(fail == match5(factory.parse("h(a, l([f(a),f(b),f(c)]))")));

    assertTrue(pattern2 == match5(factory.parse("h(b, l([ff(f(a)),f(b),ff(f(a)),f(c)]))")));
    assertTrue(fail == match5(factory.parse("h(b, l([ff(f(a)),f(b),ff(f(b)),f(c)]))")));
  }
  
  public ATerm match5(ATerm t) { 
    ATerm res = fail;
    %match(E t) { 
      h(a(), l(conc(X1*,vx@f(_),X2*,vx@f(_),X3*))) -> { return pattern1; }
      h(b(), l(conc(X1*,ff(vx@f(x)),X2*,ff(vx@f(y)),X3*))) -> { return pattern2; } 
    } 
    return res;
  } 

  public void test6() {
    ATerm res = match6(factory.parse("h(a(),b())"));
    assertTrue("slot s1 of \"h\" is a(), it should match, but got "+res, 
        pattern2 == res);
    res = match6(factory.parse("k(a(),b())"));
    /* -- This test is disabled, until the disjunction bug (TOM-50) is fixed --
    assertTrue("slot s1 of \"k\" is b(), it should not match, but got "+res, 
        fail     == res);
    res = match6(factory.parse("k(b(),a())"));
    assertTrue("slot s1 of \"k\" is a(), it should match, but got "+res, 
        pattern3 == res);
    */
/*
  }

  public ATerm match6(ATerm t) {
    ATerm res = fail;
    %match(E t) { 
      (h|k)[s1=a()] -> { 
        %match(E t) { 
          h[s1=a()]     -> { return pattern2; }
          k[s1=a()]     -> { return pattern3; }
        } 
        return pattern1;
      }
    } 
    return res;
  }
*/
}
