package examples.simplest;

import examples.simplest.dummy.types.*;

public class DemoDummy {

  %include{ dummy/Dummy.tom }

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

  public static Boolean isFlat(Foo foo){
    %match(foo) {
      a() -> { return true; }
      b() -> { return true; }
      c() -> { return true; }
    }
    return false;
  }

  public static Hoo swap(Hoo h){
    %match(h) {
      h() -> { return `m(); }
      m() -> { return `h(); }
    }
    return h;
  }

  public static Foo setElement(Foo f, Hoo h){
    %match(f) {
      hoo(_) -> { return `hoo(h); }
    }
    return f;
  }


  public static int zero(){
    return 0;
  }

  public static Boolean isShort(String s){
    return s.length() < 5;
  }

  public static Boolean containsNoA(String s){
    return !( s.length()!=0 && (s.charAt(0)=='a' && s.charAt(s.length()/2)=='a' &&  s.charAt(s.length()-1)=='a') )  ;
  }

  public static Boolean containsA(String s){
    return s.length()!=0 && (s.charAt(0)=='a' && s.charAt(s.length()-1)=='a');
  }

  public static Boolean isPalindrome(String s){
    Boolean res = true;
    if(s.length()<2){
      res=false;
    }
    for(int i=0; i< s.length()/2; i++){
      if(s.charAt(i)!=s.charAt(s.length()-1-i)){
        res=false;
      }
    }
    return res;
  }

  public static Boolean isSmall(int n){
    return n < 4;
  }

  public static String reverse(String s){
    return new StringBuilder(s).reverse().toString();
  }





  public static void main(String args[]) {    
		
		Foo term1 = `a();
		Foo term2 = `foo(b());
		Foo term3 = `goo(c());
		Foo term4 = `hoo(m());
		Foo term5 = `koo(k());

		Hoo term6 = `h();
		Koo term7 = `k();

		// public static Foo swap(Foo foo)
		System.out.println( "\nswap(" + term1 + ") = " + swap(term1) );
		System.out.println( "swap(" + term2 + ") = " + swap(term2) + "\n");

		// public static Boolean isFancy(Foo foo)
		System.out.println( "isFancy(" + term3 + ") = " + isFancy(term3) );
		System.out.println( "isFancy(" + term4 + ") = " + isFancy(term4) + "\n");

		// public static Boolean isFlat(Foo foo)
		System.out.println( "isFlat(" + term5 + ") = " + isFlat(term5) + "\n");

		// public static Hoo swap(Hoo h)
		System.out.println( "swap(" + term6 + ") = " + swap(term6) + "\n");
	
	  // public static Foo setElement(Foo f, Hoo h){
		System.out.println( "setElement(" + term4 + ", " + term6 + ") = " + setElement(term4, term6) + "\n");
	
  }

}
