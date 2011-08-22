import aterm.*;
import aterm.pure.*;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;

public class TestMatch {
  private static ATerm ok,fail;
  private static ATerm pattern1,pattern2,pattern3,pattern4,pattern5;
 
  private static ATermFactory factory = SingletonFactory.getInstance();

  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestMatch.class.getName());
  }
	
  @BeforeClass
  public static void setUp() {
    ok   = factory.parse("ok");
    fail = factory.parse("fail");
    pattern1 = factory.parse("pattern1");
    pattern2 = factory.parse("pattern2");
    pattern3 = factory.parse("pattern3");
    pattern4 = factory.parse("pattern4");
    pattern5 = factory.parse("pattern5");
  }

  @AfterClass
  public static void clean() {
    ok   = null;
    fail = null;
    pattern1 = null;
    pattern2 = null;
    pattern3 = null;
    pattern4 = null;
    pattern5 = null;
  }

  %typeterm L {
    implement { ATermList }
    is_sort(t) { $t instanceof ATermList }
    equals(l1,l2)  { $l1.equals($l2) }
  }

  %oplist L conc( E* ) {
    is_fsym(t)     { $t instanceof ATermList }
    make_empty()   { factory.makeList() }
    make_insert(e,l) { ((ATermList)$l).insert((ATerm)$e) }
    get_head(l)    { ((ATermList)$l).getFirst() }
    get_tail(l)    { ((ATermList)$l).getNext() }
    is_empty(l)    { ((ATermList)$l).isEmpty() }
  }

  %typeterm E {
    implement { ATerm }
    is_sort(t) { $t instanceof ATerm }
    equals(t1, t2) { ($t1.equals($t2)) }
  }

  %op E a() {
    is_fsym(t) { ((ATermAppl)$t).getName() == "a" }
  }

  %op E b() {
    is_fsym(t) { ((ATermAppl)$t).getName() == "b" }
  }

  %op E c() {
    is_fsym(t) { ((ATermAppl)$t).getName() == "c" }
  }

  %op E d() {
    is_fsym(t) { ((ATermAppl)$t).getName() == "d" }
  }

  %op E f(s1:E) {
    is_fsym(t) { ((ATermAppl)$t).getName() == "f" }
    get_slot(s1,t) { ((ATermAppl)$t).getArgument(0)  }
  }

  %op E ff(s1:E) {
    is_fsym(t) { ((ATermAppl)$t).getName() == "ff" }
    get_slot(s1,t) { ((ATermAppl)$t).getArgument(0)  }
  }

  %op E g(s1:E, s2:E) {
    is_fsym(t) { ((ATermAppl)$t).getName() == "g" }
    get_slot(s1,t) { ((ATermAppl)$t).getArgument(0)  }
    get_slot(s2,t) { ((ATermAppl)$t).getArgument(1)  }
  }

  %op E l(sl:L) {
    is_fsym(t) { ((ATermAppl)$t).getName() == "l" }
    get_slot(sl,t) { (ATermList) ((ATermAppl)$t).getArgument(0)  }
  }

  %op E h(s1:E,s2:E) {
    is_fsym(t) { ((ATermAppl)$t).getName() == "h" }
    get_slot(s1,t) { ((ATermAppl)$t).getArgument(0)  }
    get_slot(s2,t) { ((ATermAppl)$t).getArgument(1)  }
  }

  %op E k(s2:E,s1:E) {
    is_fsym(t) { ((ATermAppl)$t).getName() == "k" }
    get_slot(s2,t) { ((ATermAppl)$t).getArgument(0)  }
    get_slot(s1,t) { ((ATermAppl)$t).getArgument(1)  }
  }

	public class TestData {
		String question;
		String answer;
		public TestData(String question, String answer) {
			this.question = question;
			this.answer = answer;
		}
	}

	public class TestData2 {
		String question1;
		String question2;
		String answer;
		public TestData2(String question1, String question2, String answer) {
			this.question1 = question1;
			this.question2 = question2;
			this.answer = answer;
		}
	}

  @Test
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
      h(f(f(x)),g(_y,x)) -> { return pattern3; }
      h(x,g(x,x))       -> { return pattern4; }
    }
    return res;
  }

  @Test
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
      g(x,_y), f(x)       -> { return pattern1; }
      g(f(x),f(x)), f(x) -> { return pattern2; }
    }
    return res;
  }

 @Test
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
      h(l(conc(_X1*,_x,y,_X2*)),y)                -> { return pattern1; }
      h(l(conc(_X1*,_x,X2*)),l(conc(_Y1*,_y,X2*))) -> { if(!`X2.isEmpty()) return pattern2; }
      l(conc(_X1*,Y1*,_X2*,l(conc(Y1*)),_X3*))    -> { return pattern3; }
    }
    return res;
  }

  @Test
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
      h(a(), l(conc(_X1*,x,f(x),_X2*)))       -> { return pattern1; }
      h(b(), l(conc(_X1*,x,_X2*,f(x),_X3*)))   -> { return pattern2; }
      h(c(), l(conc(_X1*,x,x@f(_),_X2*)))     -> { return pattern3; }
      h(d(), l(conc(_X1*,x,_X2*,x@f(_),_X3*))) -> { return pattern4; }
    }
    return res;
  }

  @Test
  public void test5() {
    assertTrue(pattern1 == match5(factory.parse("h(a, l([f(a),f(b),f(a),f(c)]))")));
    assertTrue(fail == match5(factory.parse("h(a, l([f(a),f(b),f(c)]))")));

    assertTrue(pattern2 == match5(factory.parse("h(b, l([ff(f(a)),f(b),ff(f(a)),f(c)]))")));
    assertTrue(fail == match5(factory.parse("h(b, l([ff(f(a)),f(b),ff(f(b)),f(c)]))")));
  }

  public ATerm match5(ATerm t) {
    ATerm res = fail;
    %match(E t) {
      h(a(), l(conc(_X1*,vx@f(_),_X2*,vx@f(_),_X3*))) -> { return pattern1; }
      h(b(), l(conc(_X1*,ff(vx@f(_x)),_X2*,ff(vx@f(_y)),_X3*))) -> { return pattern2; }
    }
    return res;
  }

  @Test
  public void test6() {
    ATerm res = match6(factory.parse("h(a(),b())"));
    assertTrue("slot s1 of \"h\" is a(), it should match, but got "+res,
        pattern2 == res);
    res = match6(factory.parse("k(a(),b())"));
    /* -- test for the disjunction bug (TOM-50) --*/
    assertTrue("slot s1 of \"k\" is b(), it should not match, but got "+res,
        fail     == res);
    res = match6(factory.parse("k(b(),a())"));
    assertTrue("slot s1 of \"k\" is a(), it should match, but got "+res,
        pattern3 == res);
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

}
