import aterm.*;
import aterm.pure.*;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestWhen extends TestCase {

  %include { boolean.tom }

  private ATermFactory factory;

  %typeterm term {
    implement { ATerm }
    get_fun_sym(t)      { (((ATermAppl)t).getAFun()) }
    cmp_fun_sym(t1,t2)  { t1 == t2 }
    get_subterm(t, n)   { (((ATermAppl)t).getArgument(n)) }
    equals(t1, t2)      { t1 == t2}
  }

  %op term a {
    fsym { factory.makeAFun("a", 0, false) }
    make { factory.makeAppl(factory.makeAFun("a", 0, false)) }
  }

  %op term b {
    fsym { factory.makeAFun("b", 0, false) }
    make { factory.makeAppl(factory.makeAFun("b", 0, false)) }
  }

  %op term f(term) {
    fsym { factory.makeAFun("f" , 1, false) }
    make(t) { factory.makeAppl(factory.makeAFun("f", 1, false),t) }
  }

  %op boolean g(term) {
    fsym { factory.makeAFun("g" , 1, false) }
    make(t) { g(t) }
  }

	public static void main(String[] args) {
		junit.textui.TestRunner.run(new TestSuite(TestWhen.class));
	}

  public void setUp() {
    this.factory = new PureFactory(16);
  }

  public void testMatch() {
    assertTrue("Testing macth with when (1 arg) : ", 
								match(`f(a())) == 5 );
	}

  public void testMatch2() {
    assertTrue("Testing macth with when (2 args) : ", 
								match2(`f(a()),`f(a())) == 2 );
	}

  public int match(ATerm t) {
    int result = 0;
    %match(term t) { 
      /* ok */
      f(x) when g(x)    -> { result++; System.out.println("cas 1"); }

      f(x@_) when g(x)    -> { result++; System.out.println("cas 1bis"); }
      /* warnings */
      f(x) when g(h(x)) -> { result++; System.out.println("cas 2"); }
      f(x) when g(a) -> { result++; System.out.println("cas 3"); }
      f(x) when g(k()) -> { result++; System.out.println("cas 4"); }
      f(x) when nol() -> { result++; System.out.println("cas 5"); }

      /* errors */
      //f(x) when g(h(y)) -> { System.out.println("cas 6"); }
      //f(x) when f(x) -> { System.out.println("cas 7"); }
      //f(x) when g(x,x) -> { System.out.println("cas 8"); }
      //f(x) when h(x,g(x,x)) -> { System.out.println("cas 9"); }
    } 
    return result;
  }

  public int match2(ATerm t1,ATerm t2) {
    int result = 0;
    %match(term t1,term t2) { 
      /* ok */
      f(x),f(x) when g(x),g(x)    -> { result++; System.out.println("cas 2 1"); }
      f(x),f(x@y) when g(x),g(y)    -> { result++; System.out.println("cas 2 2"); }
      /* pas ok */
      f(x),f(y) when no(x,y) -> { result++; System.out.println("cas bug"); }

    } 
    return result;
  }

  boolean g(ATerm t) {
    %match(term t) { 
      a() -> { return `true(); }
      b() -> { return `true(); }
      _   -> { return `false(); }
    } 
  }

  ATerm h(ATerm x) {
    return `b();
  } 

  ATerm k() {
    return `b();
  } 

  boolean l() {
    return true;
  } 

  boolean nol() {
    return false;
  } 

  boolean no(ATerm t1,ATerm t2) {
    return false;
  } 
    
  %rule {
    /* ok */
    f(x) -> f(x) if x == h(x)
    /* warning */
    f(x) -> h(f(x)) if x == h(x)
    /* error */
    //f(x) -> f(x,x) if x == h(x)
    //f(x) -> h(h(f(x)),h(f(x,y))) if x == h(x)
  }
  
}
