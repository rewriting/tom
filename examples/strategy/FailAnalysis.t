/*
 * Copyright (c) 2010, INPL, INRIA
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 * 	- Redistributions of source code must retain the above copyright
 * 	notice, this list of conditions and the following disclaimer.  
 * 	- Redistributions in binary form must reproduce the above copyright
 * 	notice, this list of conditions and the following disclaimer in the
 * 	documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the INRIA nor the names of its
 * 	contributors may be used to endorse or promote products derived from
 * 	this software without specific prior written permission.
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

package strategy;

import tom.library.sl.*;

public class FailAnalysis {

  %include { sl.tom }


  /**
   * simple function that tests if a strategy can potentially fail
   * based on strategy reflexivity 
   */

  public static boolean canFail(Strategy subject) {
    %match (subject) {
      Identity() -> { return false; }
      Fail() -> { return true; }
      Sequence(s1, s2*) -> { return canFail(`s1) || canFail(`s2); }
      Choice(s1, s2*) -> { return canFail(`s1) && canFail(`s2); }
      One[] -> { return true; }
      All(s) -> { return canFail(`s); }
      Mu[s2=s] -> { return canFail(`s); }
      MuVar[] -> { return false; }
    }
    return true;
  }

  public static void main(String[] args) {
    System.out.println("test if the following composed strategies can fail if their argument cannot fail");
    System.out.println("try: "+canFail(`Try(Identity())));
    System.out.println("TopDown: "+canFail(`TopDown(Identity())));
    System.out.println("TopDownCollect: "+canFail(`TopDownCollect(Identity())));
    System.out.println("TopDownStopOnSuccess: "+canFail(`TopDownStopOnSuccess(Identity())));
    System.out.println("BottomUp: "+canFail(`BottomUp(Identity())));
    System.out.println("OnceBottomUp: "+canFail(`OnceBottomUp(Identity())));
    System.out.println("OnceTopDown: "+canFail(`OnceTopDown(Identity())));
    System.out.println("Innermost: "+canFail(`Innermost(Identity())));
    System.out.println("Outermost: "+canFail(`Outermost(Identity())));
    System.out.println("Repeat: "+canFail(`Repeat(Identity())));
    System.out.println();
    System.out.println("test if the following composed strategies can fail if their argument can fail");
    System.out.println("try: "+canFail(`Try(Fail())));
    System.out.println("TopDown: "+canFail(`TopDown(Fail())));
    System.out.println("TopDownCollect: "+canFail(`TopDownCollect(Fail())));
    System.out.println("TopDownStopOnSuccess: "+canFail(`TopDownStopOnSuccess(Fail())));
    System.out.println("BottomUp: "+canFail(`BottomUp(Fail())));
    System.out.println("OnceBottomUp: "+canFail(`OnceBottomUp(Fail())));
    System.out.println("OnceTopDown: "+canFail(`OnceTopDown(Fail())));
    System.out.println("Innermost: "+canFail(`Innermost(Fail())));
    System.out.println("Outermost: "+canFail(`Outermost(Fail())));
    System.out.println("Repeat: "+canFail(`Repeat(Fail())));
  }

}
