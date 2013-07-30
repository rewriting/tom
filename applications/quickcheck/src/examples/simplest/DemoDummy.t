package examples.simplest;

import examples.simplest.dummy.types.*;

public class DemoDummy {

  %include{ dummy/Dummy.tom }

  // ---------------- swap(Foo foo) ---------------------------------
  public static Foo swap(Foo foo){
    %match(foo) {
      a() -> { return `b(); }
      b() -> { return `a(); }
      c() -> { return `c(); }
      foo(x) -> { return `goo(x); }
      goo(x) -> { return `foo(x); }
    }
    return `a();
  }

  // ---------------- isFancy(Foo foo) ------------------------------
  public static Boolean isFancy(Foo foo){
    %match(foo) {
      a() -> { return true; }
      b() -> { return true; }
      c() -> { return true; }
      foo(x) -> { return !`isFancy(x); }
      goo(x) -> { return !`isFancy(x); }
    }
    return false;
  }

  // ---------------- isFlat(Foo foo) -------------------------------
  public static Boolean isFlat(Foo foo){
    %match(foo) {
      a() -> { return true; }
      b() -> { return true; }
      c() -> { return true; }
    }
    return false;
  }

  // ---------------- swap(Hoo h) -----------------------------------
  public static Hoo swap(Hoo h){
    %match(h) {
      h() -> { return `m(); }
      m() -> { return `h(); }
    }
    return h;
  }

  // ---------------- setElement(Foo f, Hoo h) ----------------------
  public static Foo setElement(Foo f, Hoo h){
    %match(f) {
      hoo(_) -> { return `hoo(h); }
    }
    return f;
  }

  // ---------------- main ------------------------------------------
  public static void main(String args[]) {    
		
		Foo term1 = `a();
		Foo term2 = `foo(b());
		Foo term3 = `goo(c());
		Foo term4 = `hoo(m());
		Foo term5 = `koo(k());

		Hoo term6 = `h();
		Koo term7 = `k();

		System.out.println("\n");

		System.out.println("------------- Call to : Foo swap(Foo foo) ----------------------------");
		System.out.println( "swap(" + term1 + ") = " + swap(term1) );
		System.out.println( "swap(" + term2 + ") = " + swap(term2) + "\n");

		System.out.println("------------- Call to : Boolean isFancy(Foo foo) ---------------------");
		System.out.println( "isFancy(" + term3 + ") = " + isFancy(term3) );
		System.out.println( "isFancy(" + term4 + ") = " + isFancy(term4) + "\n");

		System.out.println("------------- Call to : Boolean isFlat(Foo foo) ----------------------");
		System.out.println( "isFlat(" + term5 + ") = " + isFlat(term5) + "\n");

		System.out.println("------------- Call to : Hoo swap(Hoo h) ------------------------------");
		System.out.println( "swap(" + term6 + ") = " + swap(term6) + "\n");
	

		System.out.println("------------- Call to : Foo setElement(Foo f, Hoo h) -----------------");
		System.out.println( "setElement(" + term4 + ", " + term6 + ") = " + setElement(term4, term6));

		System.out.println("\n");
  }

}
