package jtom.runtime.strategies;

import java.util.Collection;
import java.util.HashSet;

import jtom.runtime.Collect3;

public class Rule extends Strat {
  private Collect3 collect;

  public Rule(Collect3 collect) {
    this.collect = collect;
  }

  public Rule(Strat parent, Collect3 collect) {
    this.collect = collect;
    setParent(parent);
  }
    
  public void run() {
    if (subject == null && subjects == null) {
      System.out.println(this + " subject not initialized");
      return;
    }
	
    if (subject != null) {
      collect.apply(subject, results, this);
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

  protected Collect3 getCollect() { 
    return collect;
  }

  public Strat getClone() {
    Rule clone = new Rule(collect);
    return clone;
  }

  public void print() {
    System.out.print(this + " Collect: " + collect + ";\n");
    return;
  }
    
  public void addResults(Strat t, Collection res) {
    results.addAll(res);
  }

  //{{{ public void clean()

  public void clean() {
    this.reset();
    subjects = null;
    subject = null;
    results = new HashSet();
    multi = false;
    terminated = false;
    canFail = true;
  }
  // }}}  

  }
