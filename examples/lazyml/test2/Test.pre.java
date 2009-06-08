interface V {
    public Object v();
}
interface F {
    public Object f(Object x);
}
class C {
  public String f;
  public int hash;
  public Object[] c;
  public C(String f, int hash, Object[] c) {
    this.f = f;
    this.hash = hash;
    this.c = c;
  }
  public String toString() {
    String res = "";
    for(int i=0; i<c.length-1; i++)
      res += c[i] + ",";
    if (c.length>0) res += c[c.length-1];
    return f + "(" + res + ")";
  }
}


public class Test {

  public static Object force(Object o) {
    if (o instanceof C) {
      C res = (C) o;
      for(int i=0; i<res.c.length; i++) {
        res.c[i] = force(res.c[i]);
      }
      return res;
    } else if (o instanceof F) {
      return force(((F)o).f(null));
    } else if (o instanceof V) {
      return ((V)o).v();
    } else {
      return o;
    }
  }

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
  public static String showInt(Object x) {
    return ""+((Integer)x);
  }

  public static C True = new C("True","True".hashCode(),new C[] {});
  public static C False = new C("False","False".hashCode(),new C[] {});
  public static C eqInt(Object x, Object y) {
    return ((Integer)x)==((Integer)y) ? True : False;
  }
  public static C gt(Object x, Object y) {
    return ((Integer)x)>((Integer)y) ? True : False;
  }
  public static C eqString(Object x, Object y) {
    return ((String)x).equals((String)y) ? True : False;
  }

  public static void main(String[] args) {

    Object res1 = 
    #include "main.code" 
    ;

    System.out.println(force(res1));
  }
}


