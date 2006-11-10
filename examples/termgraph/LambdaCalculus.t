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

import tom.library.strategy.mutraveler.*;
import jjtraveler.reflective.VisitableVisitor;
import java.util.*;

import termgraph.lambdaterm.*;
import termgraph.lambdaterm.types.*;
import termgraph.lambdaterm.strategy.lambdaterm.*;
import termgraph.lambdaterm.types.lambdaterm.posLambdaTerm;

public class LambdaCalculus {

  private static int comptVariable = 0;	
  %include{java/util/HashMap.tom}
  %include { mustrategy.tom }
  %include {lambdaterm/lambdaterm.tom}
  %include {lambdaterm/_lambdaterm.tom}

  %strategy ToDeBruinj(table:HashMap) extends Identity(){
    visit LambdaTerm{
      v@var[] -> {
        if(table.containsKey(`v)){
          RelativePosition pos = RelativePosition.make(getPosition(),(Position)table.get(`v));
          int[] array = pos.toArray();
          LambdaTerm ref = `posLambdaTerm();
          for(int i=0;i<pos.depth();i++){
            ref = `posLambdaTerm(ref*,array[i]);
          }
          return ref;
        }
      }
    }
  }

  %strategy CollectVar(table:HashMap) extends Fail() {
    visit LambdaTerm{
      abs[var=var,arg=arg] -> {
        table.put(`var,getPosition());
        return `abs2(arg);
      }
    }
  }

  public final static void main(String[] args) {
    LambdaTerm subject = `var("undefined");
    LambdaInfo info = new LambdaInfo();
    MuStrategy beta = `Sequence(
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
        System.out.println(prettyPrinter(subject));
      } catch (Exception e) {
        System.out.println(e);

      }
      System.out.println("Orginal term:"+subject);
      HashMap table = new HashMap();
      subject = (LambdaTerm) `BottomUp(Try((Sequence(CollectVar(table),TopDown(ToDeBruinj(table)))))).apply(subject); 
      System.out.println("Representation in graph term:"+subject);
      System.out.println("After beta-normalisation: "+`RepeatId(TopDown(Try(beta))).apply(subject));
    }
  }

  %strategy removeApp() extends `Identity() {
    visit LambdaTerm {
      app(abs2(arg),_)-> {return `arg;}
    }
  }

  %strategy print() extends `Identity() {
    visit LambdaTerm {
      X -> {System.out.println(prettyPrinter(`X));}
    }
  }

  %strategy collectPosition(info:LambdaInfo) extends `Identity() {
    visit LambdaTerm {
      _ -> {
        info.pos=getPosition();
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
    public Position pos;
    public LambdaTerm term;
  }
  
  //[subject/X]t
  %strategy substitute(info:LambdaInfo) extends `Fail(){
    visit LambdaTerm {
      p@posLambdaTerm(_*) -> {
        RelativePosition relPos = new RelativePosition(((MuReference)`p).toArray());
        if(relPos.getAbsolutePosition(getPosition()).equals(info.pos)){
          return info.term;
        }
        else{
          return `p;
        }
      }
    }
  }

  public static String prettyPrinter(LambdaTerm t){
    %match(LambdaTerm t){
      app(term1,term2) -> {return "("+prettyPrinter(`term1)+"."+prettyPrinter(`term2)+")";}
      abs(term1,term2) -> {return "("+prettyPrinter(`term1)+"->"+prettyPrinter(`term2)+")";}
      var(s) -> {return `s;}
    }
    return "";
  }

}
