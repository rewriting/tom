import java.util.*;
import aterm.*;
import aterm.pure.*;

public class TestList {
  private static ATerm ok,fail;
  private ATermFactory factory;
  
  public static void main(String args[]) {
    TestList test = new TestList();
    test.setUp();
    test.test1();
    test.test2();
    test.test3();
  }

  public void setUp() {
    factory = new PureFactory(16);
    ok   = factory.parse("ok");
    fail = factory.parse("fail");
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
    make_add(e,l) { ((ATermList)l).insert((ATerm)e) }
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
  
  public void test1() {
    assertTrue(factory.parse("pattern1") == match1(factory.parse("[f(a)]")));
    assertTrue(factory.parse("pattern2") == match1(factory.parse("[a,b,f(a)]")));
    assertTrue(factory.parse("pattern3") == match1(factory.parse("[a,f(a),b,f(b)]")));
    assertTrue(factory.parse("pattern3") == match1(factory.parse("[a,f(a),f(b)]")));
    assertTrue(factory.parse("pattern3") == match1(factory.parse("[f(a),f(b)]")));
    assertTrue(factory.parse("pattern4") == match1(factory.parse("[a,f(a),b,f(b),c]")));
    assertTrue(factory.parse("pattern4") == match1(factory.parse("[a,f(a),f(b),c]")));
    assertTrue(factory.parse("pattern4") == match1(factory.parse("[f(a),f(b),c]")));
    assertTrue(factory.parse("pattern5") == match1(factory.parse("[f(b),f(b)]")));
    assertTrue(factory.parse("pattern5") == match1(factory.parse("[f(b),f(b),c]")));
    assertTrue(factory.parse("pattern5") == match1(factory.parse("[f(b),a,f(b)]")));
    assertTrue(factory.parse("pattern5") == match1(factory.parse("[f(b),a,f(b),c]")));
    assertTrue(factory.parse("pattern6") == match1(factory.parse("[f(c),f(b)]")));
    assertTrue(fail                      == match1(factory.parse("[f(a),f(c),a,f(c)]")));
    assertTrue(factory.parse("pattern6") == match1(factory.parse("[f(a),f(c),a,f(c),f(c)]")));
    assertTrue(factory.parse("pattern7") == match1(factory.parse("[]")));
  }

  public ATerm match1(ATerm t) {
    ATerm res = fail;
    ATermList l = (ATermList)t;
    %match(L l) {
      conc(f(a))                  -> { return factory.parse("pattern1"); }
      conc(X1*,f(a))              -> { return factory.parse("pattern2"); }
      conc(X1*,f(a),X2*,f(b))     -> { return factory.parse("pattern3"); }
      conc(X1*,f(a),X2*,f(b),X3*) -> { return factory.parse("pattern4"); }
      conc(f(b),X2*,f(b),X3*)     -> { return factory.parse("pattern5"); }
      conc(X1*,f(c),f(x),X3*)     -> { return factory.parse("pattern6"); }
      conc()                      -> { return factory.parse("pattern7"); }
    }
    return res;
  }

  public void test2() {
    assertTrue(factory.parse("pattern1") == match2(factory.parse("h(a,l([f(a)]))")));
    assertTrue(factory.parse("pattern2") == match2(factory.parse("h(l([f(a)]),a)")));
    assertTrue(factory.parse("pattern3") == match2(factory.parse("h(l([a,b,a,b]),a)")));
    assertTrue(fail                      == match2(factory.parse("h(l([a,b,a,b]),l([a,a,b,a]))")));
    assertTrue(factory.parse("pattern4") == match2(factory.parse("h(l([a,b,a,b]),l([a,a,b,a,b]))")));
    assertTrue(fail                      == match2(factory.parse("l([b,b,l([a]),a,l([a,c]),b,c])")));
    assertTrue(factory.parse("pattern5") == match2(factory.parse("l([a,b,l([a]),a,l([a,c]),b,c])")));
    assertTrue(fail                      == match2(factory.parse("l([a,b,l([c]),a,b,l([a,c]),b,c])")));
    assertTrue(factory.parse("pattern5") == match2(factory.parse("l([a,b,l([c]),a,c,l([a,c]),b,c])")));
  }

  public ATerm match2(ATerm t) {
    ATerm res = fail;
    %match(E t) {
      h(a,l(conc(f(a))))                       -> { return factory.parse("pattern1"); }
      h(l(conc(f(a))),a)                       -> { return factory.parse("pattern2"); }
      h(l(conc(X1*,x,y,X2*)),z)                -> { if(y==z) return factory.parse("pattern3"); }
      h(l(conc(X1*,x,X2*)),l(conc(Y1*,y,Y2*))) -> { if(X2==Y2 && !X2.isEmpty())
                                                      return factory.parse("pattern4"); }
      l(conc(X1*,Y1*,X2*,l(conc(Y2*)),X3*))    -> { if(Y1==Y2) return factory.parse("pattern5"); }
    }
    return res;
  }

  public void test3() {
    assertTrue(factory.parse("pattern1") == match3(factory.parse("l([f(a)])")));
    assertTrue(factory.parse("pattern2") == match3(factory.parse("l([f(a),f(b),f(a),f(c)])")));
    assertTrue(factory.parse("pattern3") == match3(factory.parse("l([g(f(a)),f(b),g(f(a)),f(c)])")));
  }

  public ATerm match3(ATerm t) {
    ATerm res = fail;
    %match(E t) {
      l(vl@conc(x))                        -> { if(vl==factory.parse("[f(a)]"))
                                                 return factory.parse("pattern1"); }
      l(conc(X1*,vx@f(x),X2*,vy@f(y),X3*)) -> { if(vx==vy) return factory.parse("pattern2"); }
      l(conc(X1*,g(vx@f(x)),X2*,g(vy@f(y)),X3*)) -> { if(vx==vy) return factory.parse("pattern3"); }
    }
    return res;
  }

  static void  assertTrue(boolean condition) {
    if(!condition) {
      throw new RuntimeException("assertion failed.");
    }
  }
  
}
