/*
 * Copyright (c) 2004-2014, Universite de Lorraine, Inria
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

import lambdacalculi.barendregtconvention.barendregt.types.*;
import tom.library.sl.*;

class BarendregtConvention {

  %include { sl.tom }

  %gom {
    module barendregt
    imports String

    abstract syntax

      LTerm = lappl(fun:LTerm, arg:LTerm)
            | lambda(x:String,t:LTerm)
            | var(x:String)

      lambda:block() {
        static int counter = 0;

        /* the following hooks maintain barendregt's convention
           (no subterm with a variable both free and bound) */
 
        static lambdacalculi.barendregtconvention.barendregt.types.LTerm 
          refresh(String n, lambdacalculi.barendregtconvention.barendregt.types.LTerm lt) {
            %match(String n,lt) {
              x, lappl(t1,t2) -> { return `lappl(refresh(x,t1),refresh(x,t2)); }
              _, v@var[] -> { return `v; }
              x, lambda(y@!x,t) -> { return `realMake(y,refresh(x,t)); }
              x, lambda(x,t) -> { 
                String fresh = `x+(counter++);
                return `realMake(fresh,replace(x,fresh,t));
              }
            }
            throw new RuntimeException();
          }

        static lambdacalculi.barendregtconvention.barendregt.types.LTerm 
          replace(String v, String nv, lambdacalculi.barendregtconvention.barendregt.types.LTerm lt) {
            %match(String v,lt) {
              _, lappl(t1,t2) -> { return `lappl(replace(v,nv,t1),replace(v,nv,t2)); }
              x, var(x) -> { return `var(nv); }
              x, y@var(!x) -> { return `y; }
              x, lambda(y,t) -> { return `realMake(y,replace(v,nv,t)); }
            }
            throw new RuntimeException();
          }
      }

      lambda:make(x,t) {
        return `realMake(x,refresh(x,t));
      }

      sort LTerm:block() {
        public String toString() {
          %match(this) {
            lappl(t1,t2) -> { return %[(@`t1@ @`t2@)]%; }
            lambda(x,t) -> { return %[\@`x@.@`t@]%; }
            var(x) -> { return `x; }
          }
          throw new RuntimeException();
        }
      }
  }


  /* definition of beta-reduction is then very natural */

  %strategy Beta() extends Fail() {
    visit LTerm {
      t@lappl(lambda(x,t1),t2) -> { 
        return (LTerm) `mu(MuVar("s"),Subst(All(MuVar("s")),x,t2)).visit(`t1); 
      }
    }
  }

  %strategy Subst(def:Strategy,x:String,t:LTerm) extends def {
    visit LTerm { 
      var(v) -> { if (`v == x) return `t; } 
    }
  }

  /**
   * builds the term obtained if the computation 
   * stopped here 
   **/
  %strategy Debug() extends Identity() {
    visit LTerm {
      _ -> {
        Environment env;
        try { 
          env = (Environment) getEnvironment().clone(); 
          while(env.depth() > 0) env.up();
          System.out.println(" -> " + env.getSubject()); 
        }
        catch (CloneNotSupportedException e) {}
      }
    }
  }

  public static void normalize(LTerm lt) throws VisitFailure {
    System.out.println(lt);
    lt = (LTerm) `Outermost(Sequence(Beta(),Debug())).visit(lt);
    System.out.println(lt + "\n");
  }

  public static void main(String [] argv) throws VisitFailure {
    /* these two terms present potential variable capures */
    LTerm lt1 = `lappl( lambda("x",lappl(var("x"),var("x"))), lambda("x",lambda("y",lappl(var("x"),var("y")))));
    LTerm lt2 = `lambda("y",lappl(lambda("x",lambda("y",lappl(var("f"),var("x")))),var("y")));

    normalize(lt1);
    normalize(lt2);
  }
}
