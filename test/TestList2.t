import aterm.*;
import junit.framework.Test;
import junit.framework.TestSuite;
import aterm.pure.*;

public class TestList2 extends GenericTest{
  private static ATerm ok,fail;
  private static final ATermFactory factory = new PureFactory(16);
  private static final Object[][] TESTS = new Object[][]{
    {"match1",new Integer(1),"aterm.ATerm",factory.parse("[f(a)]"),factory.parse("pattern1")},
      {"match1",new Integer(1),"aterm.ATerm",factory.parse("[a,b,f(a)]"),factory.parse("pattern2")} ,
      {"match1",new Integer(1),"aterm.ATerm",factory.parse("[a,f(a),b,f(b)]"),factory.parse("pattern3")} ,
      {"match1",new Integer(1),"aterm.ATerm",factory.parse("[a,f(a),f(b)]"),factory.parse("pattern3")} ,
      {"match1",new Integer(1),"aterm.ATerm",factory.parse("[f(a),f(b)]"),factory.parse("pattern3")} ,
      {"match1",new Integer(1),"aterm.ATerm",factory.parse("[a,f(a),b,f(b),c]"),factory.parse("pattern4")} ,
      {"match1",new Integer(1),"aterm.ATerm",factory.parse("[a,f(a),f(b),c]"),factory.parse("pattern4")} ,     
      {"match1",new Integer(1),"aterm.ATerm",factory.parse("[f(a),f(b),c]"),factory.parse("pattern4")} ,
      {"match1",new Integer(1),"aterm.ATerm",factory.parse("[f(b),f(b)]"),factory.parse("pattern5")} ,
      {"match1",new Integer(1),"aterm.ATerm",factory.parse("[f(b),f(b),c]"),factory.parse("pattern5")} ,
      {"match1",new Integer(1),"aterm.ATerm",factory.parse("[f(b),a,f(b)]"),factory.parse("pattern5")} ,
      {"match1",new Integer(1),"aterm.ATerm",factory.parse("[f(b),a,f(b),c]"),factory.parse("pattern5")} ,
      {"match1",new Integer(1),"aterm.ATerm",factory.parse("[f(c),f(b)]"),factory.parse("pattern6")} ,
      {"match1",new Integer(1),"aterm.ATerm",factory.parse("[f(a),f(c),a,f(c)]"),factory.parse("fail")} ,
      {"match1",new Integer(1),"aterm.ATerm",factory.parse("[f(a),f(c),a,f(c),f(c)]"),factory.parse("pattern6")} ,
      {"match1",new Integer(1),"aterm.ATerm",factory.parse("[]"),factory.parse("pattern7")}, 
      {"match2",new Integer(1),"aterm.ATerm",factory.parse("h(a,l([f(a)]))"),factory.parse("pattern1")},
      {"match2",new Integer(1),"aterm.ATerm",factory.parse("h(l([f(a)]),a)"),factory.parse("pattern2")},
      {"match2",new Integer(1),"aterm.ATerm",factory.parse("h(l([a,b,a,b]),a)"),factory.parse("pattern3")},
      {"match2",new Integer(1),"aterm.ATerm",factory.parse("h(l([a,b,a,b]),l([a,a,b,a]))"),factory.parse("fail"  )  },     
      {"match2",new Integer(1),"aterm.ATerm",factory.parse("h(l([a,b,a,b]),l([a,a,b,a,b]))"),factory.parse("pattern4")},
      {"match2",new Integer(1),"aterm.ATerm",factory.parse("l([b,b,l([a]),a,l([a,c]),b,c])"),factory.parse("fail" )   },     
      {"match2",new Integer(1),"aterm.ATerm",factory.parse("l([a,b,l([a]),a,l([a,c]),b,c])"),factory.parse("pattern5")},
      {"match2",new Integer(1),"aterm.ATerm",factory.parse("l([a,b,l([c]),a,b,l([a,c]),b,c])"),factory.parse("fail"  )  },         
      {"match2",new Integer(1),"aterm.ATerm",factory.parse("l([a,b,l([c]),a,c,l([a,c]),b,c])"),factory.parse("pattern5")},
      {"match3",new Integer(1),"aterm.ATerm",factory.parse("l([f(a)])"),factory.parse("pattern1")},
      {"match3",new Integer(1),"aterm.ATerm",factory.parse("l([f(a),f(b),f(a),f(c)])"),factory.parse("pattern2")},
      {"match3",new Integer(1),"aterm.ATerm",factory.parse("l([g(f(a)),f(b),g(f(a)),f(c)])"),factory.parse("pattern3")},
      {"match4",new Integer(1),"aterm.ATerm",factory.parse("[a,b]"),new Integer(6)},
      {"match5",new Integer(1),"aterm.ATerm",factory.parse("[l([a,b]),a,b]"),new Integer(6)},
      {"match6",new Integer(1),"aterm.ATerm",factory.parse("[]"), factory.parse("fail")},
      {"match6",new Integer(1),"aterm.ATerm",factory.parse("[f(a)]"),factory.parse("ok")},
      {"match6",new Integer(1),"aterm.ATerm",factory.parse("[f(a),f(b),f(a),f(c)]"),factory.parse("ok")},
      {"match6",new Integer(1),"aterm.ATerm",factory.parse("[g(f(a)),f(b),g(f(a)),f(c)]"),factory.parse("ok")}
  };                                                      
  public TestList2(int testNumber){
    super(testNumber,TESTS);
  }

  public void setUp() {
    ok      = factory.parse("ok");
    fail    = factory.parse("fail");
  }
  
  public static Test suite() {
    TestSuite suite = new TestSuite();
    for (int i = 0; i<TESTS.length;i++) {
      suite.addTest(new TestList2(i));
    }
    return suite;
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }
  

  %typeterm L {
    implement { ATermList }
    equals(l1,l2)  { l1.equals(l2) }
  }

  %oplist L conc( E* ) {
    is_fsym(t) { t instanceof ATermList }
    get_head(l)    { ((ATermList)l).getFirst() }
    get_tail(l)    { ((ATermList)l).getNext() }
    is_empty(l)    { ((ATermList)l).isEmpty() }
    make_empty()   { factory.makeList() }
    make_insert(e,l) { ((ATermList)l).insert((ATerm)e) }
  }

  %typeterm E {
    implement { ATerm }
    equals(t1, t2) { (t1.equals(t2)) }
  }

  %op E a() {
    is_fsym(t) { ((ATermAppl)t).getName() == "a" }
  }

  %op E b() {
    is_fsym(t) { ((ATermAppl)t).getName() == "b" }
  }

  %op E c() {
    is_fsym(t) { ((ATermAppl)t).getName() == "c" }
  }

  %op E f(s1:E) {
    is_fsym(t) { ((ATermAppl)t).getName() == "f" }
    get_slot(s1,t) { ((ATermAppl)t).getArgument(0)  }
  }

  %op E g(s1:E) {
    is_fsym(t) { ((ATermAppl)t).getName() == "g" }
    get_slot(s1,t) { ((ATermAppl)t).getArgument(0)  }
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


  public int  match4(ATerm l) {
    int nbSol = 0;
    %match(L l) {
      conc(X1*,X2*,X3*) -> {
        nbSol++;
      }
    }
    return nbSol; 
  }

  
  public int match5(ATerm l) {
    int nbSol = 0;
    %match(L l) {
      conc(l(conc(R*,T*)),X1*,u,X2*) -> {
        nbSol++;
      }
    }
    return nbSol;
  }


  
  public ATerm match6(ATerm l) {
    %match(L l) {
      conc(_,_*) -> {
        return factory.parse("ok");
      }
    }
    return factory.parse("fail");
  }


}
