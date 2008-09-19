/*
 * Copyright (c) 2004-2008, INRIA
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 * 	- Redistributions of source code must retain the above copyright
 * 	notice, this list of conditions and the following disclaimer.  
 * 	- Redistributions in binary form must reproduce the above copyright
 * 	notice, this list of conditions and the following disclaimer in the
 * 	documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the INRIA nor the names of its
 * 	contributors may be used to endorse or promote products derived from
 * 	this software without specific prior written permission.
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

package expression;

public class Record { 

  %include {int.tom}
  %include {string.tom}

  %typeterm TomExp {
    implement { Exp }
    is_sort(t) { t instanceof Exp }
    equals(t1,t2) {t1.equals(t2)}
  }

  %typeterm TomBinaryOperator {
    implement { BinaryOperator }
    is_sort(t) { t instanceof BinaryOperator }
    equals(t1,t2) {t1.equals(t2)}
  }

  %typeterm TomUnaryOperator {
    implement { UnaryOperator }
    is_sort(t) { t instanceof UnaryOperator }
    equals(t1,t2) {t1.equals(t2)}
  }

  %typeterm TomCstExp {
    implement { CstExp }
    is_sort(t) { t instanceof CstExp }
    equals(t1,t2) {t1.equals(t2)}
  }

  %typeterm TomPlus {
    implement { Plus }
    is_sort(t) { t instanceof Plus }
    equals(t1,t2) {t1.equals(t2)}
  }

  %typeterm TomMult {
    implement { Mult }
    is_sort(t) { t instanceof Mult }
    equals(t1,t2) {t1.equals(t2)}
  }

  %typeterm TomUminus {
    implement { Uminus }
    is_sort(t) { t instanceof Uminus }
    equals(t1,t2) {t1.equals(t2)}
  }

  %typeterm TomStringExp {
    implement { StringExp }
    is_sort(t) { t instanceof StringExp }
    equals(t1,t2) {t1.equals(t2)}
  }

  %typeterm TomIntExp {
    implement { IntExp }
    is_sort(t) { t instanceof IntExp }
    equals(t1,t2) {t1.equals(t2)}
  }

    // ------------------------------------------------------------
  
  %op TomExp BinaryOperator(first:TomExp, second:TomExp) {
    is_fsym(t) { t instanceof BinaryOperator }
    get_slot(first,t) { ((BinaryOperator)t).first }
    get_slot(second,t) { ((BinaryOperator)t).second }
  }

  %op TomExp UnaryOperator(first:TomExp) {
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

  %op TomCstExp StringExp(value:String) {
    is_fsym(t) { t instanceof StringExp }
    get_slot(value,t) { ((StringExp)t).value }
  }


  %op TomCstExp IntExp(value:int) {
    is_fsym(t) { t instanceof IntExp }
    get_slot(value,t) { ((IntExp)t).value }
  }

    // ------------------------------------------------------------
  
  public final static void main(String[] args) {
    Record test = new Record();
    test.test1();
//    test.test2();
    test.test3();
    test.test4();
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

/*
  public Exp simplifiedExp1() {
    return new IntExp(20);
  }
  
  public Exp simplifiedExp2() {
    return new StringExp("a");
  }

  public Exp simplifiedExp3() {
    return new IntExp(0);
  }
*/
  public Exp simplifiedExp4() {
    return new Uminus(new StringExp("a"));
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
/*  
  public void test2() {
    Exp e = buildExp2();
    String s1 = prettyPrint(e);
    String s2 = prettyPrintInv(e);
    String s3 = prettyPrint(traversalSimplify(e));

    System.out.println("prettyPrint: " + s1);
    System.out.println("prettyPrintInv: " + s2);
    System.out.println("simplify: " + s3);
  }
*/
  public void test3() {
    Exp e = buildExp3();

    String s1 = prettyPrint(e);
    String s2 = prettyPrintInv(e);
    String s3 = prettyPrint(traversalSimplify(e));

    System.out.println("prettyPrint: " + s1);
    System.out.println("prettyPrintInv: " + s2);
    System.out.println("simplify: " + s3);
  }

    public void test4() {
    Exp e = simplifiedExp4();

    String s1 = prettyPrint(e);
    String s2 = prettyPrintInv(e);
    String s3 = prettyPrint(traversalSimplify(e));

    System.out.println("prettyPrint: " + s1);
    System.out.println("prettyPrintInv: " + s2);
    System.out.println("simplify: " + s3);
  }


  public String prettyPrint(Exp t) {
    String op = t.getOperator();
    %match(TomExp t) {
      //  return "" + value;
      // it is not possible to match with a CstExp expression because the CstExp class
      // has not un attribute like "value" so the print result will be " " 
      //CstExp -> { return op; }
      IntExp[]  -> { return op; }
      StringExp[]  -> { return op; }
      
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
      //  return "" + value;
      IntExp[]  -> { return op; }
      StringExp[]  -> { return op; }
      
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
        // -- cast on t
        System.out.println("t is istanceof Exp = " + (t instanceof Exp));
        System.out.println("t is istanceof UnaryOperator = " + (t instanceof UnaryOperator));
        System.out.println("t is istanceof BinaryOperator = " + (t instanceof BinaryOperator));      
        ((UnaryOperator)t).first  = traversalSimplify(`e1);
        System.out.println("t is istanceof Exp = " + (t instanceof Exp));
        System.out.println("t is istanceof UnaryOperator = " + (t instanceof UnaryOperator));
        // ask for the expression value
        return simplify(t);
      }
      
      BinaryOperator[first=e1, second=e2] -> {
        // cast on t
        ((BinaryOperator)t).first  = traversalSimplify(`e1);
        ((BinaryOperator)t).second = traversalSimplify(`e2);
        // ask for the expression value
        return simplify(t);
      }
    }
    return t;
  }

  public Exp simplify(Exp t) {
    %match(TomExp t) {
      // if there are 2 ints then return the addition
      Plus[first=IntExp(v1), second=IntExp(v2)] -> {
        return new IntExp(`v1 + `v2);
      }

      // if one of them is 0 then return the other
      Plus[first=e1, second=IntExp(0)] -> { return `e1; }
      Plus[second=e1, first=IntExp(0)] -> { return `e1; }

      // if both of them are 0 then return 0
      Plus[first=e1, second=Uminus(e2)] -> {
        if(`myEquals(e1,e2)) {
          return new IntExp(0);
        } else {
          return t;
        }
      }

      // if there are 2 ints then return the multiplication 
      Mult[first=IntExp(v1), second=IntExp(v2)] -> {
        return new IntExp(`(v1 * v2));
      }
     
      // if one of them is 1 then return teh other
      Mult[first=e1, second=IntExp(1)] -> { return `e1; }
      Mult[second=e1, first=IntExp(1)] -> { return `e1; }
    }
    return t;
  }

  
  public boolean myEquals(Exp t1, Exp t2) {
    %match(TomExp t1, TomExp t2) {
      // if both of them are int with the same value then return true
      IntExp[value=e1], IntExp[value=e2]       -> { return `e1==`e2; }
      // if both of them are string with the same value the return true
      StringExp[value=e1], StringExp[value=e2] -> { return `e1.equals(`e2); }
      
      // if both of them are positive/negative with the same value then return true
      UnaryOperator[first=e1], UnaryOperator[first=f1] -> {
        return t1.getOperator().equals(t2.getOperator()) && `myEquals(e1,f1);
      }
     
      // if both of them are addition/multiplication with the same operands then return true
      BinaryOperator[first=e1, second=e2], BinaryOperator[first=f1, second=f2] -> {
        return t1.getOperator().equals(t2.getOperator()) && `myEquals(e1,f1) && `myEquals(e2,f2);
      }

    }
    return false;
  }

  public boolean equalsOperands(Exp t1, Exp t2) {
    %match(TomExp t1, TomExp t2) {
      BinaryOperator[first=e1], UnaryOperator[first=f1] -> {
        return `myEquals(e1,f1);
      }
    }
    return false;
  }

}

