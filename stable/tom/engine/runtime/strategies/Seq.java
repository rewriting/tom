/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2004, Antoine Reilles
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
 *
 * INRIA, Nancy, France 
 * Antoine Reilles  e-mail: Antoine.Reilles@loria.fr
 *
 **/

package jtom.runtime.strategies;

import java.util.Collection;

public class Seq extends Strat {

  public Seq() {}

  public Seq(StratList strats) {
    this.initialize(strats);
  }

  public void run() {

    if (subject == null && subjects == null) {
      System.out.println(this + " subject not initialized");
      return;
    }

    if (subject != null) {
      Strat firsttask = strats.get(0);
      firsttask.setSubject(subject);
      invoke(firsttask);
      Collection tempsubject = results;
      if (strats.length() > 1) {
        for(int i = 1; i < strats.length(); i++)
        {
          Strat thentask = strats.get(i);
          thentask.setSubject(tempsubject);
          invoke(thentask);
          tempsubject = results;
        }
      }
      setTerminated();
      if (this.parent != null) { parent.addResults((Strat)this, results); }

      return;
    }
    if (subjects != null) {
      EvalGetAll getall = new EvalGetAll(this, subjects);
      invoke(getall);
      parent.addResults((Strat)this, results);
      return;
    }
    return;
  }

  public synchronized void addResults(Strat t, Collection res) {
    results = res;
  }

}
