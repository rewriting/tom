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
package termgraph;

import termgraph.matching.term.types.*;
import termgraph.matching.term.types.pos.conc;
import tom.library.sl.*;

public class Matching {

  %include{ sl.tom }

  %gom(--termgraph) {
    module Term
      imports String int
      abstract syntax
      Term = var(n:String)
           | a()
           | f(t:Term)
           | g(t1:Term, t2:Term)
           | True()
           | False()
           | Match(pattern:Term, subject:Term, omega:Pos, omega2:Pos)
           | And(Term*)
           | Solve(matchconstraints: Term, context: Term)

      Pos = conc(int*)

      And:AU() { `True() }

  }

  public Term resolve(Term solve, Term originalSubject) {
    try {
      return (Term) `Repeat(oneStepSolve(originalSubject)).visitLight(solve);
    } catch (VisitFailure e) {
      throw new RuntimeException("failure");
    }
  }


  %strategy oneStepSolve(originalSubject:Term) extends Fail()  {

    visit Term {
      // Delete
      Solve(And?(X*,m@Match(a(),a(),_,_),Y*),context) -> { return `Solve(And(X,Y),And(context,m));}

      // Decompose
      Solve(And?(X*,m@Match(f(x),f(y),omega,omega2),Y*),context) -> { 
        return `Solve(And(X*,Match(x,y,conc(omega,1),conc(omega2,1)),Y*),And(context,m));
      }
      Solve(And?(X*,m@Match(g(x1,x2),g(y1,y2),omega,omega2),Y*),context) -> {
        return `Solve(And(X*,Match(x1,y1,conc(omega,1),conc(omega2,1)),Match(x2,y2,conc(omega,1),conc(omega2,1)),Y*),And(context,m));
      }

      // SymbolClash
      Solve(And?(X*,Match(f(_),a(),_,_),Y*),context) -> { return `False() ;}
      Solve(And?(X*,Match(a(),f(_),_,_),Y*),context) -> { return `False() ;}
      Solve(And?(X*,Match(g(_,_),a(),_,_),Y*),context) -> { return `False() ;}
      Solve(And?(X*,Match(a(),g(_,_),_,_),Y*),context) -> { return `False() ;}
      Solve(And?(X*,Match(f(_),g(_,_),_,_),Y*),context) -> { return `False() ;}
      Solve(And?(X*,Match(g(_,_),f(_),_,_),Y*),context) -> { return `False() ;}

      // Dereferencing
      Solve(And?(X*,m@Match(t,p@PathTerm(_*),omega,omega2),Y*),context) -> {
        conc omega2 = (conc) `omega2;
        int[] array = new int[omega2.size()];
        int i =0;
        while (! omega2.isEmpty()) {
          array[i] = omega2.getHeadconc();
          i++;
          omega2 = (conc) omega2.getTailconc(); 
        }
        Position src = Position.makeFromArray(array);
        Position dest = (Position) src.add((Path)`p).getCanonicalPath();
        Term refTerm = (Term) dest.getSubterm().visit(originalSubject);
        int [] destarray = dest.toIntArray(); 
        Pos destconc = `conc();
        for (int k=0; k<dest.length();k++) {
          destconc = `conc(destconc*,destarray[k]);
        }
        return `Solve(And(X*,Match(t,refTerm,omega,destconc),Y*),And(context,m));
      }

      // Fix Point
      Solve(And?(X*,m@Match(p@PathTerm(_*),t@!PathTerm(_*),omega,omega2),Y*),context@And(_*,Match(_,_,omega1,omega2),_*)) -> {
        conc omega = (conc) `omega;
        conc omega1 = (conc) `omega1;
        int[] array = new int[omega.size()];
        int i =0;
        while (! omega.isEmpty()) {
          array[i] = omega.getHeadconc();
          i++;
          omega = (conc) omega.getTailconc(); 
        }
        Position src = Position.makeFromArray(array);
        Position dest = (Position) src.add((Path)`p).getCanonicalPath();
        array = new int[omega1.size()];
        i =0;
        while (! omega1.isEmpty()) {
          array[i] = omega1.getHeadconc();
          i++;
          omega1 = (conc) omega1.getTailconc(); 
        }
        Position newdest = Position.makeFromArray(array);
        if (dest.equals(newdest)) {
          return `Solve(And(X*,Y*),And(context,m));
        }
      }
      Solve(True(),_) -> {
        return `True();
      }
    }


  }


  public void run() {
    Term t1 = `f(f(PathTerm(-1,-1)));
    Term t2 = `f(PathTerm(-1));
    Term start = `resolve(Solve(Match(t1,t2,conc(),conc()),True()),t2);
    System.out.println("Match("+`t1+","+`t2+") = " + `start);

    t1 = `f(f(PathTerm(-1,-1)));
    t2 = `g(f(PathTerm(-1,-1,2)),f(PathTerm(-1,-2,1)));
    start = `resolve(Solve(Match(t1,f(PathTerm(-1,-1,2)),conc(),conc(1)),True()),t2);
    System.out.println("Match("+`t1+","+`t2+") = " + `start);
    start = `resolve(Solve(Match(t1,f(PathTerm(-1,-2,1)),conc(),conc(2)),True()),t2);
    System.out.println("Match("+`t1+","+`t2+") = " + `start);

    t1 = `f(f(f(f(f(f(f(f(f(f(PathTerm(-1,-1,-1,-1,-1,-1)))))))))));
    t2 = `f(f(f(f(f(f(PathTerm(-1,-1,-1)))))));
    start = `resolve(Solve(Match(t1,t2,conc(),conc()),True()),t2);
    System.out.println("Match("+`t1+","+`t2+") = " + `start);
    start = `resolve(Solve(Match(t2,t1,conc(),conc()),True()),t1);
    System.out.println("Match("+`t2+","+`t1+") = " + `start);


  }

  public final static void main(String[] args) {
    Matching test = new Matching();
    test.run();
  }

}
