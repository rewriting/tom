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
import java.util.Iterator;

public class Pick extends Strat {
  private boolean multistrat = false;

  public Pick() {}

  public Pick(Strat strat) {
    this.initialize(strat);
  }

  public Pick(StratList strats) {
    this.initialize(strats);
  }

  public Pick(Strat strat, Collection results) {
    this.initialize(strat);
    this.results = results;
  }

  //{{{ public void initialize(Strat strat)

  public void initialize(Strat strat) {
    StratList l = new StratList();
    l.add(strat);
    initialize(l);
  }
  // }}}  

  //{{{ public void initialize(StratList strats)

  public void initialize(StratList strats) {
    super.initialize(strats);
    // build SelectOne(Pick(S_1),_,Pick(S_n)) 
    if (strats.length() == 1) {
      // nothing ?
    }
    else if (strats.length() > 1) {
      multistrat = true;
    }
  }
  // }}} 

  public void run() {

    if (subject == null && subjects == null) {
      System.out.println(this + " subject not initialized");
      return;
    }

    if (multistrat == false ) {
      if (subject != null) {
        try {
          Strat task = strats.get(0);
          task.setSubject(subject);
          task.fork();

          while (!terminated) { yield(); }

          if (this.parent != null) { parent.addResults((Strat)this, results); }
          return;
        }
        catch (Exception ex) {
          System.out.println("Error Pick: " + this + " error : " + ex);
          ex.printStackTrace();
          return;
        }
      }
      if (subjects != null) {
        EvalGetOne getone = new EvalGetOne(this, subjects);
        this.invoke(getone);
        if (this.parent != null) { parent.addResults((Strat)this, results); }
        return;
      }
    }
    else {
      // build SelectOne(Pick(S_1),_,Pick(S_n))
      StratList newlist = new StratList();
      for (int i = 0; i < strats.length(); i++) {
        newlist.add(new Pick(strats.get(i))); // maybe use a copy
      }
      One one = new One(newlist);
      one.setParent(this);
      this.invoke(one);
      if (this.parent != null) { parent.addResults((Strat)this, results); }
      return;
    }
    return;
  }

  //{{{ public synchronized void addResults(Strat t, Collection res)

  public synchronized void addResults(Strat t, Collection res) {
    // It is a one, so we ensure we have only one result
    // We may need a "real" non-deterministic choice 
    if (multi == false) {
      if (res.size() != 0)
      {
        if (res.size() == 1) {
          results.addAll(res);
        }
        else {
          Iterator it = res.iterator();
          results.add(it.next());
        }
      } 
    } else {
      results.addAll(res);
    }
    setTerminated();
  }
  // }}}  

  //{{{ public void clean()

  public void clean() {
    this.reset();
    subjects = null;
    subject = null;
    results = new HashSet();
    multi = false;
    terminated = false;
    canFail = true;
    multistrat = false;
    for (int i = 0;i < strats.length();i++) {
      strats.get(i).clean();
    }
  }
  // }}}        

}
