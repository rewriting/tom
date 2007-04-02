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
  
  %typeterm TomObject {
    implement { Object }
    is_sort(t) { t instanceof Object }
  }

  %typeterm TomExp {
    implement { Exp }
    is_sort(t) { t instanceof Exp }
  }

  %typeterm TomBinaryOperator {
    implement { BinaryOperator }
    is_sort(t) { t instanceof BinaryOperator }
  }
  
  %typeterm TomUnaryOperator {
    implement { UnaryOperator }
    is_sort(t) { t instanceof UnaryOperator }
  }

  %typeterm TomCstExp {
    implement { CstExp }
    is_sort(t) { t instanceof CstExp }
  }

    // ------------------------------------------------------------
  
  %op TomBinaryOperator BinaryOperator(first:TomExp, second:TomExp) {
    is_fsym(t) { t instanceof BinaryOperator }
    get_slot(first,t) { ((BinaryOperator)t).first }
    get_slot(second,t) { ((BinaryOperator)t).second }
  }

  %op TomUnaryOperator UnaryOperator(first:TomExp) {
    is_fsym(t) { t instanceof UnaryOperator }
    get_slot(first,t) { ((UnaryOperator)t).first }
  }

  %op TomBinaryOperator Plus(first:TomExp, second:TomExp) {
    is_fsym(t) { t instanceof Plus }
    get_slot(first,t) { ((Plus)t).first }
    get_slot(second,t) { ((Plus)t).second }
  }

  %op TomBinaryOperator Mult(first:TomExp, second:TomExp) {
    is_fsym(t) { t instanceof Mult }
    get_slot(first,t) { ((Mult)t).first }
    get_slot(second,t) { ((Mult)t).second }
  }

  %op TomUnaryOperator Uminus(first:TomExp) {
    is_fsym(t) { t instanceof Uminus }
    get_slot(first,t) { ((Uminus)t).first }
  }

  %op TomCstExp CstExp(value:TomObject) {
    is_fsym(t) { t instanceof CstExp }
    get_slot(value,t) { ((CstExp)t).value }
  }

  %op TomCstExp IntExp(value:TomInteger) {
    is_fsym(t) { t instanceof IntExp }
    get_slot(value,t) { (Integer)((IntExp)t).value }
  }

    // ------------------------------------------------------------
  
  private final static Integer ZERO = new Integer(0);
  private final static Integer SUC  = new Integer(1);
  
  %typeterm TomInteger {
    implement { Integer }
    is_sort(t) { t instanceof Integer }
  }

  %op TomInteger zero() {
    is_fsym(t) { (((Integer)t).intValue()==0) }
  }

  %op TomInteger suc(p:TomInteger) {
    is_fsym(t) { (((Integer)t).intValue()!=0) }
    get_slot(p,t) { new Integer(((Integer)t).intValue()-1) }
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

