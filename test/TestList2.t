import aterm.*;
import aterm.pure.*;

import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import java.util.Collection;
import java.util.Arrays;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Suite.class)
@SuiteClasses({
    TestList2.Match1.class,
    TestList2.Match2.class,
    TestList2.Match3.class,
    TestList2.Match45.class,
    TestList2.Match6.class
    })
public class TestList2 {
  private static ATerm ok,fail;
  private static final ATermFactory factory = new PureFactory(16);

  @BeforeClass
  public static void setUp() {
    ok      = factory.parse("ok");
    fail    = factory.parse("fail");
  }

  @AfterClass
  public static void tearDown() {
    ok = null;
    fail = null;
  }

  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestList2.class.getName());
  }
  
  %typeterm L {
    implement { ATermList }
    is_sort(t) { $t instanceof ATermList }
    equals(l1,l2)  { $l1.equals($l2) }
  }

  %oplist L conc( E* ) {
    is_fsym(t) { $t instanceof ATermList }
    get_head(l)    { ((ATermList)$l).getFirst() }
    get_tail(l)    { ((ATermList)$l).getNext() }
    is_empty(l)    { ((ATermList)$l).isEmpty() }
    make_empty()   { factory.makeList() }
    make_insert(e,l) { ((ATermList)$l).insert((ATerm)$e) }
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

  %op E f(s1:E) {
    is_fsym(t) { ((ATermAppl)$t).getName() == "f" }
    get_slot(s1,t) { ((ATermAppl)$t).getArgument(0)  }
  }

  %op E g(s1:E) {
    is_fsym(t) { ((ATermAppl)$t).getName() == "g" }
    get_slot(s1,t) { ((ATermAppl)$t).getArgument(0)  }
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

  @RunWith(Parameterized.class)
  public static class Match1 {
    String question;
    String answer;

    public Match1(String question, String answer) {
      this.question = question;
      this.answer = answer;
    }

    @Test
    public void testMatch1() {
      assertSame(
        "TestMatch1 : expected "+answer+" for term "+question +"",
        match1(factory.parse(question)),
        factory.parse(answer));
    }

    @Parameters
    public static Collection<Object[]> data() {
      return Arrays.asList(new Object[][] {
          { "[f(a)]",                  "pattern1" },
          { "[a,b,f(a)]",              "pattern2" },
          { "[a,f(a),b,f(b)]",         "pattern3" },
          { "[a,f(a),f(b)]",           "pattern3" },
          { "[f(a),f(b)]",             "pattern3" },
          { "[a,f(a),b,f(b),c]",       "pattern4" },
          { "[a,f(a),f(b),c]",         "pattern4" },     
          { "[f(a),f(b),c]",           "pattern4" },
          { "[f(b),f(b)]",             "pattern5" },
          { "[f(b),f(b),c]",           "pattern5" },
          { "[f(b),a,f(b)]",           "pattern5" },
          { "[f(b),a,f(b),c]",         "pattern5" },
          { "[f(c),f(b)]",             "pattern6" },
          { "[f(a),f(c),a,f(c)]",      "fail"     },
          { "[f(a),f(c),a,f(c),f(c)]", "pattern6" },
          { "[]",                      "pattern7" }
      });
    }

    public ATerm match1(ATerm t) {
      ATerm res = fail;
      ATermList l = (ATermList) t;
      %match(L l) {
        conc(f(a()))                           -> { return factory.parse("pattern1"); }
        conc(_X1*,f(a()))                      -> { return factory.parse("pattern2"); }
        conc(_X1*,f(a()),_X2*,f(b()))      -> { return factory.parse("pattern3"); }
        conc(_X1*,f(a()),_X2*,f(b()),_X3*) -> { return factory.parse("pattern4"); }
        conc(f(b()),_X2*,f(b()),_X3*)      -> { return factory.parse("pattern5"); }
        conc(_X1*,f(c()),f(_x),_X3*)       -> { return factory.parse("pattern6"); }
        conc()                             -> { return factory.parse("pattern7"); }
      }
      return res;
    }
  }

  @RunWith(Parameterized.class)
  public static class Match2 {
    String question;
    String answer;

    public Match2(String question, String answer) {
      this.question = question;
      this.answer = answer;
    }

    @Test
    public void testMatch2() {
      assertSame(
        "TestMatch2 : expected "+answer+" for term "+question +"",
        match2(factory.parse(question)),
        factory.parse(answer));
    }

    @Parameters
    public static Collection<Object[]> data() {
      return Arrays.asList(new Object[][] {
          { "h(a,l([f(a)]))",                   "pattern1" },
          { "h(l([f(a)]),a)",                   "pattern2" },
          { "h(l([a,b,a,b]),a)",                "pattern3" },
          { "h(l([a,b,a,b]),l([a,a,b,a]))",         "fail" },     
          { "h(l([a,b,a,b]),l([a,a,b,a,b]))",   "pattern4" },
          { "l([b,b,l([a]),a,l([a,c]),b,c])",       "fail" },     
          { "l([a,b,l([a]),a,l([a,c]),b,c])",   "pattern5" },
          { "l([a,b,l([c]),a,b,l([a,c]),b,c])",     "fail" },         
          { "l([a,b,l([c]),a,c,l([a,c]),b,c])", "pattern5" }
      });
    }

    public ATerm match2(ATerm t) {
      ATerm res = fail;
      %match(E t) {
        h(a(),l(conc(f(a()))))                       -> { return factory.parse("pattern1"); }
        h(l(conc(f(a()))),a())                       -> { return factory.parse("pattern2"); }
        h(l(conc(_X1*,_x,y,_X2*)),z)                 -> { if(`y==`z) return factory.parse("pattern3"); }
        h(l(conc(_X1*,_x,X2*)),l(conc(_Y1*,_y,Y2*))) -> { if(`X2==`Y2 && !`X2.isEmpty()) return factory.parse("pattern4"); }
        l(conc(_X1*,Y1*,_X2*,l(conc(Y2*)),_X3*))     -> { if(`Y1==`Y2) return factory.parse("pattern5"); }
      }
      return res;
    }
  }

  @RunWith(Parameterized.class)
  public static class Match3 {
    String question;
    String answer;

    public Match3(String question, String answer) {
      this.question = question;
      this.answer = answer;
    }

    @Test
    public void testMatch3() {
      assertSame(
        "TestMatch3 : expected "+answer+" for term "+question +"",
        match3(factory.parse(question)),
        factory.parse(answer));
    }

    @Parameters
    public static Collection<Object[]> data() {
      return Arrays.asList(new Object[][] {
          { "l([f(a)])",                      "pattern1" },
          { "l([f(a),f(b),f(a),f(c)])",       "pattern2" },
          { "l([g(f(a)),f(b),g(f(a)),f(c)])", "pattern3" }
      });
    }

    public ATerm match3(ATerm t) {
      ATerm res = fail;
      %match(E t) {
        l(vl@conc(_x)) -> { if(`vl==factory.parse("[f(a)]"))
          return factory.parse("pattern1"); }
        l(conc(_X1*,vx@f(_x),_X2*,vy@f(_y),_X3*)) -> { if(`vx==`vy) return factory.parse("pattern2"); }
        l(conc(_X1*,g(vx@f(_x)),_X2*,g(vy@f(_y)),_X3*)) -> { if(`vx==`vy) return factory.parse("pattern3"); }
      }
      return res;
    }
  }

  public static class Match45 {
    @Test
    public void match4() {
      ATermList l = (ATermList) factory.parse("[a,b]");
      int nbSol = 0;
      %match(L l) {
        conc(_X1*,_X2*,_X3*) -> {
          nbSol++;
        }
      }
      assertEquals(nbSol, 6);
    }

    @Test
    public void match5() {
      ATermList l = (ATermList) factory.parse("[l([a,b]),a,b]");
      int nbSol = 0;
      %match(L l) {
        conc(l(conc(_R*,_T*)),_X1*,_u,_X2*) -> {
          nbSol++;
        }
      }
      assertEquals(nbSol, 6);
    }
  }

  @RunWith(Parameterized.class)
  public static class Match6 {
    String question;
    String answer;

    public Match6(String question, String answer) {
      this.question = question;
      this.answer = answer;
    }

    @Test
    public void testMatch6() {
      assertSame(
        "TestMatch6 : expected "+answer+" for term "+question +"",
        match6(factory.parse(question)),
        factory.parse(answer));
    }

    @Parameters
    public static Collection<Object[]> data() {
      return Arrays.asList(new Object[][] {
          { "[]",                          "fail" },
          { "[f(a)]",                        "ok" },
          { "[f(a),f(b),f(a),f(c)]",         "ok" },
          { "[g(f(a)),f(b),g(f(a)),f(c)]",   "ok" }
      });
    }

    public ATerm match6(ATerm l) {
      ATermList ll = (ATermList) l;
      %match(L ll) {
        conc(_,_*) -> {
          return factory.parse("ok");
        }
      }
      return factory.parse("fail");
    }
  }
}
