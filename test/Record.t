public class Record {

    // ------------------------------------------------------------
  abstract class Exp {
    public abstract String getOperator();
  }
   
  class CstExp extends Exp {
    public Object value;
    public CstExp(Object value) {
      this.value = value;
    }
    public String getOperator() {
      return "" + value;
    }
  }

  class IntExp extends CstExp {
    public IntExp(int value) {
      super(new Integer(value));
    }
  }
  
  class StringExp extends CstExp {
    public StringExp(String value) {
      super(value);
    }
  }

  class UnaryOperator extends Exp {
    public Exp first;
    public UnaryOperator(Exp first) {
      this.first = first;
    } 
    public String getOperator() { return ""; }
  }

  class BinaryOperator extends Exp {
    public Exp first;
    public Exp second;
    public BinaryOperator(Exp first, Exp second) {
      this.first = first;
      this.second = second;
    }
    public String getOperator() { return ""; }
  }
  
  class Plus extends BinaryOperator {
    public Plus(Exp first, Exp second) {
      super(first,second);
    }
    public String getOperator() { return "Plus"; }
  }

  class Mult extends BinaryOperator {
    public Mult(Exp first, Exp second) {
      super(first,second);
    }
    public String getOperator() { return "Mult"; }
  }
  
  class Uminus extends UnaryOperator {
    public Uminus(Exp first) {
      super(first);
    }
    public String getOperator() { return "Uminus"; }
  }

    // ------------------------------------------------------------
  
  private Object myGetSubterm(Object t, int n) {
    try {
        //System.out.println("fields[" + n + "] = " + (t.getClass().getFields()[n]));
      return (t.getClass().getFields()[n]).get(t);
    } catch (Exception e) {
      return null;
    }
  }
  
  %typeterm TomObject {
    implement { Object }
    get_fun_sym(t)      { t.getClass() }
    cmp_fun_sym(subjectFunSym,patternFunSym)  {
      ((Class)patternFunSym).isAssignableFrom(((Class)subjectFunSym))
        }
    get_subterm(t, n)   { myGetSubterm(t,n) }
  }

  %typeterm TomExp {
    implement { Exp }
    get_fun_sym(t)      { t.getClass() }
    cmp_fun_sym(subjectFunSym,patternFunSym)  {
      ((Class)patternFunSym).isAssignableFrom(((Class)subjectFunSym))
        }
    get_subterm(t, n)   { myGetSubterm(t,n) }
  }

  %typeterm TomBinaryOperator {
    implement { BinaryOperator }
    get_fun_sym(t)      { t.getClass() }
    cmp_fun_sym(subjectFunSym,patternFunSym)  {
      ((Class)patternFunSym).isAssignableFrom(((Class)subjectFunSym))
        }
    get_subterm(t, n)   { (n==0)?((BinaryOperator)t).first:((BinaryOperator)t).second }
  }
  
  %typeterm TomUnaryOperator {
    implement { UnaryOperator }
    get_fun_sym(t)      { t.getClass() }
    cmp_fun_sym(subjectFunSym,patternFunSym)  {
      ((Class)patternFunSym).isAssignableFrom(((Class)subjectFunSym))
        }
    get_subterm(t, n)   { ((UnaryOperator)t).first }
  }

  %typeterm TomCstExp {
    implement { CstExp }
    get_fun_sym(t)      { t.getClass() }
    cmp_fun_sym(subjectFunSym,patternFunSym)  {
      ((Class)patternFunSym).isAssignableFrom(((Class)subjectFunSym))
        }
    get_subterm(t, n)   { ((CstExp)t).value }
  }

    // ------------------------------------------------------------
  
  %op TomBinaryOperator BinaryOperator(first:TomExp, second:TomExp) {
    fsym { (new BinaryOperator(null,null)).getClass() }
  }

  %op TomUnaryOperator UnaryOperator(first:TomExp) {
    fsym { (new UnaryOperator(null)).getClass() }
  }

  %op TomBinaryOperator Plus(first:TomExp, second:TomExp) {
    fsym { (new Plus(null,null)).getClass() }
  }

  %op TomBinaryOperator Mult(first:TomExp, second:TomExp) {
    fsym { (new Mult(null,null)).getClass() }
  }

  %op TomUnaryOperator Uminus(first:TomExp) {
    fsym { (new Uminus(null)).getClass() }
  }

  %op TomCstExp CstExp(value:TomObject) {
    fsym { (new CstExp(null)).getClass() }
  }

  %op TomCstExp IntExp(value:TomInteger) {
    fsym { (new IntExp(0)).getClass() }
  }

    // ------------------------------------------------------------
  
  private final static Integer ZERO = new Integer(0);
  private final static Integer SUC  = new Integer(1);
  
  %typeterm TomInteger {
    implement { Integer }
    get_fun_sym(i)      { (((Integer)i).intValue()==0)?ZERO:SUC }
    cmp_fun_sym(i1,i2)  { i1 == i2 }
    get_subterm(i, n)   { new Integer(((Integer)i).intValue()-1) }
  }

  %op TomInteger zero {
    fsym { ZERO }
  }

  %op TomInteger suc(TomInteger) {
    fsym { SUC }
  }

    // ------------------------------------------------------------

  public final static void main(String[] args) {
    Record test = new Record();
    test.test1();
    test.test2();
    test.test3();
  }

  public Exp buildExp1() {
    return new Mult(new Plus(new IntExp(2), new IntExp(3)), new IntExp(4));
  }

  public Exp buildExp2() {
    return new Mult(new Plus(new StringExp("a"), new IntExp(0)), new IntExp(1));
  }

  public Exp buildExp3() {
    return new Plus(buildExp2(), new Uminus(new StringExp("a")));
  }

  public Exp simplifiedExp1() {
    return new IntExp(20);
  }
  
  public Exp simplifiedExp2() {
    return new StringExp("a");
  }

  public Exp simplifiedExp3() {
    return new IntExp(0);
  }

  public void test1() {
    Exp e = buildExp1();
    String s1 = prettyPrint(e);
    String s2 = prettyPrintInv(e);
    String s3 = prettyPrint(traversalSimplify(e));
    assertTrue( s1.equals("Mult(Plus(2,3),4)") );
    assertTrue( s2.equals("2 3 Plus 4 Mult") );
    assertTrue( s3.equals("20") );
  }
  public void test2() {
    Exp e = buildExp2();
    String s1 = prettyPrint(e);
    String s2 = prettyPrintInv(e);
    String s3 = prettyPrint(traversalSimplify(e));
    assertTrue( s1.equals("Mult(Plus(a,0),1)") );
    assertTrue( s2.equals("a 0 Plus 1 Mult") );
    assertTrue( s3.equals("a") );
  }

  public void test3() {
    Exp e = buildExp3();
    String s1 = prettyPrint(e);
    String s2 = prettyPrintInv(e);
    String s3 = prettyPrint(traversalSimplify(e));
    assertTrue( s1.equals("Plus(Mult(Plus(a,0),1),Uminus(a))") );
    assertTrue( s2.equals("a 0 Plus 1 Mult a Uminus Plus") );
    assertTrue( s3.equals("0") );
  }

  public String prettyPrint(Exp t) {
    String op = t.getOperator();
    %match(TomExp t) {
      CstExp[]  -> { return op; }
      
      UnaryOperator[first=e1] -> {
        return op + "(" + prettyPrint(`e1) + ")";
      }

      BinaryOperator[first=e1,second=e2] -> {
        return op + "(" + prettyPrint(`e1) + "," + prettyPrint(`e2) + ")";
      }
    }
    return "error";
  }

  public String prettyPrintInv(Exp t) {
    String op = t.getOperator();
    %match(TomExp t) {
      CstExp[]  -> { return op; }
      
      UnaryOperator[first=e1] -> {
        return prettyPrintInv(`e1) + " " + op;
      }
      
      BinaryOperator[first=e1,second=e2] -> {
        return prettyPrintInv(`e1) + " " + prettyPrintInv(`e2) + " " + op;
      }
    }
    return "error";
  }

  public Exp traversalSimplify(Exp t) {
    %match(TomExp t) {
      UnaryOperator[first=e1] -> {
        ((UnaryOperator)t).first  = traversalSimplify(`e1);
        return simplify(t);
      }
      
      BinaryOperator[first=e1, second=e2] -> {
        ((BinaryOperator)t).first  = traversalSimplify(`e1);
        ((BinaryOperator)t).second = traversalSimplify(`e2);
        return simplify(t);
      }
    }
    return t;
  }

  public Exp simplify(Exp t) {
    %match(TomExp t) {
      Plus[first=IntExp(v1), second=IntExp(v2)] -> {
        return new IntExp(`v1.intValue() + `v2.intValue());
      }

      Plus[first=e1, second=IntExp(zero())] -> { return `e1; }
      Plus[second=e1, first=IntExp(zero())] -> { return `e1; }

      Plus[first=e1, second=Uminus(e2)] -> {
        if(myEquals(`e1,`e2)) {
          return new IntExp(0);
        } else {
          return t;
        }
      }

      Mult[first=IntExp(v1), second=IntExp(v2)] -> {
        return new IntExp(`v1.intValue() * `v2.intValue());
      }
      
      Mult[first=e1, second=IntExp(suc(zero()))] -> { return `e1; }
      Mult[second=e1, first=IntExp(suc(zero()))] -> { return `e1; }
    }
    return t;
  }

  public boolean myEquals(Exp t1, Exp t2) {
    %match(TomExp t1, TomExp t2) {
      
      CstExp[value=e1], CstExp[value=e2]       -> { return `e1.equals(`e2); }
      
      UnaryOperator[first=e1], UnaryOperator[first=f1] -> {
        return t1.getOperator().equals(t2.getOperator()) && myEquals(`e1,`f1);
      }
      
      BinaryOperator[first=e1, second=e2], BinaryOperator[first=f1, second=f2] -> {
        return t1.getOperator().equals(t2.getOperator()) && myEquals(`e1,`f1) && myEquals(`e2,`f2);
      }

    }
    return false;
  }

  static void  assertTrue(boolean condition) {
    if(!condition) {
      throw new RuntimeException("assertion failed.");
    }
  }
  
}

