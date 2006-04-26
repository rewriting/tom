import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestRecord extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(new TestSuite(TestRecord.class));
	}

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
  }

  %typeterm TomExp {
    implement { Exp }
  }

  %typeterm TomBinaryOperator {
    implement { BinaryOperator }
  }
  
  %typeterm TomUnaryOperator {
    implement { UnaryOperator }
  }

  %typeterm TomCstExp {
    implement { CstExp }
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
  }

  %op TomInteger zero() {
    is_fsym(t) { (((Integer)t).intValue()==0) }
  }

  %op TomInteger suc(p:TomInteger) {
    is_fsym(t) { (((Integer)t).intValue()!=0) }
    get_slot(p,t) { new Integer(((Integer)t).intValue()-1) }
  }

    // ------------------------------------------------------------

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

	public void testPrettyPrint() {
    String s1 = prettyPrint(buildExp1());
    assertEquals("Pretty print Exp1 :", s1,"Mult(Plus(2,3),4)");
	}

  public void testPrettyPrintInv() {
    String s2 = prettyPrintInv(buildExp1());
    assertEquals("PrettyPrintInv Exp1 :", s2,"2 3 Plus 4 Mult");
  }

  public void testPrettyPrintTraversal() {
    String s3 = prettyPrint(traversalSimplify(buildExp1()));
    assertEquals("PrettyPrint with Traversal Exp1 :", s3,"20");
  }

  public void testPrettyPrint2() {
    String s = prettyPrint(buildExp2());
    assertEquals("Pretty print Exp2 :", s,"Mult(Plus(a,0),1)");
  }

  public void testPrettyPrintInv2() {
    String s = prettyPrintInv(buildExp2());
    assertEquals("PrettyPrintInv Exp2 :", s,"a 0 Plus 1 Mult");
  }

  public void testPrettyPrintTraversal2() {
    String s = prettyPrint(traversalSimplify(buildExp2()));
    assertEquals("PrettyPrint with Traversal Exp2 :", s,"a");
  }

  public void testPrettyPrint3() {
    String s = prettyPrint(buildExp3());
    assertEquals("Pretty print Exp3 :", s,"Plus(Mult(Plus(a,0),1),Uminus(a))");
  }

  public void testPrettyPrintInv3() {
    String s = prettyPrintInv(buildExp3());
		assertEquals("PrettyPrintInv Exp3 :", s,"a 0 Plus 1 Mult a Uminus Plus");
  }

  public void testPrettyPrintTraversal3() {
    String s = prettyPrint(traversalSimplify(buildExp3()));
		assertEquals("PrettyPrint with Traversal Exp3 :", s,"0");
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

}

