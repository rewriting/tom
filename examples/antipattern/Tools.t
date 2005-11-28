/*
 * Copyright (c) 2005-2006, INRIA
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

package antipattern;

import aterm.*;
import aterm.pure.*;

import java.io.*;
import java.util.*;

import antipattern.term.*;
import antipattern.term.types.*;

public class Tools {
  private TermFactory factory;

  %include{ term/Term.tom }
  %include{ atermmapping.tom }

  private final TermFactory getTermFactory() {
    return factory;
  }
  
  private final PureFactory getPureFactory() {
    return factory.getPureFactory();
  }

  Tools() {
    this(TermFactory.getInstance(SingletonFactory.getInstance()));;
  }
  
  Tools(TermFactory factory) {
    this.factory = factory;
  }

  public static void main(String[] args) {
    Tools tools = new Tools();
    System.out.println("\nRunning Matching1: \n");
    Matching test1 = new Matching2(tools.getTermFactory());
    tools.run(test1,args[0]);
    System.out.println("\nRunning Matching2: \n");
    Matching test2 = new Matching2(tools.getTermFactory());
    tools.run(test2,args[1]);
    System.out.println("\nRunning Matching3: \n");
    Matching test3 = new Matching3(tools.getTermFactory());
    tools.run(test3,args[2]);
  }

  public void run(Matching match, String fileName) {
	  
	BufferedReader br = null;
	try{  
		br = new BufferedReader(new FileReader( 
				match.getClass().getResource(fileName).getFile()));
	}catch(FileNotFoundException e){
		System.out.println("Can't find the input file 'input.txt' :" + e.getMessage());
		System.exit(0);
	}
    String s = null;
    try{
	    while((s = br.readLine()) != null ) {      
	      if(s.equals("-")) {
	        System.out.println("---------------------------------------");
	      } else {
	        ATerm at = getPureFactory().parse(s);	        
	        Constraint c = atermToConstraint(at);
          Collection solution = new HashSet();
	        System.out.println(s);
	        Constraint simplifiedConstraint = match.simplifyAndSolve(c,solution);
	        System.out.println(" --> " + simplifiedConstraint);
          if(simplifiedConstraint == `True()) {
            System.out.println(" sol = " + solution);
          }
	      }
	    }
    }catch(IOException e1){
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
      } else if(Character.isUpperCase(name.charAt(0)) && afun.getArity()==0) {
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
  
  private ConstraintList atermListToConstraintList(ATerm at) {
    if(at instanceof ATermList) {
      ATermList atl = (ATermList) at;
      ConstraintList l = `emptyConstraintList();
      while(!atl.isEmpty()) {
        l = `manyConstraintList(atermToConstraint(atl.getFirst()),l);
        atl = atl.getNext();
      }
      return l;
    }

    throw new RuntimeException("error on: " + at);
  }

  private TermList atermListToTermList(ATerm at) {
    if(at instanceof ATermList) {
      ATermList atl = (ATermList) at;
      TermList l = `emptyTermList();
      while(!atl.isEmpty()) {
        l = `manyTermList(atermToTerm(atl.getFirst()),l);
        atl = atl.getNext();
      }
      return l;
    }

    throw new RuntimeException("error on: " + at);
  }

  private Constraint atermToConstraint(ATerm at) {
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
 
}

