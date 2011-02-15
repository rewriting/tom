/*
 * Copyright (c) 2005-2011, INPL, INRIA
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

package antipattern.associative;

import aterm.*;
import aterm.pure.*;
import antipattern.*;

import java.io.*;
import java.util.*;

import antipattern.associative.termaso.*;
import antipattern.associative.termaso.types.*;


import tom.library.sl.*;

public class Tools {

  %include{ termaso/TermAso.tom }
  %include{ sl.tom }	
  %include{ aterm.tom }

  public static void main(String[] args) {
    Tools tools = new Tools();
    System.out.println("\nRunning ClassicalAsso: \n");
    ClassicalAssociativity testAsso = new ClassicalAssociativity();
    tools.run(testAsso,args[0]);
  }

  public void run(ClassicalAssociativity match, String fileName) {
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader( 
          match.getClass().getResource(fileName).getFile()));
    } catch(FileNotFoundException e) {
      System.out.println("Can't find the input file 'input.txt' :" + e.getMessage());
      System.exit(0);
    }
    String s = null;
    try {
      while((s = br.readLine()) != null ) {      
        if(s.equals("-")) {
          System.out.println("---------------------------------------");
        } else {
          ATerm at = SingletonFactory.getInstance().parse(s);	        
          Constraint c = atermToConstraint(at);          
          Collection solution = new HashSet();
          System.out.println(formatConstraint(c));
          Constraint simplifiedConstraint = match.simplifyAndSolve(c,solution);
          System.out.println(" --> " + formatConstraint(simplifiedConstraint));
          if(simplifiedConstraint == `True() && !solution.isEmpty()) {
            System.out.println(" sol = " + solution);
          }
        }
      }
    } catch(IOException e1) {
      System.out.println("IOException: " + e1.getMessage()); 
      System.exit(0);
    }

  }    

  private Term atermToTerm(ATerm at) {
    if(at instanceof ATermAppl) {
      ATermAppl appl = (ATermAppl) at;
      AFun afun = appl.getAFun();
      String name = afun.getName();
      if(name.equals("anti") && afun.getArity()==1) {
        ATerm subterm = appl.getArgument(0);
        return `Anti(atermToTerm(subterm));
//      }else if(name.startsWith("e_") && afun.getArity()==0){
//      return `Variable(name);
      } else if(Character.isUpperCase(name.charAt(0)) && afun.getArity()==0) {
        // check if it is a constrained variable
        if (name.length() > 2 && name.charAt(name.length()-2) == '_'){
          return `ConsVariable(Variable(name),name.charAt(name.length()-1) + "_a");
        }
        return `Variable(name);        
      } else {
        String strTmp = "cons";
        ATermList args = appl.getArguments();
        %match(ATermList args, String strTmp){
          concATerm(s1*,ATermAppl(AFun(tmp,_,_),cs)),tmp ->{    		  
            return `ApplCons(name,atermListToTermList(s1),
                atermListToConstraintList(cs));
          }
        }
        return `Appl(name,atermListToTermList(appl.getArguments()));
      }
    }else if(at instanceof ATermInt) {
      String intVal = ((ATermInt)at).getInt()+"";
      return `Appl( intVal,concTerm());
    }

    throw new RuntimeException("error on: " + at);
  }

  private Constraint atermListToConstraintList(ATerm at) {
    if(at instanceof ATermList) {
      ATermList atl = (ATermList) at;
      Constraint l = `And();
      while(!atl.isEmpty()) {
        l = `And(atermToConstraint(atl.getLast()),l*);
        atl = atl.getPrefix();
      }
      return l;
    }

    throw new RuntimeException("error on: " + at);
  }

  private TermList atermListToTermList(ATerm at) {
    if(at instanceof ATermList) {
      ATermList atl = (ATermList) at;
      TermList l = `concTerm();
      while(!atl.isEmpty()) {
        l = `concTerm(atermToTerm(atl.getLast()),l*);
        atl = atl.getPrefix();
      }
      return l;
    }

    throw new RuntimeException("error on: " + at);
  }

  public Constraint atermToConstraint(String term) {
    return atermToConstraint(SingletonFactory.getInstance().parse(term));
  }

  public Constraint atermToConstraint(ATerm at) {
    if(at instanceof ATermAppl) {
      ATermAppl appl = (ATermAppl) at;
      String name = appl.getName();
      if(name.equals("match")) {
        ATerm pattern = appl.getArgument(0);
        ATerm subject = appl.getArgument(1);
        return `Match(atermToTerm(pattern),atermToTerm(subject));
      } else if(name.equals("neg")) {
        ATerm term = appl.getArgument(0);
        return `Neg(atermToConstraint(term));
      } else if(name.equals("gt")) {
        ATermAppl term1 = (ATermAppl)appl.getArgument(0);
        ATermInt term2 = (ATermInt)appl.getArgument(1);
        return `GreaterThan(Variable(term1.getAFun().getName()),
            Appl(term2.getInt()+"",concTerm()));     		  

      } else if(name.equals("lt")) {
        ATermAppl term1 = (ATermAppl)appl.getArgument(0);
        ATermInt term2 = (ATermInt)appl.getArgument(1);
        return `LessThan(Variable(term1.getAFun().getName()),
            Appl(term2.getInt()+"",concTerm()));     		  

      }  
    }

    throw new RuntimeException("error on: " + at);
  }  

  public String formatConstraint(Constraint c){

    %match(Constraint c) {
      True() -> {
        return "T";	
      }
      False() ->{
        return "F";
      }
      Neg(cons) ->{
        return "Neg(" + formatConstraint(`cons) + ")";
      }
      and@And(_*) ->{
        String result = "(";
        %match(and){
          And(_*,x,Y*) ->{
            result += formatConstraint(`x) + " and ";
          }
        }
        return result.substring(0, result.length() - 5) + ")"; 
      }
      or@Or(_*) ->{
        String result = "<";
        %match(or){
          Or(_*,x,Y*) ->{
            result += formatConstraint(`x) + " or ";
          }
        }
        return result.substring(0, result.length() - 4) + ">"; 
      }
      Equal(pattern, subject) ->{
        return formatTerm(`pattern) + "=" + formatTerm(`subject); 
      }
      NEqual(pattern, subject) ->{
        return formatTerm(`pattern) + "!=" + formatTerm(`subject); 
      }
      Exists(Variable(name),cons) -> {
        return "exists " + `name + ", [ " + formatConstraint(`cons) + " ] "; 
      }			
      ForAll(Variable(name),cons) -> {				
        return "for all " + `name + ", [ " + formatConstraint(`cons) + " ] ";				
      }
      Match(pattern, subject) ->{
        return formatTerm(`pattern) + " << " + formatTerm(`subject); 
      }
    }

    return c.toString();
  }

  public String formatTerm(Term t){

    %match(Term t){
      ConsVariable(Variable(name),_) ->{
        return `name;
      }
      Variable(name) ->{
        return `name;
      }
      Appl(name, concTerm())->{
        return `name;
      }
      Anti(apl@Appl(_,_))->{
        return "!" + formatTerm(`apl);
      }
      Anti(name)->{
        return "!" + `name;
      }
      GenericGroundTerm(name) ->{
        return `name;
      }
      Appl(name, concTerm(x,Z*)) ->{

        TermList l = `Z*;
        String result = formatTerm(`x);

        while(!l.isEmptyconcTerm()){
          result = result + "," + formatTerm(l.getHeadconcTerm());
          l = l.getTailconcTerm();
        }

        return `name + "(" + result + ")"; 
      }

    }

    return t.toString();
  }

  private boolean foundVariable = false;

  public boolean containsVariable(Constraint c, Term v){

    foundVariable = false;

    try{		
      `InnermostId(ConstraintContainsVariable(v)).visitLight(c);
    }catch(VisitFailure e){
      throw new RuntimeException("VisitFailure occured:" + e);
    }

    return foundVariable;
  }

  %strategy ConstraintContainsVariable(v:Term) extends `Identity(){

    visit Constraint {
      Equal(p,_) ->{
        `InnermostId(TermContainsVariable(v)).visitLight(`p);
      }
      NEqual(p,_) ->{
        `InnermostId(TermContainsVariable(v)).visitLight(`p);
      }
    }
  }

  %strategy TermContainsVariable(v:Term) extends `Identity(){

    visit Term {
      var@Variable(_) ->{

        if (`var == v){					
          foundVariable = true;
        }
      }
    }
  }



}

