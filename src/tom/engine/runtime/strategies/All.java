/*
  
    TOM - To One Matching Compiler

    Copyright (C) 2000-2004 INRIA
			                      Nancy, France.


    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Library General Public
    License as published by the Free Software Foundation; either
    version 2 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Library General Public License for more details.

    You should have received a copy of the GNU Library General Public
    License along with this library; if not, write to the Free
    Software Foundation, Inc., 59 Temple Place - Suite 330, Boston,
    MA 02111-1307, USA

    Pierre-Etienne Moreau	e-mail: Pierre-Etienne.Moreau@loria.fr
    Antoine Reilles       e-mail: Antoine.Reilles@loria.fr
*/

package jtom.runtime.strategies;

import java.util.Collection;
import java.util.HashSet;

public class All extends Strat {
  private int callbacksExpected = 0;
  private volatile int callbacksReceived = 0;

  public All() { }

  public All(StratList strats) {
    this.initialize(strats);
  }

  //{{{ public void run() 

  public void run() {

    if (subject == null && subjects == null) {
      System.out.println(this + " subject not initialized");
      return;
    }

    if (subject != null) {
      // split using strats
      for (int i = 0; i < strats.length(); i++) {
        try {
          Strat task = strats.get(i);
          task.setSubject(subject);
          callbacksExpected++;
          task.fork();
        } catch(Exception e) {
          System.out.println("Error during fork() in All");
          // TODO
        }
      }

      // join
      if (callbacksExpected == 0) {
        // no split...failure
        setTerminated();
        if (this.parent != null) { parent.addResults((Strat)this, results); }
        return;
      }

      while (callbacksReceived < callbacksExpected) { yield(); }
      setTerminated();
      if (this.parent != null) { parent.addResults((Strat)this, results); }

      return;
    }

    if (subjects != null) {
      EvalGetAll getall = new EvalGetAll(this, subjects);
      invoke(getall);
      setTerminated();
      if (this.parent != null) { parent.addResults((Strat)this, results); }
      return;
    }
    return;
  }
  //}}}        

  //{{{ public synchronized void addResults(Strat t, Collection res) 

  public synchronized void addResults(Strat t, Collection res) {
    results.addAll(res);
    ++callbacksReceived;
  }
  //}}}        

  //{{{ public void clean() 

  public void clean() {
    this.reset();
    subjects = null;
    subject = null;
    results = new HashSet();
    multi = false;
    terminated = false;
    canFail = true;
    callbacksExpected = 0;
    callbacksReceived = 0;
    for (int i = 0;i < strats.length();i++) {
      strats.get(i).clean();
    }
  }
  //}}}        

}

