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
package antipattern.baralgo;

import aterm.*;
import aterm.pure.*;

import antipattern.*;
import antipattern.term.*;
import antipattern.term.types.*;


import tom.library.sl.*;

import java.util.Collection;

public class SolveSystem extends AbstractStrategyBasic {

  %include{ ../term/Term.tom }
  %include{ sl.tom }

  protected Collection solution; 
  protected boolean isIdentity;

  public SolveSystem(Collection c,Strategy vis) {
    super(vis);
    this.solution = c;
    this.isIdentity = (vis.getClass().equals(`Identity().getClass()) ? 
        true : false );      
  }

  public <T> T visitLight(T o, Introspector i) throws VisitFailure {
    if (o instanceof Constraint) {
      Constraint arg = (Constraint) o;
      %match(Constraint arg) {
        match@Match(var@Variable(name),s) -> {
          solution.add(`match); 
          Constraint res = `True();
          /* System.out.println("[solve1] -> [" + match + "," + res + "]"); */
          return (T) res;
        }
        Neg(match@Match(var@Variable(name),s)) -> {
          solution.add(`match); 
          Constraint res = `False();
          /* System.out.println("[solve1] -> [" + match + "," + res + "]"); */
          return (T) res;
        }
        And(concAnd(X*,match@Match(var@Variable(name),s),Y*)) -> {
          solution.add(`match);
          Strategy rule,ruleStrategy;            
          if (isIdentity){
            rule = new ReplaceSystem(`var,`s, `Identity());
            ruleStrategy = `InnermostId(rule);
          }else{
            rule = new ReplaceSystem(`var,`s, `Fail());
            ruleStrategy = `Innermost(rule);
          }            
          Constraint res = (Constraint) ruleStrategy.visitLight(`And(concAnd(X*,Y*)));
          /* System.out.println("[solve3] -> [" + match + "," + res + "]"); */
          return (T) res;
        }
        Neg(And(concAnd(X*,match@Match(var@Variable(name),s),Y*))) -> {
          solution.add(`match);
          Strategy rule,ruleStrategy;            
          if (isIdentity){
            rule = new ReplaceSystem(`var,`s, `Identity());
            ruleStrategy = `InnermostId(rule);
          }else{
            rule = new ReplaceSystem(`var,`s, `Fail());
            ruleStrategy = `Innermost(rule);
          }
          Constraint res = (Constraint) ruleStrategy.visitLight(`Neg(And(concAnd(X*,Y*))));
          /* System.out.println("[solve4] -> [" + match + "," + res + "]"); */
          return (T) res;
        }
      }
    }
    return (isIdentity ? o : (T)`Fail().visitLight(o,i));
  }

}
