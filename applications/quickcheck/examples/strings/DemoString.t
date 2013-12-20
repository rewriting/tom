package examples.strings;

public class DemoString {

  // ---------------- isShort ---------------------------------------------------------------------
  public static Boolean isShort(String s){
    return s.length() < 3;
  }

  // ---------------- containsNoA -----------------------------------------------------------------
  public static Boolean containsNoA(String s){
		if( s.indexOf('a') == -1 && s.indexOf('A') == -1 ) {
			return true;
		}
		return false;
  }

  // ---------------- containsA -------------------------------------------------------------------
  public static Boolean containsA(String s){
		if( s.indexOf('a') == -1 && s.indexOf('A') == -1 ) {
			return false;
		}
		return true;
  }

  // ---------------- isPalindrome ----------------------------------------------------------------
  public static Boolean isPalindrome(String s){
    if(s.length()<2){
    	return false;
  	}
  	
  	// Removes all whitespaces and non visible characters such as tab, \n .
		s = s.replaceAll("\\s","");
		
    for(int i=0; i < s.length()/2; i++){
      if( s.charAt(i) != s.charAt(s.length()-1-i) ) {
      	return false;
    	}
    }
    return true;
  }

  // ---------------- reverse ---------------------------------------------------------------------
  public static String reverse(String s){
    return new StringBuilder(s).reverse().toString();
  }

  // ---------------- subString -------------------------------------------------------------------
  public static Boolean subString(String s, String z) {
	  return s.contains(z);
  }
  
  // ---------------- main ------------------------------------------------------------------------
  public static void main(String args[]) {
			
		String s1 = "abcd";
		String s2 = "DCBA";
		String s3 = "bcdefg";
		String s4 = "GFEDCB";
		String s5 = "madam";
		String s6 = "was it a car or a cat i saw";

		System.out.print("\n");

		System.out.println("------------------- Is Short -----------------------------------------");
		System.out.println( "isShort(" + s1 + ") = " + isShort(s1) );
		System.out.println( "isShort(" + s2 + ") = " + isShort(s2) );
		System.out.println( "isShort(" + s3 + ") = " + isShort(s3) );
		System.out.println( "isShort(" + s4 + ") = " + isShort(s4) );

		System.out.println("------------------- Contains no A ------------------------------------");
		System.out.println( "containsNoA(" + s1 + ") = " + containsNoA(s1) );
		System.out.println( "containsNoA(" + s2 + ") = " + containsNoA(s2) );
		System.out.println( "containsNoA(" + s3 + ") = " + containsNoA(s3) );
		System.out.println( "containsNoA(" + s4 + ") = " + containsNoA(s4) );


		System.out.println("------------------- Contains A ---------------------------------------");
		System.out.println( "containsA(" + s1 + ") = " + containsA(s1) );
		System.out.println( "containsA(" + s2 + ") = " + containsA(s2) );
		System.out.println( "containsA(" + s3 + ") = " + containsA(s3) );
		System.out.println( "containsA(" + s4 + ") = " + containsA(s4) );

		System.out.println("------------------- Is palindrome ------------------------------------");
		System.out.println( "isPalindrome(" + s5 + ") = " + isPalindrome(s5) );
		System.out.println( "isPalindrome(" + s6 + ") = " + isPalindrome(s6) );

		System.out.println("------------------- Reverse ------------------------------------------");
		System.out.println( "reverse(" + s1 + ") = " + reverse(s1) );
		System.out.println( "reverse(" + s4 + ") = " + reverse(s4) );
		System.out.println( "reverse(" + s6 + ") = " + reverse(s5) );

		System.out.println("------------------- SubString ----------------------------------------");
		String sub1 = "CBA";
		String sub2 = "cdex";
		String sub3 = "madam";
		System.out.println( "subString(" + s2 + "," + sub1 + ") = " + subString(s2,sub1) );
		System.out.println( "subString(" + s3 + "," + sub2 + ") = " + subString(s3,sub2) );
		System.out.println( "subString(" + s5 + "," + sub3 + ") = " + subString(s5,sub3) );
		
		System.out.print("\n");
	}

}
