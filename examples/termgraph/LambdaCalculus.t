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
    Strategy betaLeft = `Sequence(
        _app(Identity(),RelativeRef(collectTerm(info))), 
        _app(Sequence(
            collectPosition(info),
            _abs2(Mu(MuVar("x"),Choice((substitute(info)),All(MuVar("x")))))),Identity()),
        clean(info));
    Strategy betaRight = `Sequence(
        _app(Identity(),RelativeRef(collectTerm(info))), 
        _app(Sequence(
            collectPosition(info),
            _abs2(Mu(MuVar("x"),Choice((substitute(info)),AllRightSeq(MuVar("x")))))),Identity()),
        clean(info));


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
      System.out.println("Orginal term: "+prettyPrint(subject));
      try{
        System.out.println("Call by name: "+prettyPrint((LambdaTerm)`RepeatId(TopDown(Try(betaLeft))).fire(subject)));
      }catch(java.lang.StackOverflowError e){
        System.out.println("Call by name: Infinite loop");
      }
      info.lazy=true;
      try{
        System.out.println("Call by need: "+prettyPrint((LambdaTerm)`RepeatId(TopDown(Try(betaLeft))).fire(subject)));
      }catch(java.lang.StackOverflowError e){
        System.out.println("Call by need: Infinite loop");
      }
      try{
        System.out.println("Call by value: "+prettyPrint((LambdaTerm)`InnermostId(betaRight).fire(subject)));
      }catch(java.lang.StackOverflowError e){
        System.out.println("Call by value: Infinite loop");
      }
    }
  }

  %strategy clean(info:LambdaInfo) extends `Identity() {
    visit LambdaTerm {
      app(abs2(arg),_)-> {
        info.firstOccur =null;
        info.term =null;
        info.omega =null;
        return `arg;
      }
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
        info.omega= getEnvironment().getPosition();
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

  //we do not need the sequentiality
  //it is just for the simplicity ;)
  %strategy AllRightSeq(s:Strategy) extends `Identity(){
    visit LambdaTerm{
      x -> {
        int n = `x.getChildCount();
        for(int i = n-1;i>=0;i--){
          getEnvironment().down(i);
          s.visit();
          getEnvironment().up();
        }
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
    public Position omega;
    public LambdaTerm term;
    public Position firstOccur;
    public boolean lazy;
  }

  //[subject/X]t
  %strategy substitute(info:LambdaInfo) extends `Fail(){
    visit LambdaTerm {
      p@posLambdaTerm(_*) -> {
        Position relative = Position.makeRelativePosition(((Reference)`p).toArray());
        Position source = getEnvironment().getPosition();
        Position absolute = source.getAbsolutePosition(relative);        
        if(absolute.equals(info.omega)){
          if(info.firstOccur==null || !info.lazy){
            info.firstOccur = getEnvironment().getPosition();
            return info.term;
          }
          else{
            Position target = info.firstOccur;
            Position relativeInv = source.getRelativePosition(target);
            int[] omega = relativeInv.toArray(); 
            LambdaTerm t = `posLambdaTerm();
            for (int i =0; i<omega.length;i++){
              t = `posLambdaTerm(t*,omega[i]);
            }
            return `t;
          }
        }
        else{
          return `p;
        }
      }
    }
  }

  %op Strategy TopDownSeq(s1:Strategy) {
    make(v) { `mu(MuVar("_x"),Sequence(v,AllSeq(MuVar("_x")))) }
  }
  public static String prettyPrint(LambdaTerm t){
    ppcounter = 0;
    t = (LambdaTerm) `TopDownSeq(UnExpand()).fire(t);
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
        return `abs3(var(v),term);
      }
      p@posLambdaTerm(_*)-> {
        //test if it is a cycle to a lambda
        //it can be a ref corresponding to a sharing due to lazy evaluation
        if(`((Reference)p).toArray().length==1){
          Position relative = Position.makeRelativePosition(((Reference)`p).toArray());
          Position source = getEnvironment().getPosition();
          Position target = source.getAbsolutePosition(relative);
          Position relativeInv = target.getRelativePosition(source);
          getEnvironment().goTo(relative);
          LambdaTerm var = ((LambdaTerm)getEnvironment().getSubject()).getvar();
          getEnvironment().goTo(relativeInv);
          return var;
        }
        else{
          Position relative = Position.makeRelativePosition(((Reference)`p).toArray());
          Position source = getEnvironment().getPosition();
          Position target = source.getAbsolutePosition(relative);
          return (LambdaTerm) target.getSubterm().fire(getEnvironment().getRoot());
        }
      }

    }
  }
}


