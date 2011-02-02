/*
 * Copyright (c) 2004-2010, INPL, INRIA
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *  - Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  - Neither the name of the INRIA nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package subtypeinference;

public class Problem3 {
  %include{ int.tom }
  %include{ String.tom }

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
    public IntExp(int ivalue) {
      super(new Integer(ivalue));
    }
  }
  
  class StringExp extends CstExp {
    public StringExp(String svalue) {
      super(svalue);
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
  %typeterm TomInteger {
    implement { Integer }
    is_sort(t) { $t instanceof Integer }
  }

  %typeterm TomExp {
    implement { Exp }
    is_sort(t) { $t instanceof Exp }
  }

  %typeterm TomBinaryOperator extends TomExp {
    implement { BinaryOperator }
    is_sort(t) { $t instanceof BinaryOperator }
  }

  %typeterm TomUnaryOperator extends TomExp{
    implement { UnaryOperator }
    is_sort(t) { $t instanceof UnaryOperator }
  }

  %typeterm TomCstExp extends TomExp {
    implement { CstExp }
    is_sort(t) { $t instanceof CstExp }
  }

    // ------------------------------------------------------------
  
  %op TomInteger zero() {
    is_fsym(t) { (((Integer)$t).intValue()==0) }
  }

  %op TomInteger suc(p:TomInteger) {
    is_fsym(t) { (((Integer)$t).intValue()!=0) }
    get_slot(p,t) { new Integer(((Integer)$t).intValue()-1) }
  }

  %op TomBinaryOperator Plus(first:TomExp, second:TomExp) {
    is_fsym(t) { $t instanceof Plus }
    get_slot(first,t) { ((Plus)$t).first }
    get_slot(second,t) { ((Plus)$t).second }
  }

  %op TomBinaryOperator Mult(first:TomExp, second:TomExp) {
    is_fsym(t) { $t instanceof Mult }
    get_slot(first,t) { ((Mult)$t).first }
    get_slot(second,t) { ((Mult)$t).second }
  }

  %op TomUnaryOperator Uminus(first:TomExp) {
    is_fsym(t) { $t instanceof Uminus }
    get_slot(first,t) { ((Uminus)$t).first }
  }

   %op TomCstExp IntExp(ivalue:TomInteger) {
    is_fsym(t) { $t instanceof IntExp }
    get_slot(ivalue,t) { ((Integer)((IntExp)$t).value) }
  }

  %op TomCstExp StringExp(svalue:String) {
    is_fsym(t) { $t instanceof StringExp }
    get_slot(svalue,t) { ((String)((StringExp)$t).value) }
  }

    // ------------------------------------------------------------
 
  public final static void main(String[] args) {
    Problem3 test = new Problem3();
    //test.test1();
    //test.test2();
    test.test3();
  }

  public BinaryOperator buildExp1() {
    return new Mult(new Plus(new IntExp(2), new IntExp(3)), new IntExp(4));
  }

  public BinaryOperator buildExp2() {
    return new Mult(new Plus(new StringExp("a"), new IntExp(0)), new IntExp(1));
  }

  public BinaryOperator buildExp3() {
    return new Plus(buildExp2(), new Uminus(new StringExp("a")));
  }

/*
  public void test1() {
    Exp e = buildExp1();
    String s1 = prettyPrint(e);
    String s2 = prettyPrintInv(e);
    String s3 = prettyPrint(traversalSimplify(e));
    assertTrue( s1.equals("Mult(Plus(2,3),4)") );
    assertTrue( s2.equals("2 3 Plus 4 Mult") );
    assertTrue( s3.equals("20") );
    System.out.println(s1);
    System.out.println(s2);
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
*/
  public void test3() {
    Exp e = buildExp3();
    //String s1 = prettyPrint(e);
    //String s2 = prettyPrintInv(e);
    String s3 = prettyPrint(traversalSimplify(e));
    //assertTrue( s1.equals("Plus(Mult(Plus(a,0),1),Uminus(a))") );
    //assertTrue( s2.equals("a 0 Plus 1 Mult a Uminus Plus") );
    //assertTrue( s3.equals("0") );
  }

  /* An example with explicit declaration of subject's type */
  public String prettyPrint(Exp t) {
    String op = t.getOperator();
    %match {
      (Plus|Mult)[first=e1,second=e2] << TomBinaryOperator t-> {
        return op + "(" + prettyPrint(`e1) + "," + prettyPrint(`e2) + ")";
      }

      Uminus[first=e1] << TomUnaryOperator t -> {
        return op + "(" + prettyPrint(`e1) + ")";
      }

      _ << TomCstExp t -> { return op; }

    }
    return "error";
  }

  /* An example without declaration of subject's type */
  /*
  public String prettyPrintInv(Exp t) {
    String op = t.getOperator();
    %match(t) {
      (Plus|Mult)[first=e1,second=e2] -> {
        return prettyPrintInv(`e1) + " " + prettyPrintInv(`e2) + " " + op;
      }

      Uminus[first=e1] -> {
        return prettyPrintInv(`e1) + " " + op;
      }

      (IntExp|StringExp)[]  -> { return op; }
      
    }
    return "error";
  }
*/
  public Exp traversalSimplify(Exp t) {
    %match(t) {
      Uminus[first=e1] -> {
        ((UnaryOperator)t).first  = traversalSimplify(`e1);
        return simplify(t);
      }
      
      (Plus|Mult)[first=e1, second=e2] -> {
        ((BinaryOperator)t).first  = traversalSimplify(`e1);
        ((BinaryOperator)t).second = traversalSimplify(`e2);
        return simplify(t);
      }
    }
    return t;
  }

  public Exp simplify(Exp t) {
    %match(t) {
      /*
      Plus[first=IntExp(v1), second=IntExp(v2)] -> {
        return new IntExp(`v1.intValue() + `v2.intValue());
      }
*/
      //Plus[first=e1, second=IntExp(zero())] -> { return `e1; }
      //Plus[second=e1, first=IntExp(zero())] -> { return `e1; }

      /*
      Plus[first=e1, second=Uminus(e2)] -> {
        if(myEquals(`e1,`e2)) {
          return new IntExp(0);
        } else {
          return t;
        }
      }
*/
      Mult[first=IntExp(v1), second=IntExp(v2)] -> {
        //return new IntExp(`v1.intValue() * `v2.intValue());
      }
      
      Mult[first=e1, second=IntExp(suc(zero()))] -> { return `e1; }
      Mult[second=e1, first=IntExp(suc(zero()))] -> { return `e1; }
    }
    return t;
  }

  public boolean myEquals(Exp t1, Exp t2) {
    %match(t1,t2) {
      
      IntExp[ivalue=e1], IntExp[ivalue=e2] -> { return `e1.equals(`e2); }
      
      StringExp[svalue=e1], StringExp[svalue=e2] -> { return `e1.equals(`e2); }

      Uminus[first=e1], Uminus[first=f1] -> { return myEquals(`e1,`f1); }
      
      (Plus|Mult)[first=e1, second=e2], (Plus|Mult)[first=f1, second=f2] -> {
        return t1.getOperator().equals(t2.getOperator()) && myEquals(`e1,`f1) && myEquals(`e2,`f2);
      }

    }
    return false;
  }
  /*
  static void  assertTrue(boolean condition) {
    if(!condition) {
      throw new RuntimeException("assertion failed.");
    }
  }
  */
}

