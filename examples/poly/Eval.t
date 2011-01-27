/*
 * Copyright (c) 2004-2011, INPL, INRIA
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
package poly;

import java.util.*;
import poly.polynom.types.*;
import tom.library.sl.VisitFailure;

public class Eval {

  %include { polynom/polynom.tom }
  %include { util/types/Collection.tom }
  %typeterm ParamMap {
    implement      { java.util.Map<Poly,Poly> }
    is_sort(t)      { $t instanceof java.util.Map }
    equals(l1,l2)  { $l1.equals($l2) }
  }
  %include { sl.tom }

  public static void main(String[] arg) {
    Poly test = `Mult(Plus(Variable("a"),Parameter("a")),Number(2),Max(Variable("a"),Number(5)));
    prettyPrint(test);
    prettyPrint(applySubst(test,`Variable("a"),`Number(6)));

    // tests the flattening of variadic operators
    Poly testaplat = `Plus(Plus(Plus(), Number(2), Number(3)),Plus(Plus(Number(4)),Plus(Parameter("a"))));
    prettyPrint(testaplat);

    // tests the distributine normal form computation
    Poly testdistrib = `Mult(
                          Plus(
                            Max(Number(1),Parameter("a")),
                            Variable("x")),
                          Variable("y")
                        );
    prettyPrint(testdistrib);
    testdistrib = distribute(testdistrib);
    prettyPrint(testdistrib);

    // collects all variables...
    Collection set = variableSet(testdistrib);
    System.out.println(set);

    // and substitute them with a value
    Iterator it = set.iterator();
    int i = 7;
    while(it.hasNext()) {
      testdistrib = applySubst(testdistrib,(Poly)it.next(),`Number(i));
      i++;
    }
    prettyPrint(testdistrib);

    // and evaluate the polynom with parameter a = 10
    Map<Poly,Poly> param = new HashMap<Poly,Poly>();
    param.put(`Parameter("a"),`Number(10));
    int val = eval(param,testdistrib);
    System.out.println("Evaluation = "+val);

  }

  /**
   * This strategy applies a given substitution at the root of the subject
   */
  %strategy Substitute(var:Poly,value:Poly) extends Identity() {
    visit Poly {
      x@(Variable|Parameter)[] -> { if (`x == var) { return value; } }
    }
  }

  /**
   * Apply the substitution at every position in the subject.
   *
   * @param subject  the subject where the substitution is performed
   * @param variable the variable to substitute
   * @param value    the value to give to the variable
   * @return subject with all variable occurences replaced by value
   */
  public static Poly applySubst(Poly subject, Poly variable, Poly value) {
    try {
    return (Poly)`TopDown(Substitute(variable,value)).visitLight(subject);
    } catch(VisitFailure e) { return subject; }
  }

  /**
   * This strategy applies the distribution rules for the Mult, Plus and Max
   * operators.
   */
  %strategy Distribute() extends Identity() {
    visit Poly {
      Mult(A*,Plus(x, X*),B*) -> {
        return `Plus( Mult(A*,x,B*), Mult(A*,Plus(X*),B*));
      }
      Plus(A*,Max(x,X*),B*) -> {
        return `Max( Plus(A*,x,B*), Plus(A*,Max(X*),B*));
      }
      Mult(A*,Max(x,X*),B*) -> {
        return `Max( Mult(A*,x,B*), Mult(A*,Max(X*),B*));
      }
    }
  }

  /**
   * Repeat the application of the distribution rules to reach a fix point.
   * @param subject the Poly to put in normal form for distributivity
   * @return a Poly in normal form
   */
  public static Poly distribute(Poly subject) {
    try {
    return (Poly) `InnermostId(Distribute()).visitLight(subject);
    } catch(VisitFailure e) { return subject; }
  }

  /**
   * Prints the subject.
   * It is usefull when combined with other strategies
   */
  %strategy Print() extends Identity() {
    visit Poly {
      x -> { prettyPrint(`x); }
    }
  }

  /**
   * Prints a Poly to stdout in a human readable manner.
   * @param subject the Poly to print
   */
  public static void prettyPrint(Poly subject) {
    prettyPrintAux(subject);
    System.out.println();
  }

  private static void prettyPrintAux(Poly subject) {
    %match(Poly subject) {
      Number[value=i] -> { System.out.print(`i); }
      (Parameter|Variable)[name=name] -> {
        System.out.print(`name); return;
      }
      Plus(x) -> { prettyPrintAux(`x); return; }
      Mult(x) -> { prettyPrintAux(`x); return; }
      Max(x) -> { prettyPrintAux(`x); return; }
      Plus(X*) -> {
        Poly tmp = `X*;
        System.out.print("(");
        %match(Poly tmp) {
          Plus(_*,x,A*) -> {
            prettyPrintAux(`x);
            if (!`A*.isEmptyPlus()) {
              System.out.print(" + ");
            }
          }
        }
        System.out.print(")");
        return;
      }
      Mult(X*) -> {
        Poly tmp = `X*;
        System.out.print("(");
        %match(Poly tmp) {
          Mult(_*,x,A*) -> {
            prettyPrintAux(`x);
            if (!`A*.isEmptyMult()) {
              System.out.print(" * ");
            }
          }
        }
        System.out.print(")");
        return;
      }
      Max(X*) -> {
        Poly tmp = `X*;
        System.out.print("Max(");
        %match(Poly tmp) {
          Max(_*,x,A*) -> {
            prettyPrintAux(`x);
            if (!`A*.isEmptyMax()) {
              System.out.print(", ");
            }
          }
        }
        System.out.print(")");
        return;
      }
    }
  }

  %strategy CollectVariables(bag:Collection) extends Identity() {
    visit Poly {
      v@Variable[] -> {
        // it will not count multiple occurences, due to maximal sharing
        bag.add(`v);
      }
    }
  }

  /**
   * Compute the set of variabbles occuring in a given Poly.
   *
   * @param p a Poly that may contain some variables
   * @return a Collection of all Variables occuring in p
   */
  public static Collection variableSet(Poly p) {
    Collection bag = new HashSet();
    try {
      `BottomUp(CollectVariables(bag)).visitLight(p);
    } catch(VisitFailure e) {}
    return bag;
  }

  /**
   * Evaluate a given node if possible, and fails otherwise.
   */
  %strategy Evaluate(env:ParamMap) extends Fail() {
    visit Poly {
      Variable[] -> {
        throw new RuntimeException("Uninstantiated variable");
      }
      p@Parameter[] -> {
        if(env.containsKey(`p)) {
          return env.get(`p);
        } else {
          throw new RuntimeException("Uninstantiated parameter");
        }
      }
      n@Number[] -> {
        return `n;
      }
      Plus(Number(x),Number(y)) -> {
        return `Number(x + y);
      }
      Mult(Number(x),Number(y)) -> {
        return `Number(x * y);
      }
      Max(Number(x),Number(y)) -> {
        return (`x>`y)?(`Number(x)):(`Number(y));
      }
    }
  }

  /**
   * Evaluate a Poly using the given values for parameters.
   * It is assumed that all Variables in the subject have been substituted, and
   * that all Parameters have a value in the evaluation environment.
   *
   * @param parameters a Map giving numeric values to each Parameter occuring
   * in p
   * @param p the Poly to evaluate
   * @return the value of the evaluation
   */
  public static int eval(Map<Poly,Poly> parameters, Poly p) {
    try {
      p = `BottomUp(Evaluate(parameters)).visitLight(p);
    } catch(VisitFailure e) {}
    %match(Poly p) {
      Number(n) -> { return `n; }
    }
    throw new RuntimeException("Evaluation failed for "+p);
  }
}
