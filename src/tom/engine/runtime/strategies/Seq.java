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
