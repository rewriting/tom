package jtom.runtime.strategies;

import EDU.oswego.cs.dl.util.concurrent.*;
import aterm.*;
import java.util.*;
import jtom.runtime.*;

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
