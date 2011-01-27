/*
 * Copyright (c) 2004-2011, INPL, INRIA
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
import termgraph.lambdaterm.types.lambdaterm.*;

public class LambdaCalculus {

  private static int comptVariable = 0;	
  %include { sl.tom }
  %include {lambdaterm/lambdaterm.tom}
  //%include {lambdaterm/_lambdaterm.tom}
  %include {util/HashMap.tom}


  public final static void main(String[] args) {
    LambdaTerm subject = `var("undefined");
    LambdaInfo info = new LambdaInfo();
    
    // beta with refs (designed for call by need)
    Strategy betaRef = `Sequence(
        _app(Identity(),DeRef(collectTerm(info))), 
        _app(Sequence(
            collectPosition(info),
            _abs2(Mu(MuVar("x"),Choice(substitute(info),All(MuVar("x")))))),Identity()),
        clean(info));

    // beta without refs
    Strategy beta = `Sequence(
        _app(Identity(),collectTerm(info)), 
        _app(Sequence(
            collectPosition(info),
            _abs2(Mu(MuVar("x"),Choice(substitute(info),All(MuVar("x")))))),Identity()),
        clean(info));

    String s;
    LambdaTermLexer lexer = new LambdaTermLexer(System.in); // Create parser attached to lexer
    LambdaTermParser parser = new LambdaTermParser(lexer);
    while(true){
      System.out.print("> ");

      // term parsing
      try {
        subject = parser.lambdaterm();
      } catch (Exception e) {
        System.out.println(e);
      }
     
      // parsed term
      System.out.println("Orginal term: "+prettyPrint(subject));

      // call by name
      info = new LambdaInfo();
      try{
        System.out.println("Call by name: "+prettyPrint((LambdaTerm)`Not(Sequence( RepeatId(TopDown(Try(beta))),OnceTopDown(beta) )).visit(subject)));
      }catch (VisitFailure e){
        System.out.println("Call by name: Infinite loop");
      }

      // call by need
      info.lazy=true;
      try{
        System.out.println("Call by need: "+prettyPrint((LambdaTerm)`Not(Sequence( RepeatId(TopDown(Try(betaRef))),OnceTopDown(betaRef) )).visit(subject)));
      }catch(VisitFailure e){
        System.out.println("Call by need: Infinite loop");
      }

      // call by value
      info.lazy=false;
      try{
        System.out.println("Call by value: "+prettyPrint((LambdaTerm)`InnermostRight(beta).visit(subject)));
      } catch(java.lang.StackOverflowError e){
        System.out.println("Call by value: Infinite loop");
      } catch(VisitFailure f) {
        System.out.println("Failure");
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
        for(int i = n; i>0; i--){
          getEnvironment().down(i);
          int status = s.visit(VisitableIntrospector.getInstance());
          if(status != Environment.SUCCESS){
            getEnvironment().up();
            return `x;
          }else{
            getEnvironment().up();
          }
        }
      }
    }
  }

  %op Strategy InnermostRight(s1:Strategy) {
    make(v) { `mu(MuVar("_x"),Sequence(AllRightSeq(MuVar("_x")),Try(Sequence(v,MuVar("_x"))))) }
  }


  %typeterm LambdaInfo{
    implement {LambdaInfo}
    is_sort(t)     { t instanceof LambdaInfo }
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
      p@PathLambdaTerm(_*) -> {
        Position source = getEnvironment().getPosition();
        Position dest = (Position) source.add((Path)`p);
        if(dest.equals(info.omega)){
          if(info.firstOccur==null || !info.lazy){
            info.firstOccur = getEnvironment().getPosition();
            return info.term;
          }
          else{
            Position target = info.firstOccur;
            return ConsPathLambdaTerm.make(target.sub(source));
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
    try {
      t = (LambdaTerm) `TopDownSeq(UnExpand()).visit(t);
      %match(LambdaTerm t){
        app(term1,term2) -> {return "("+prettyPrint(`term1)+"."+prettyPrint(`term2)+")";}
        abs3(term1,term2) -> {return "("+prettyPrint(`term1)+"->"+prettyPrint(`term2)+")";}
        var(s) -> {return `s;}
      }
    } catch(VisitFailure f) {
      System.out.println("Failure");
    }
    return "";
  }

  static int ppcounter = 0;

  %strategy Debug(s:String) extends `Identity() {
    visit LambdaTerm {
      _ -> {
        System.out.println(s + " " + getEnvironment());
      }
    }
  }

  %strategy UnExpand() extends `Identity() {
    visit LambdaTerm {
      abs2(term) -> {
        String v = "x" + (ppcounter++);
        return `abs3(var(v),term);
      }
      p@PathLambdaTerm(_*)-> {
        //test if it is a cycle to a lambda
        //it can be a ref corresponding to a sharing due to lazy evaluation
        Path pp = (Path)`p;
        if(pp.length()==1){
          getEnvironment().followPath(pp);
          LambdaTerm var = ((LambdaTerm)getEnvironment().getSubject()).getvar();
          getEnvironment().followPath(pp.inverse());
          return var;
        }
        else{
          Position source = getEnvironment().getPosition();
          Position target = (Position) source.add((Path)`p);
          return (LambdaTerm) target.getSubterm().visit((Visitable) getEnvironment().getRoot());
        }
      }

    }
  }
}


