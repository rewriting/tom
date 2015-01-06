/*
 * Copyright (c) 2004-2015, Universite de Lorraine, Inria
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 *	- Redistributions of source code must retain the above copyright
 *	notice, this list of conditions and the following disclaimer.  
 *	- Redistributions in binary form must reproduce the above copyright
 *	notice, this list of conditions and the following disclaimer in the
 *	documentation and/or other materials provided with the distribution.
 *	- Neither the name of the Inria nor the names of its
 *	contributors may be used to endorse or promote products derived from
 *	this software without specific prior written permission.
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

package lambdacalculi;

import lambdacalculi.lambdaupsilon.upsilon.*;
import lambdacalculi.lambdaupsilon.upsilon.types.*;

class LambdaUpsilon {

  %gom {
    module upsilon
    imports String

    abstract syntax

      LTerm = lappl(fun:LTerm, arg:LTerm)
            | lambda(t:LTerm)
            | subst(t:LTerm, s:Subst)
            | one()
            | suc(n:LTerm)
            | constant(name:String)
    
      Subst = slash(t:LTerm)
            | lift(s:Subst)
            | shift()
    
      module upsilon:rules() {
        lappl(lambda(a),b) -> subst(a,slash(b))
        subst(lappl(a,b),s) -> lappl(subst(a,s),subst(b,s))
        subst(lambda(a),s) -> lambda(subst(a,lift(s)))
        subst(one(),slash(a)) -> a
        subst(suc(n),slash(a)) -> n
        subst(one(),lift(s)) -> one()
        subst(suc(n),lift(s)) -> subst(subst(n,s),shift())
        subst(n,shift()) -> suc(n)
      }
    
      sort LTerm:block() {
        public String toString() {
          %match(this) {
            lappl(t1,t2) -> { return %[(@`t1@ @`t2@)]%; }
            lambda(t) -> { return %[\@`t@ ]%; }
            subst(t,s) -> { return %[@`t@[@`s@]]%; }
            one() -> { return "1"; }
            n@suc(p) -> { 
              if(`isInt(n)) return "" + `toInt(n);
              else return %[suc(@`p@)]%;
            }
            constant(name) -> { return `name; }
          }
          return "";
        }
        private boolean isInt(LTerm n) {
          %match(n) {
            suc(m) -> { return isInt(`m); }
            one() -> { return true; }
          }
          return false;
        }
        private int toInt(LTerm n) {
          %match(n) {
            suc(m) -> { return toInt(`m) + 1; }
            one() -> { return 1; }
          }
          return -1;
        }
      }
    
      sort Subst:block() {
        public String toString() {
          %match(this) {
            slash(t) -> { return `t+"/"; }
            lift(s) -> { return "\"("+`s+")"; }
            shift() -> { return "|"; }
          }
          return "";
        }
      }
  }


  public static void main(String [] argv) {
    LTerm t1 = `lappl(lambda(lappl(one(),one())),constant("a"));
    LTerm swap = `lambda(lambda(lambda(
                    lappl(lappl(suc(suc(one())),one()),suc(one())))));
    
    LTerm t2 = 
      `lappl(lappl(lappl(swap,constant("f")),constant("a")),constant("b"));

    LTerm t3 = `lappl(swap,constant("f"));
    tom.library.utils.Viewer.display(t3);
    tom.library.utils.Viewer.toTree(t3);

    System.out.println("(\\x.(x x) a) = " + t1);
    System.out.println("swap = \\fxy.(f y x) = " + swap);
    System.out.println("swap f a b = " + t2);
    System.out.println("swap f = " + t3);
  }
}
