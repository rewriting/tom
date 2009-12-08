interface V {
    public Object v();
}
interface F {
    public Object f(Object x);
}
class C {
  public String f;
  public Object[] c;
  public C(String f, Object[] c) {
    this.f = f;
    this.c = c;
  }
  public String toString() {
    if (f.equals("Cons") || f.equals("Nil"))
      return "[" + toList() + "]";
    String res = "";
    for(int i=0; i<c.length-1; i++)
      res += c[i] + ",";
    if (c.length>0) res += c[c.length-1];
    return f + "(" + res + ")";
  }
  private String toList() {
    if (f.equals("Cons"))
      return c[0].toString() + (((C)c[1]).f.equals("Nil") ? "" : "," + ((C)c[1]).toList());
    else return "";
  }
}

public class Main {

  public static int plus(Object x, Object y) {
    return ((Integer)x)+((Integer)y);
  }
  public static int minus(Object x, Object y) {
    return ((Integer)x)-((Integer)y);
  }
  public static int times(Object x, Object y) {
    return ((Integer)x)*((Integer)y);
  }
  public static String append(Object x, Object y) {
    return ((String)x)+((String)y); 
  }
  public static String consString(Object x, Object y) {
    return ((Character)x)+((String)y); 
  }
  public static String showInt(Object x) {
    return ""+((Integer)x);
  }
  public static String showChar(Object x) {
    return ""+((Character)x);
  }
  public static C convertString(Object o) {
    String s = (String)o;
    C res = new C("Nil", new C[] {});
    for (int i=s.length()-1; i>=0; i--) {
      res = new C("Cons", new Object[] {((Character)s.charAt(i)),res});
    }
    return res;
  }

  public static int strLen(Object o) {
    return ((String) o).length();
  }

  public static C True = new C("True", new C[] {});
  public static C False = new C("False", new C[] {});
  public static C eqInt(Object x, Object y) {
    return ((Integer)x)==((Integer)y) ? True : False;
  }
  public static C eqChar(Object x, Object y) {
    return ((Character)x)==((Character)y) ? True : False;
  }
  public static C gt(Object x, Object y) {
    return ((Integer)x)>((Integer)y) ? True : False;
  }
  public static C eqString(Object x, Object y) {
    return ((String)x).equals((String)y) ? True : False;
  }
  public static int valueOf(Object c) {
    return Character.getNumericValue((Character)c);
  }
  public static String getInput() {
    try {
      java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
      String line;
      StringBuilder result = new StringBuilder();
      while ((line = br.readLine()) != null) {
        result.append(line);
        result.append("\n");
      }
      return result.toString();
    } catch (java.io.IOException e) {
      return "";
    }
  }
	public static C eol(Object x) {
    return ((Character)x).equals('\n') ? True : False;
	}

  public static Object modulePretty() {
    return 
    #include "pretty.code"
    ;
  }

  public static void main(String[] args) {

    Object res1 = 
    #include "main.code" 
    ;

    System.out.println(res1);
  }
}


