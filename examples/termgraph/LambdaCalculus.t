/*
 * Copyright (c) 2004-2006, INRIA
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 *	- Redistributions of source code must retain the above copyright
 *	notice, this conc of conditions and the following disclaimer.  
 *	- Redistributions in binary form must reproduce the above copyright
 *	notice, this conc of conditions and the following disclaimer in the
 *	documentation and/or other materials provided with the distribution.
 *	- Neither the name of the INRIA nor the names of its
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

package termgraph;

import tom.library.sl.*;
import java.util.*;

import termgraph.lambdaterm.*;
import termgraph.lambdaterm.types.*;
import termgraph.lambdaterm.strategy.lambdaterm.*;
import termgraph.lambdaterm.types.lambdaterm.posLambdaTerm;

public class LambdaCalculus {

  private static int comptVariable = 0;	
  %include { sl.tom }
  %include {lambdaterm/lambdaterm.tom}
  %include {lambdaterm/_lambdaterm.tom}
  %include {util/HashMap.tom}

  public final static void main(String[] args) {
    LambdaTerm subject = `var("undefined");
    LambdaInfo info = new LambdaInfo();
    Strategy beta = `Sequence(
        _app(Identity(),collectTerm(info)),
        _app(Sequence(
            collectPosition(info),
            _abs2(Mu(MuVar("x"),Choice((substitute(info)),All(MuVar("x")))))),Identity()),
        removeApp());
    String s;
    LambdaTermLexer lexer = new LambdaTermLexer(System.in); // Create parser attached to lexer
    LambdaTermParser parser = new LambdaTermParser(lexer);
    while(true){
      System.out.print(">");
      try {
        subject = parser.lambdaterm();
      } catch (Exception e) {
        System.out.println(e);

      }
      System.out.println("Orginal term:"+prettyPrint(subject));
      System.out.println("After beta-normalisation: "+`TopDown(Try(beta)).fire(subject));
      System.out.println("After beta-normalisation: "+prettyPrint((LambdaTerm)`TopDown(Try(beta)).fire(subject)));
    }
  }

  %strategy removeApp() extends `Identity() {
    visit LambdaTerm {
      app(abs2(arg),_)-> {return `arg;}
    }
  }

  %strategy print() extends `Identity() {
    visit LambdaTerm {
      X -> {System.out.println(prettyPrint(`X));}
    }
  }

  %strategy collectPosition(info:LambdaInfo) extends `Identity() {
    visit LambdaTerm {
      _ -> {
        info.omega=getEnvironment().getOmega();
      }
    }
  }

  %strategy collectTerm(info:LambdaInfo) extends `Identity() {
    visit LambdaTerm {
      term -> {
        info.term=`term;
      }
    }
  }


  %typeterm Position{
    implement {Position}
  }

  %typeterm LambdaInfo{
    implement {LambdaInfo}
  }


  static class LambdaInfo{
    public int[] omega;
    public LambdaTerm term;
  }
  
  //[subject/X]t
  %strategy substitute(info:LambdaInfo) extends `Fail(){
    visit LambdaTerm {
      p@posLambdaTerm(_*) -> {
        if(OmegaManager.getAbsoluteOmega(getEnvironment().getOmega(),((Reference)`p).toArray()).equals(info.omega)){
          return info.term;
        }
        else{
          return `p;
        }
      }
    }
  }

  public static String prettyPrint(LambdaTerm t){
    t= (LambdaTerm) `TopDown(UnExpand()).fire(t);
    %match(LambdaTerm t){
      app(term1,term2) -> {return "("+prettyPrint(`term1)+"."+prettyPrint(`term2)+")";}
      abs3(term1,term2) -> {return "("+prettyPrint(`term1)+"->"+prettyPrint(`term2)+")";}
      var(s) -> {return `s;}
    }
    return "";
  }
 
  static int ppcounter = 0;

  %strategy Debug() extends `Identity() {
    visit LambdaTerm {
      _ -> {
        System.out.println(getEnvironment());
      }
    }
  }

  %strategy UnExpand() extends `Identity() {
    visit LambdaTerm {
      abs2(term) -> {
        String v = "x" + (ppcounter++);
        System.out.println("replace"+v);
        return `abs3(var(v),term);
      }
      p@posLambdaTerm(_*)-> {
        int[] omega = OmegaManager.getRelativeOmega(OmegaManager.getAbsoluteOmega(getEnvironment().getOmega(),((Reference)`p).toArray()),getEnvironment().getOmega());
        getEnvironment().goTo(((Reference)`p).toArray());
        LambdaTerm var = ((LambdaTerm)getEnvironment().getSubject()).getvar();
        getEnvironment().goTo(omega);
        return var;
      }

    }
  }
}


