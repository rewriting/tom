import aterm.*;
import aterm.pure.*;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestList extends TestCase {
  private static ATerm ok,fail;
  private ATermFactory factory;
	private int testNumber;

	public static void main(String[] args) {
		junit.textui.TestRunner.run(new TestSuite(TestList.class));
	}

  public void setUp() {
    factory = new PureFactory(16);
    ok   = factory.parse("ok");
    fail   = factory.parse("fail");
  }
	
	public TestList(String testMethodName) {
		super(testMethodName);
	}

	public TestList(String testMethodName,int testNumber) {
		super(testMethodName);
		this.testNumber = testNumber;
	}

  %typelist L {
    implement { ATermList }
    get_fun_sym(t) { ((t instanceof ATermList)?factory.makeAFun("conc", 1, false):null) }
    cmp_fun_sym(t1,t2) { t1 == t2 }
    equals(l1,l2)  { l1.equals(l2) }
    get_head(l)    { ((ATermList)l).getFirst() }
    get_tail(l)    { ((ATermList)l).getNext() }
    is_empty(l)    { ((ATermList)l).isEmpty() }
  }

  %oplist L conc( E* ) {
    fsym { factory.makeAFun("conc", 1, false) }
    make_empty()  { factory.makeList() }
    make_insert(e,l) { ((ATermList)l).insert((ATerm)e) }
  }
  
  %typeterm E {
    implement { ATerm }
    get_fun_sym(t)      { (((ATermAppl)t).getAFun()) }
    cmp_fun_sym(t1,t2) { t1 == t2 }
    get_subterm(t, n)   { (((ATermAppl)t).getArgument(n)) }
    equals(t1, t2)      { (t1.equals(t2)) }
  }

  %op E a {
    fsym { factory.makeAFun("a", 0, false) }
  }
  
  %op E b {
    fsym { factory.makeAFun("b", 0, false) }
  }

  %op E c {
    fsym { factory.makeAFun("c", 0, false) }
  }

  %op E f(E) {
    fsym { factory.makeAFun("f", 1, false) }
  }

  %op E g(E) {
    fsym { factory.makeAFun("g", 1, false) }
  }

  %op E l(L) {
    fsym { factory.makeAFun("l", 1, false) }
  }

  %op E h(E,E) {
    fsym { factory.makeAFun("h", 2, false) }
  }

	static class TestData {
		String question;
		String answer;
		public TestData(String question, String answer) {
			this.question = question;
			this.answer = answer;
		}
	}

	public static Test suite() {
		TestSuite suite = new TestSuite();
		for (int i = 0; i<TESTS1.length;i++) {
			suite.addTest(new TestList("testMatch1",i));
		}
		for (int i = 0; i<TESTS2.length;i++) {
			suite.addTest(new TestList("testMatch2",i));
		}
		for (int i = 0; i<TESTS3.length;i++) {
			suite.addTest(new TestList("testMatch3",i));
		}
    suite.addTest(new TestList("testMatch4"));
    suite.addTest(new TestList("testMatch5"));
		return suite;
	}

	public void testMatch1() {
		TestData td = TESTS1[this.testNumber];
		assertSame(
               "TestMatch1 : "+this.testNumber+" expected "+td.answer+" for term "+td.question +"",
               match1(factory.parse(td.question)), 
               factory.parse(td.answer));
	}

	private static final TestData[] TESTS1 = new TestData[] {
		new TestData("[f(a)]",                  "pattern1" ),
		new TestData("[a,b,f(a)]",              "pattern2" ),
		new TestData("[a,f(a),b,f(b)]",         "pattern3" ),
		new TestData("[a,f(a),f(b)]",           "pattern3" ),
		new TestData("[f(a),f(b)]",             "pattern3" ),
		new TestData("[a,f(a),b,f(b),c]",       "pattern4" ),
		new TestData("[a,f(a),f(b),c]",         "pattern4" ),     
		new TestData("[f(a),f(b),c]",           "pattern4" ),
		new TestData("[f(b),f(b)]",             "pattern5" ),
		new TestData("[f(b),f(b),c]",           "pattern5" ),
		new TestData("[f(b),a,f(b)]",           "pattern5" ),
		new TestData("[f(b),a,f(b),c]",         "pattern5" ),
		new TestData("[f(c),f(b)]",             "pattern6" ),
		new TestData("[f(a),f(c),a,f(c)]",          "fail" ),
		new TestData("[f(a),f(c),a,f(c),f(c)]", "pattern6" ),
		new TestData("[]",                      "pattern7" )
	};

  public ATerm match1(ATerm t) {
    ATerm res = fail;
    ATermList l = (ATermList)t;
    %match(L l) {
      (f(a()))                        -> { return factory.parse("pattern1"); }
      (X1*,f(a()))                    -> { return factory.parse("pattern2"); }
      conc(X1*,f(a()),X2*,f(b()))     -> { return factory.parse("pattern3"); }
      conc(X1*,f(a()),X2*,f(b()),X3*) -> { return factory.parse("pattern4"); }
      conc(f(b()),X2*,f(b()),X3*)     -> { return factory.parse("pattern5"); }
      conc(X1*,f(c()),f(x),X3*)       -> { return factory.parse("pattern6"); }
      conc()                          -> { return factory.parse("pattern7"); }
    }
    return res;
  }

	public void testMatch2() {
		TestData td = TESTS2[this.testNumber];
		assertSame(
               "TestMatch2 : "+this.testNumber+" expected "+td.answer+" for term "+td.question +"",
               match2(factory.parse(td.question)), 
               factory.parse(td.answer));
	}

	private static final TestData[] TESTS2 = new TestData[] {
		new TestData("h(a,l([f(a)]))"                  , "pattern1"),
		new TestData("h(l([f(a)]),a)"                  , "pattern2"),
		new TestData("h(l([a,b,a,b]),a)"               , "pattern3"),
		new TestData("h(l([a,b,a,b]),l([a,a,b,a]))"    , "fail"    ),     
		new TestData("h(l([a,b,a,b]),l([a,a,b,a,b]))"  , "pattern4"),
		new TestData("l([b,b,l([a]),a,l([a,c]),b,c])"  , "fail"    ),     
		new TestData("l([a,b,l([a]),a,l([a,c]),b,c])"  , "pattern5"),
		new TestData("l([a,b,l([c]),a,b,l([a,c]),b,c])", "fail"    ),         
		new TestData("l([a,b,l([c]),a,c,l([a,c]),b,c])", "pattern5")
	};                                                            
                                                                
  public ATerm match2(ATerm t) {
    ATerm res = fail;
    %match(E t) {
      h(a(),l(conc(f(a()))))                   -> { return factory.parse("pattern1"); }
      h(l(conc(f(a()))),a())                   -> { return factory.parse("pattern2"); }
      h(l(conc(X1*,x,y,X2*)),z)                -> { if(`y==`z) return factory.parse("pattern3"); }
      h(l(conc(X1*,x,X2*)),l(conc(Y1*,y,Y2*))) -> { if(`X2==`Y2 && !`X2.isEmpty())
					return factory.parse("pattern4"); }
      l(conc(X1*,Y1*,X2*,l(conc(Y2*)),X3*))    -> { if(`Y1==`Y2) return factory.parse("pattern5"); }
    }
    return res;
  }

	public void testMatch3() {
		TestData td = TESTS3[this.testNumber];
		assertSame(
               "TestMatch3 : "+this.testNumber+" expected "+td.answer+" for term "+td.question +"",
               match3(factory.parse(td.question)), 
               factory.parse(td.answer));
	}

	private static final TestData[] TESTS3 = new TestData[] {
    new TestData("l([f(a)])"                     , "pattern1"),
		new TestData("l([f(a),f(b),f(a),f(c)])"      , "pattern2"),
    new TestData("l([g(f(a)),f(b),g(f(a)),f(c)])", "pattern3"),
	};                                                            

  public ATerm match3(ATerm t) {
    ATerm res = fail;
    %match(E t) {
      l(vl@conc(x))                        -> { if(`vl==factory.parse("[f(a)]"))
					return factory.parse("pattern1"); }
      l(conc(X1*,vx@f(x),X2*,vy@f(y),X3*)) -> { if(`vx==`vy) return factory.parse("pattern2"); }
      l(conc(X1*,g(vx@f(x)),X2*,g(vy@f(y)),X3*)) -> { if(`vx==`vy) return factory.parse("pattern3"); }
    }
    return res;
  }

	public void testMatch4() {
    int nbSol = 0;
    ATerm l = factory.parse("[a,b]");
		%match(L l) {
      conc(X1*,X2*,X3*) -> {
        nbSol++;
				//System.out.println("X1 = " + `X1* + " X2 = " + `X2*+ " X3 = " + `X3*);
      }
    }

    assertTrue("TestMatch4",nbSol==6);
	}

	public void testMatch5() {
    int nbSol = 0;
    ATerm l = factory.parse("[l([a,b]),a,b]");
		%match(L l) {
      conc(l(conc(R*,T*)),X1*,u,X2*) -> {
        nbSol++;
				//System.out.println("R = " + `R* + " T = " + `T*+" X1 = " + `X1* + " X2 = " + `X2*+ " u = " + `u);
      }
    }

    assertTrue("TestMatch5",nbSol==6);
	}

  
}
