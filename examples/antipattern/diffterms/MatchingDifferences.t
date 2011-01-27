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

package antipattern.diffterms;

//import aterm.pure.SingletonFactory;
import aterm.*;
import aterm.pure.*;

import java.io.*;
import java.util.*;

import antipattern.*;
import antipattern.term.*;
import antipattern.term.types.*;


import tom.library.sl.*;

public class MatchingDifferences implements Matching {

  // %include{ atermmapping.tom }
  %include{ ../term/Term.tom }
  %include{ sl.tom }

  public Constraint simplifyAndSolve(Constraint c, Collection solution) {

//  System.out.println("Problem to solve:" + c);

    // the anti-pattern to match against
    Term ap = null;
    // the subject to match
    Term subject = null;

    %match(Constraint c){
      Match(p,s) ->{
        ap = `p;
        subject = `s;
      }
    }		

    Strategy decomposeRule = new DecomposeAP(`Identity());

    try {		

      Term result = (Term) `InnermostId(decomposeRule).visitLight(ap);
      return analyzeMembership(subject,result);			
    } catch (VisitFailure e) {

      System.out.println("reduction failed on: " + c);
      // e.printStackTrace();
    }
    return `False();
  }	

  // checks to see if t is inside td
  private Constraint analyzeMembership(Term t, Term td){

    // if the second is a simple term
    // then just verify if it filters
    %match(Term td){

      Appl(a,b) ->{
        return termMatch(t,td);
      }

      TermDiff(f,s)->{
        return `isInDifference(t,f,s);
      }
    }	

    throw new RuntimeException("analyzeMembership: Should never get here");
  }

  // checks to see if subjects in contained in first\second
  private Constraint isInDifference(Term subject, Term first, Term second){

    /*
     * System.out.println("Is in difference (subject/first/second):" + subject + " " +
     * first + " " + second);
     */

    %match(Term first, Term second){

      Appl(_,_),TermDiff(dif1,dif2)->{	

        if (termMatch(subject,first) == `True() && isInDifference(subject,`dif1,`dif2) == `False()){
          return `True();
        }

        return `False();
      }

      TermDiff(dif1,dif2),TermDiff(dif3,dif4)->{	

        if(`isInDifference(subject,dif1,dif2) == `True() && `isInDifference(subject,dif3,dif4) == `False()){
          return `True();
        }

        return `False();
      }

      TermDiff(dif1,dif2),Appl(_,_)->{	

        if(`isInDifference(subject,dif1,dif2) == `True() && `termMatch(subject,second) == `False()){
          return `True();
        }

        return `False();
      }

      Appl(_,_),Appl(_,_)->{	

        /*
         * if the subject is in the first, but is not in the second, then it
         * means that it belongs to the difference
         */
        if (termMatch(subject,first) == `True() && termMatch(subject,second) == `False()){
          return `True();
        }

        return `False();
      }

      Variable(_),Appl(_,_)->{				

        if (termMatch(subject,second) == `False()){
          return `True();
        }

        return `False();
      }

      Variable(_),TermDiff(dif1,dif2)->{	

        if(`isInDifference(subject,dif1,dif2) == `False()){
          return `True();
        }

        return `False();
      }
    }

    throw new RuntimeException("isInDifference: Should never get here");
    /* return `False(); */
  }

  /* matches two simple terms */
  private Constraint termMatch(Term subject, Term pattern){

    Strategy matchRule = new ClassicalPatternMatching(`Identity());

    try {
      /* System.out.println("Got:" + pattern + ", " + subject); */
      Constraint result = (Constraint) `InnermostId(matchRule).visitLight(`Match(pattern, subject));
      /*
       * System.out.println("Result:" + result); if the result is a Match or an
       * And, then this means success
       */
      %match(Constraint result){
        (Match|And)[] ->{
          System.out.println("Part of solution:" + result);
          return `True();
        }
      }
      return result;
    } catch (VisitFailure e) {
      System.out.println("reduction failed on: " + `Match(pattern, subject));
      throw new RuntimeException("termMatch: Should never get here");
    }		
  }
}
