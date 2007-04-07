import aterm.*;
import aterm.pure.*;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestWhen extends TestCase {

  %include { boolean.tom }

  private ATermFactory factory;

  %typeterm term {
    implement { ATerm }
    is_sort(t) { t instanceof ATerm }
    equals(t1, t2) { t1 == t2}
  }

  %op term a() {
    is_fsym(t) { ((ATermAppl)t).getName() == "a" }
    make { factory.makeAppl(factory.makeAFun("a", 0, false)) }
  }

  %op term b() {
    is_fsym(t) { ((ATermAppl)t).getName() == "b" }
    make { factory.makeAppl(factory.makeAFun("b", 0, false)) }
  }

  %op term f(s1:term) {
    is_fsym(t) { ((ATermAppl)t).getName() == "f" }
    get_slot(s1,t) { ((ATermAppl)t).getArgument(0)  }
    make(t) { factory.makeAppl(factory.makeAFun("f", 1, false),t) }
  }

  %op term ff(s1:term) {
    is_fsym(t) { ((ATermAppl)t).getName() == "ff" }
    get_slot(s1,t) { ((ATermAppl)t).getArgument(0)  }
    make(t) { factory.makeAppl(factory.makeAFun("ff", 1, false),t) }
  }

  %op boolean g(s1:term) {
    is_fsym(t) { false }
    make(t) { g(t) }
  }

  %op boolean glight(s1:term) {
    //is_fsym(t) { false }
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
								match(`f(a())) == 12 );
	}

  public void testMatchBis() {
    assertTrue("Testing macth in a match : ", 
								matchBis(`f(a())) == 1 );
	}

  public void testMatch2() {
    assertTrue("Testing macth with when (2 args) : ", 
								match2(`f(a()),`f(a())) == 2 );
	}

  public int match(ATerm t) {
    int result = 0;
    String test = "toto";
    %match(term t) { 
      /* ok */
      f(x) when g(x)      -> { result++; /*System.out.println("cas 1");*/ }
      f(x@_) when g(x)    -> { result++; /*System.out.println("cas 2");*/ }
      f(x) when glight(x)      -> { result++; /*System.out.println("cas 3");*/ }
      ff(x) when g(x)     -> { result++; /*System.out.println("nothing 1");*/ }
      (f|ff)(x) when g(x) -> { result++; /*System.out.println("cas disjunction 1");*/ }
      (ff|f)(x) when g(x) -> { result++; /*System.out.println("cas disjunction 2");*/ }
      f(x) when constant(true)   -> { result++; /*System.out.println("cas constants 1");*/ }
      f(x) when constant(5)      -> { result++; /*System.out.println("cas constants 2");*/ }
      f(x) when constant("toto") -> { result++; /*System.out.println("cas constants 3");*/ }
      f(x) when constant(result) -> { result++; /*System.out.println("cas constants 4");*/ }
      //f(x) when constant(test.length()) ???
      /* warnings */
      f(x) when g(h(x))   -> { result++; /*System.out.println("cas warning 1");*/ }
      f(x) when g(a())    -> { result++; /*System.out.println("cas warning 2");*/ }
      f(x) when g(k())    -> { result++; /*System.out.println("cas warning 3");*/ }
      f(x) when nol()     -> { result++; /*System.out.println("cas warning 4");*/ }
      /* errors */
      //f(x) when g(h(y))   -> { System.out.println("cas error 1"); }
      //f(x) when f(x)      -> { System.out.println("cas error 2"); }
      //f(x) when g(x,x)    -> { System.out.println("cas error 3"); }
      //f(x) when h(x,g(x,x)) -> { System.out.println("cas error 4"); }
    } 
    return result;
  }

  public int matchBis(ATerm t) {
    int result = 0;
    %match(term t) { 
      /* ok */
      f(x) when g(x)    -> {
        %match(term x) {
          a() -> { result++; /*System.out.println("cas bis a");*/ }
          b() -> { result++; /*System.out.println("cas bis b");*/ }
        }
      }
    } 
    return result;
  }

  public int match2(ATerm t1,ATerm t2) {
    int result = 0;
    %match(term t1,term t2) { 
      /* ok */
      f(x),f(x) when g(x),g(x)   -> { result++; /*System.out.println("cas 2 1");*/ }
      f(x),f(x@y) when g(x),g(y) -> { result++; /*System.out.println("cas 2 2");*/ }
      /* pas ok */
      f(x),f(y) when no(x,y)     -> { result++; /*System.out.println("cas 2 3");*/ }
    } 
    return result;
  }

  boolean g(ATerm t) {
    %match(term t) { 
      a() -> { return `true(); }
      b() -> { return `true(); }
    } 
		return `false(); 
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

  boolean constant(boolean b) {
    return b;
  } 

  boolean constant(int i) {
    return i>0;
  }

  boolean constant(String s) {
    return s.equals("toto");
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
