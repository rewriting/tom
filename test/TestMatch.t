import aterm.*;
import aterm.pure.*;

public class TestMatch {
  private static ATerm ok,fail;
  private static ATerm pattern1,pattern2,pattern3,pattern4,pattern5;
 
  private ATermFactory factory;
  
  public static void main(String args[]) {
    TestMatch test = new TestMatch();
    test.setUp();
    test.test1();
    test.test2();
    test.test3();
    test.test4();
    test.test5();
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

  %op E d {
    fsym { factory.makeAFun("d", 0, false) }
  }

  %op E f(E) {
    fsym { factory.makeAFun("f", 1, false) }
  }

  %op E ff(E) {
    fsym { factory.makeAFun("ff", 1, false) }
  }

  %op E g(E,E) {
    fsym { factory.makeAFun("g", 2, false) }
  }

  %op E l(L) {
    fsym { factory.makeAFun("l", 1, false) }
  }

  %op E h(E,E) {
    fsym { factory.makeAFun("h", 2, false) }
  }
  
  public void test1() {
    assertTrue(pattern1 == match1(factory.parse("g(a,a)")));
    assertTrue(fail     == match1(factory.parse("g(a,b)")));

    assertTrue(pattern2 == match1(factory.parse("h(f(b),f(b))")));
    assertTrue(fail     == match1(factory.parse("h(f(a),f(b))")));

    assertTrue(pattern3 == match1(factory.parse("h(f(f(g(a,b))),g(b,g(a,b)))")));
    assertTrue(fail     == match1(factory.parse("h(f(f(g(a,b))),g(b,g(b,b)))")));
    assertTrue(pattern4 == match1(factory.parse("h(b,g(b,b))")));
    assertTrue(fail     == match1(factory.parse("h(a,g(b,b))")));
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
    assertTrue(pattern1 == match2(factory.parse("g(a,b)"),
                                  factory.parse("f(a)")));
    assertTrue(fail     == match2(factory.parse("g(b,b)"),
                                  factory.parse("f(a)")));

    assertTrue(pattern2 == match2(factory.parse("g(f(a),f(a))"),
                                  factory.parse("f(a)")));
    assertTrue(fail     == match2(factory.parse("g(f(b),f(a))"),
                                  factory.parse("f(a)")));

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
    assertTrue(pattern1 == match3(factory.parse("h(l([a,b,a,b]),a)")));
    assertTrue(fail     == match3(factory.parse("h(l([a,b,a,b]),l([a,a,b,a]))")));
    assertTrue(pattern2 == match3(factory.parse("h(l([a,b,a,b]),l([a,a,b,a,b]))")));
    assertTrue(fail     == match3(factory.parse("l([b,b,l([a]),a,l([a,c]),b,c])")));
    assertTrue(pattern3 == match3(factory.parse("l([a,b,l([a]),a,l([a,c]),b,c])")));
    assertTrue(fail     == match3(factory.parse("l([a,b,l([c]),a,b,l([a,c]),b,c])")));
    assertTrue(pattern3 == match3(factory.parse("l([a,b,l([c]),a,c,l([a,c]),b,c])")));
  }
  
  public ATerm match3(ATerm t) {
    ATerm res = fail;
    %match(E t) {
      h(l(conc(X1*,x,y,X2*)),y)                -> { return pattern1; }
      h(l(conc(X1*,x,X2*)),l(conc(Y1*,y,X2*))) -> { if(!X2.isEmpty())
                                                      return pattern2; }
      l(conc(X1*,Y1*,X2*,l(conc(Y1*)),X3*))    -> { return pattern3; }
    }
    return res;
  }
    
  public void test4() {
    assertTrue(pattern1 == match4(factory.parse("h(a, l([a,b,f(b),a]))")));
    assertTrue(fail     == match4(factory.parse("h(a, l([a,b,f(a),b]))")));

    assertTrue(pattern2 == match4(factory.parse("h(b, l([a,b,f(a),a]))")));
    assertTrue(fail     == match4(factory.parse("h(b, l([b,b,f(a),b]))")));

    assertTrue(pattern3 == match4(factory.parse("h(c, l([a,f(a),f(a),a]))")));
    assertTrue(fail     == match4(factory.parse("h(c, l([a,f(a),f(b),a]))")));

    assertTrue(pattern4 == match4(factory.parse("h(d, l([f(a),f(b),f(a)]))")));
    assertTrue(fail     == match4(factory.parse("h(d, l([a,f(a),f(b),a]))")));
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

  static void  assertTrue(boolean condition) {
    if(!condition) {
      throw new RuntimeException("assertion failed.");
    }
  }

}
