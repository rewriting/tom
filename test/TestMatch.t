import aterm.*;
import aterm.pure.*;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestMatch extends TestCase {
  private static ATerm ok,fail;
  private static ATerm pattern1,pattern2,pattern3,pattern4,pattern5;
 
  private ATermFactory factory;

	public static void main(String[] args) {
		junit.textui.TestRunner.run(new TestSuite(TestMatch.class));
	}
	
  public void setUp() {
    factory = new PureFactory(16);
    ok   = factory.parse("ok");
    fail = factory.parse("fail");
    pattern1 = factory.parse("pattern1");
    pattern2 = factory.parse("pattern2");
    pattern3 = factory.parse("pattern3");
    pattern4 = factory.parse("pattern4");
    pattern5 = factory.parse("pattern5");
  }

  %typeterm L {
    implement { ATermList }
    equals(l1,l2)  { l1.equals(l2) }
  }

  %oplist L conc( E* ) {
    is_fsym(t) { t instanceof ATermList }
    make_empty()  { factory.makeList() }
    make_insert(e,l) { ((ATermList)l).insert((ATerm)e) }
    get_head(l)    { ((ATermList)l).getFirst() }
    get_tail(l)    { ((ATermList)l).getNext() }
    is_empty(l)    { ((ATermList)l).isEmpty() }
  }
  
  %typeterm E {
    implement { ATerm }
    equals(t1, t2)      { (t1.equals(t2)) }
  }

  %op E a {
    is_fsym(t) { ((ATermAppl)t).getName() == "a" }
  }
  
  %op E b {
    is_fsym(t) { ((ATermAppl)t).getName() == "b" }
  }

  %op E c {
    is_fsym(t) { ((ATermAppl)t).getName() == "c" }
  }

  %op E d {
    is_fsym(t) { ((ATermAppl)t).getName() == "d" }
  }

  %op E f(s1:E) {
    is_fsym(t) { ((ATermAppl)t).getName() == "f" }
    get_slot(s1,t) { ((ATermAppl)t).getArgument(0)  }
  }

  %op E ff(s1:E) {
    is_fsym(t) { ((ATermAppl)t).getName() == "ff" }
    get_slot(s1,t) { ((ATermAppl)t).getArgument(0)  }
  }

  %op E g(s1:E, s2:E) {
    is_fsym(t) { ((ATermAppl)t).getName() == "g" }
    get_slot(s1,t) { ((ATermAppl)t).getArgument(0)  }
    get_slot(s2,t) { ((ATermAppl)t).getArgument(1)  }
  }

  %op E l(s1:L) {
    is_fsym(t) { ((ATermAppl)t).getName() == "l" }
    get_slot(s1,t) { (ATermList) ((ATermAppl)t).getArgument(0)  }
  }

  %op E h(s1:E,s2:E) {
    is_fsym(t) { ((ATermAppl)t).getName() == "h" }
    get_slot(s1,t) { ((ATermAppl)t).getArgument(0)  }
    get_slot(s2,t) { ((ATermAppl)t).getArgument(1)  }
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

		for (int i=0; i<TEST.length;i++) {
			assertSame(
				"TestMatch1 expected "+TEST[i].answer+" for match1("+TEST[i].question+")",
				match1(factory.parse(TEST[i].question)),
				factory.parse(TEST[i].answer)
				);
		}
  }

  public ATerm match1(ATerm t) {
    ATerm res = fail;
    %match(E t) {
      g(x,x)            -> { return pattern1; }
      h(f(x),f(x))      -> { return pattern2; }
      h(f(f(x)),g(y,x)) -> { return pattern3; }
      h(x,g(x,x))       -> { return pattern4; }
    }
    return res;
  }
      
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

}
