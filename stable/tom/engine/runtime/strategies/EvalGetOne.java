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
import java.util.HashSet;
import java.util.Iterator;

import aterm.ATerm;

public class EvalGetOne extends Strat {
    private Strat strat;
  //private Collection subjects;
    private int callbacksExpected = 0;
    private volatile int callbacksReceived = 0;
    private Collection childs = new HashSet();

    public EvalGetOne() { }

    public EvalGetOne(Strat strat, Collection subjects) {
  this.strat = strat;
  this.setParent(strat);
  this.subjects = subjects;
  // don't forget : always use a copy !!
    }

    public void initialize(StratList strats) {
  // do nothing
    }

    public void run() {

  // split using strats
  Iterator it = subjects.iterator();
  try {
      while (it.hasNext()) {

    Strat task = strat.getClone();
    task.setParent(this);
    task.setSubject((ATerm)it.next());
    // we have to stop tasks when they are no more needed
    childs.add(task);
    callbacksExpected++;
    task.fork();
      }
  }
  catch(Exception e) {
      System.out.println(e);
      e.printStackTrace();
      // TODO
  }


  // join
  if (callbacksExpected == 0) {
      // no split...failure
      setTerminated();
      if (this.parent != null) { parent.addResults((Strat)this, results); }
      return;
  }

  while (results.isEmpty() && (callbacksReceived < callbacksExpected)) { yield(); }
  setTerminated();
  if (this.parent != null) { parent.addResults((Strat)this, results); }

  // do some cleanup
  Iterator it2 = childs.iterator();
  while(it2.hasNext()) {
      Strat child = (Strat) it2.next();
      child.cancel();
  }

  return;
    }

    public synchronized void addResults(Strat t, Collection res) {
  results.addAll(res);
  ++callbacksReceived;
    }

}

