/*
 * Copyright (c) 2004-2011, INPL, INRIA
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

public class RecordStrict {

  %include {int.tom}
  %include {string.tom}
  
  %typeterm Exp {
    implement { Exp }
    is_sort(t) { $t instanceof Exp }
    equals(t1,t2) {$t1.equals($t2)}
  }

    // ------------------------------------------------------------
  
  %op Exp BinaryOperator(first:Exp, second:Exp) {
    is_fsym(t) { $t instanceof BinaryOperator }
    get_slot(first,t) { ((BinaryOperator)$t).first }
    get_slot(second,t) { ((BinaryOperator)$t).second }
  }

  %op Exp UnaryOperator(first:Exp) {
    is_fsym(t) { $t instanceof UnaryOperator }
    get_slot(first,t) { ((UnaryOperator)$t).first }
  }

  %op Exp Plus(first:Exp, second:Exp) {
    is_fsym(t) { $t instanceof Plus }
    get_slot(first,t) { ((Plus)$t).first }
    get_slot(second,t) { ((Plus)$t).second }
  }

  %op Exp Mult(first:Exp, second:Exp) {
    is_fsym(t) { $t instanceof Mult }
    get_slot(first,t) { ((Mult)$t).first }
    get_slot(second,t) { ((Mult)$t).second }
  }

  %op Exp Uminus(first:Exp) {
    is_fsym(t) { $t instanceof Uminus }
    get_slot(first,t) { ((Uminus)$t).first }
  }

  %op Exp CstExp() {
    is_fsym(t) { $t instanceof CstExp }
  }

  %op Exp StringExp(value:String) {
    is_fsym(t) { $t instanceof StringExp }
    get_slot(value,t) { ((StringExp)$t).value }
  }


  %op Exp IntExp(value:int) {
    is_fsym(t) { $t instanceof IntExp }
    get_slot(value,t) { ((IntExp)$t).value }
  }

    // ------------------------------------------------------------
  
  public final static void main(String[] args) {
    RecordStrict test = new RecordStrict();
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
      CstExp() -> { return op; }

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
    %match(Exp t) {
      CstExp() -> { return op; }
      
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
    %match(Exp t) {
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
    %match(Exp t) {
      Plus[first=IntExp(v1), second=IntExp(v2)] -> {
        return new IntExp(`v1 + `v2);
      }

      Plus[first=e1, second=IntExp(0)] -> { return `e1; }
      Plus[second=e1, first=IntExp(0)] -> { return `e1; }

      Plus[first=e1, second=Uminus(e2)] -> {
        if(`myEquals(e1,e2)) {
          return new IntExp(0);
        } else {
          return t;
        }
      }

      Mult[first=IntExp(v1), second=IntExp(v2)] -> {
        return new IntExp(`(v1 * v2));
      }
      
      Mult[first=e1, second=IntExp(1)] -> { return `e1; }
      Mult[second=e1, first=IntExp(1)] -> { return `e1; }
    }
    return t;
  }

  
  public boolean myEquals(Exp t1, Exp t2) {
    %match(Exp t1, Exp t2) {
      IntExp[value=e1], IntExp[value=e2]       -> { return `e1==`e2; }
      
      StringExp[value=e1], StringExp[value=e2] -> { return `e1.equals(`e2); }
      
      UnaryOperator[first=e1], UnaryOperator[first=f1] -> {
        return t1.getOperator().equals(t2.getOperator()) && `myEquals(e1,f1);
      }
      
      BinaryOperator[first=e1, second=e2], BinaryOperator[first=f1, second=f2] -> {
        return t1.getOperator().equals(t2.getOperator()) && `myEquals(e1,f1) && `myEquals(e2,f2);
      }
      
    }
    return false;
  }

 
}

