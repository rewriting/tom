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

package antipattern.baralgo;

import aterm.*;
import aterm.pure.*;

import java.io.*;
import java.util.*;

import antipattern.*;
import antipattern.term.*;
import antipattern.term.types.*;


import tom.library.sl.*;

public class Matching1 implements Matching {

  //%include{ atermmapping.tom }
  %include{ ../term/Term.tom }
  %include{ sl.tom }

  public Constraint simplifyAndSolve(Constraint c,Collection solution) {
   Strategy simplifyRule = new SimplifySystem(`Fail());
   Strategy solveRule = new SolveSystem(solution,`Fail());
   try { 
     return (Constraint) `Sequence(Innermost(simplifyRule),
           Repeat( Sequence( solveRule, Innermost(simplifyRule)))
    				 ).visitLight(c);
    } catch (VisitFailure e) {
      System.out.println("reduction failed on: " + c);
      //e.printStackTrace();
    }
   return `False();
  }        
}
