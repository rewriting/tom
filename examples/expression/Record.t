package Expression;

public class Record { 

  %include {int.tom}

  %typeterm Exp {
    implement { Exp }
    get_fun_sym(t) {null}
    cmp_fun_sym(s1,s2) { false}
    get_subterm(t,n) {null}
    equals(t1,t2) {t1.equals(t2)}
  }

  %typeterm BinaryOperator {
    implement { BinaryOperator }
    get_fun_sym(t) {null}
    cmp_fun_sym(s1,s2) { false}
    get_subterm(t,n) {null}
    equals(t1,t2) {t1.equals(t2)}
  }

  %typeterm UnaryOperator {
    implement { UnaryOperator }
    get_fun_sym(t) {null}
    cmp_fun_sym(s1,s2) { false}
    get_subterm(t,n) {null}
    equals(t1,t2) {t1.equals(t2)}
  }

  %typeterm CstExp {
    implement { CstExp }
    get_fun_sym(t) {null}
    cmp_fun_sym(s1,s2) { false}
    get_subterm(t,n) {null}
    equals(t1,t2) {t1.equals(t2)}
  }

    // ------------------------------------------------------------
  
  %op Exp BinaryOperator(first:Exp, second:Exp) {
    fsym {}
    is_fsym(t) { t instanceof BinaryOperator }
    get_slot(first,t) { ((BinaryOperator)t).first }
    get_slot(second,t) { ((BinaryOperator)t).second }
  }

  %op Exp UnaryOperator(first:Exp) {
    fsym { }
    is_fsym(t) { t instanceof UnaryOperator }
    get_slot(first,t) { ((UnaryOperator)t).first }
  }

  %op BinaryOperator Plus(first:Exp, second:Exp) {
    fsym { }
    is_fsym(t) { t instanceof Plus }
    get_slot(first,t) { ((Plus)t).first }
    get_slot(second,t) { ((Plus)t).second }
  }

  %op BinaryOperator Mult(first:Exp, second:Exp) {
    fsym { }
    is_fsym(t) { t instanceof Mult }
    get_slot(first,t) { ((Mult)t).first }
    get_slot(second,t) { ((Mult)t).second }
  }

  %op UnaryOperator Uminus(first:Exp) {
    fsym { } 
    is_fsym(t) { t instanceof Uminus }
    get_slot(first,t) { ((Uminus)t).first }
  }

  %op CstExp StringExp(value:String) {
    fsym { } 
    is_fsym(t) { t instanceof StringExp }
    get_slot(value,t) { ((StringExp)t).value }
  }


  %op CstExp IntExp(value:int) {
    fsym { } 
    is_fsym(t) { t instanceof IntExp }
    get_slot(value,t) { ((IntExp)t).value }
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

    System.out.println("prettyPrint: " + s1);
    System.out.println("prettyPrintInv: " + s2);
    System.out.println("simplify: " + s3);
  }
  
  public void test2() {
    Exp e = buildExp2();
    String s1 = prettyPrint(e);
    String s2 = prettyPrintInv(e);
    String s3 = prettyPrint(traversalSimplify(e));

    System.out.println("prettyPrint: " + s1);
    System.out.println("prettyPrintInv: " + s2);
    System.out.println("simplify: " + s3);
  }

  public void test3() {
    Exp e = buildExp3();
    String s1 = prettyPrint(e);
    String s2 = prettyPrintInv(e);
    String s3 = prettyPrint(traversalSimplify(e));

    System.out.println("prettyPrint: " + s1);
    System.out.println("prettyPrintInv: " + s2);
    System.out.println("simplify: " + s3);
  }

  public String prettyPrint(Exp t) {
    String op = t.getOperator();
    %match(Exp t) {
      IntExp[]  -> { return op; }
      StringExp[]  -> { return op; }
      
      UnaryOperator[first=e1] -> {
        return op + "(" + prettyPrint(e1) + ")";
      }

      BinaryOperator[first=e1,second=e2] -> {
        return op + "(" + prettyPrint(e1) + "," + prettyPrint(e2) + ")";
      }
    }
    return "error";
  }

  public String prettyPrintInv(Exp t) {
    String op = t.getOperator();
    %match(Exp t) {
      IntExp[]  -> { return op; }
      StringExp[]  -> { return op; }
      
      UnaryOperator[first=e1] -> {
        return prettyPrintInv(e1) + " " + op;
      }
      
      BinaryOperator[first=e1,second=e2] -> {
        return prettyPrintInv(e1) + " " + prettyPrintInv(e2) + " " + op;
      }
    }
    return "error";
  }

  public Exp traversalSimplify(Exp t) {
    %match(Exp t) {
      UnaryOperator[first=e1] -> {
        ((UnaryOperator)t).first  = traversalSimplify(e1);
        return simplify(t);
      }
      
      BinaryOperator[first=e1, second=e2] -> {
        ((BinaryOperator)t).first  = traversalSimplify(e1);
        ((BinaryOperator)t).second = traversalSimplify(e2);
        return simplify(t);
      }
    }
    return t;
  }

  public Exp simplify(Exp t) {
    %match(Exp t) {
      Plus[first=IntExp(v1), second=IntExp(v2)] -> {
        return new IntExp(v1 + v2);
      }

      Plus[first=e1, second=IntExp(0)] -> { return e1; }
      Plus[second=e1, first=IntExp(0)] -> { return e1; }

      Plus[first=e1, second=Uminus(e2)] -> {
        if(myEquals(e1,e2)) {
          return new IntExp(0);
        } else {
          return t;
        }
      }

      Mult[first=IntExp(v1), second=IntExp(v2)] -> {
        return new IntExp(v1 * v2);
      }
      
      Mult[first=e1, second=IntExp(1)] -> { return e1; }
      Mult[second=e1, first=IntExp(1)] -> { return e1; }
    }
    return t;
  }

  
  public boolean myEquals(Exp t1, Exp t2) {
    %match(Exp t1, Exp t2) {
      
      IntExp[value=e1], IntExp[value=e2]       -> { return e1==e2; }
      StringExp[value=e1], StringExp[value=e2] -> { return e1.equals(e2); }
      
      UnaryOperator[first=e1], UnaryOperator[first=f1] -> {
        return t1.getOperator().equals(t2.getOperator()) && myEquals(e1,f1);
      }
      
      BinaryOperator[first=e1, second=e2], BinaryOperator[first=f1, second=f2] -> {
        return t1.getOperator().equals(t2.getOperator()) && myEquals(e1,f1) && myEquals(e2,f2);
      }

    }
    return false;
  }

 
}

