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
